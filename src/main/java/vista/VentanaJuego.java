package vista;

import datos.Jugador;
import operaciones.ControladorJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//para imagenes *recordar para aprendizaje*
import java.io.InputStream;
import javax.imageio.ImageIO;

public class VentanaJuego extends JFrame {
    private ControladorJuego controlador;
    private Jugador jugador;

    private javax.swing.JTextField campoApuesta;
    private JLabel labelMultiplicador;
    private JLabel labelSaldo;
    private JButton botonAccion;
    private javax.swing.Timer temporizador;

    private Image imagenFondo;

    private javax.swing.JLabel labelAnimacion;
    private javax.swing.ImageIcon gifVuelo;
    private javax.swing.ImageIcon gifGanador;
    private javax.swing.ImageIcon gifPerdedor;

    public VentanaJuego(Jugador jugador, ControladorJuego controlador) {
        this.jugador = jugador;
        this.controlador = controlador;

        cargarFondo();

        setTitle("AstroFly - MVP");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

        try {
            gifVuelo = new javax.swing.ImageIcon(getClass().getClassLoader().getResource("volando.gif"));
            gifGanador = new javax.swing.ImageIcon(getClass().getClassLoader().getResource("ganador.gif"));
            gifPerdedor = new javax.swing.ImageIcon(getClass().getClassLoader().getResource("perdedor.gif"));
        } catch (Exception e) {
            System.out.println("Error al cargar los GIFs: " + e.getMessage());
        }

        JPanel panelFondo = new JPanel(new GridLayout(4, 1)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    // Dibuja la imagen estirada al tamaño de la ventana
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(panelFondo);

        inicializarComponentes();
        configurarTemporizador();
    }

    private void cargarFondo() {
        try {
            InputStream is = getClass().getResourceAsStream("/fondo.png");
            if (is != null) {
                imagenFondo = ImageIO.read(is);
            } else {
                System.out.println("No se encontró la imagen fondo.png");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    private void inicializarComponentes() {
        //paneles
        JPanel panel1 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));
        panel1.setOpaque(false);
        labelSaldo = new JLabel("Saldo: $" + String.format("%.2f", jugador.getSaldo()));
        labelSaldo.setForeground(Color.WHITE);
        labelSaldo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
        panel1.add(labelSaldo);
        add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setOpaque(false);
        labelMultiplicador = new JLabel("1.00x");
        labelMultiplicador.setForeground(Color.GREEN);
        labelMultiplicador.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
        panel2.add(labelMultiplicador);
        add(panel2);

        JPanel panel3 = new JPanel(new java.awt.GridBagLayout());
        panel3.setOpaque(false);

// 1. Inicializamos la etiqueta aquí, asegurándonos de que ya existe en memoria
        labelAnimacion = new JLabel();
        labelAnimacion.setHorizontalAlignment(JLabel.CENTER);

// 2. Opcional: le pones un GIF por defecto al arrancar la ventana
        if (gifVuelo != null) {
            labelAnimacion.setIcon(gifVuelo);
        }

        panel3.add(labelAnimacion);
        add(panel3);

        JPanel panel4 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));
        panel4.setOpaque(false);

        JLabel labelIndicador = new JLabel("Apuesta: $");
        labelIndicador.setForeground(java.awt.Color.WHITE);
        labelIndicador.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

        campoApuesta = new javax.swing.JTextField("20", 5);
        campoApuesta.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

        JButton btnMas10 = new JButton("+10");
        btnMas10.addActionListener(e -> sumarApuesta(10));

        JButton btnMas50 = new JButton("+50");
        btnMas50.addActionListener(e -> sumarApuesta(50));

        botonAccion = new JButton("INICIAR");
        botonAccion.addActionListener(e -> manejarClicBoton());

        panel4.add(labelIndicador);
        panel4.add(campoApuesta);
        panel4.add(btnMas10);
        panel4.add(btnMas50);
        panel4.add(botonAccion);
        add(panel4);


    }

    private void sumarApuesta(int monto) {
        try {
            int actual = Integer.parseInt(campoApuesta.getText());
            campoApuesta.setText(String.valueOf(actual + monto));
        } catch (NumberFormatException ex) {
            campoApuesta.setText(String.valueOf(monto)); // Si el usuario borró todo o puso letras
        }
    }

    private void configurarTemporizador() {
        temporizador = new javax.swing.Timer(70, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.getEstado() == ControladorJuego.EstadoJuego.VUELO) {
                    controlador.procesarTickTimer();
                    labelMultiplicador.setText(String.format("%.2fx", controlador.getMultiplicadorEnPantalla()));

                    if (controlador.getEstado() == ControladorJuego.EstadoJuego.CHOQUE) {
                        temporizador.stop();
                        labelMultiplicador.setForeground(Color.RED);
                        botonAccion.setText("INICIAR");

                        // Cambiar al GIF de perdedor
                        if (gifPerdedor != null) {
                            labelAnimacion.setIcon(gifPerdedor);
                        }
                    }
                }
            }
        });
    }

    private void manejarClicBoton() {
        if (controlador.getEstado() == ControladorJuego.EstadoJuego.ESPERA || controlador.getEstado() == ControladorJuego.EstadoJuego.CHOQUE) {
            try {
                double monto = Double.parseDouble(campoApuesta.getText());

                if (controlador.iniciarRonda(monto)) {
                    labelSaldo.setText("Saldo: $" + jugador.getSaldo());
                    botonAccion.setText("RETIRAR");
                    labelMultiplicador.setText("1.00x");
                    labelMultiplicador.setForeground(java.awt.Color.GREEN);

                    // Cambiar al GIF de vuelo
                    if (gifVuelo != null) {
                        labelAnimacion.setIcon(gifVuelo);
                    }

                    if (temporizador != null) {
                        temporizador.start();
                    }
                }
            } catch (NumberFormatException ex) {
                campoApuesta.setText("20");
            }
        } else if (controlador.getEstado() == ControladorJuego.EstadoJuego.VUELO) {
            controlador.evaluarRetiro();
            labelSaldo.setText("Saldo: $" + jugador.getSaldo());
            botonAccion.setText("INICIAR");

            // Detenemos el temporizador porque ya se retiró con éxito
            if (temporizador != null) {
                temporizador.stop();
            }

            // Cambiar al GIF de ganador
            if (gifGanador != null) {
                labelAnimacion.setIcon(gifGanador);
            }
        }
    }
}