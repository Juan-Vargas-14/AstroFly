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

    private JLabel labelMultiplicador;
    private JLabel labelSaldo;
    private JButton botonAccion;
    private javax.swing.Timer temporizador;

    private Image imagenFondo;
    public VentanaJuego(Jugador jugador, ControladorJuego controlador) {
        this.jugador = jugador;
        this.controlador = controlador;

        cargarFondo();

        setTitle("AstroFly - MVP");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

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
        labelSaldo = new JLabel("Saldo: $" + String.format("%.2f",jugador.getSaldo()));
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

        JPanel panel3 = new JPanel();
        panel3.setOpaque(false);
        add(panel3);

        JPanel panel4 = new JPanel();
        panel4.setOpaque(false);
        botonAccion = new JButton("APOSTAR $200 E INICIAR");


        botonAccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarClicBoton();
            }
        });
        panel4.add(botonAccion);
        add(panel4);
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
                        botonAccion.setText("JUGAR DE NUEVO");
                    }
                }
            }
        });
    }
    private void manejarClicBoton() {
        if (controlador.getEstado() == ControladorJuego.EstadoJuego.ESPERA) {
            if (controlador.iniciarRonda(200.0)) {            // Apuesta fija temporal
                botonAccion.setText("¡RETIRAR GANANCIA!");
                labelSaldo.setText("Saldo: $" + jugador.getSaldo());
                temporizador.start();                        // Inicia la animación
            } else {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente.");
            }
        }
        else if (controlador.getEstado() == ControladorJuego.EstadoJuego.VUELO) {
            controlador.evaluarRetiro();                     // Cobra a tiempo
            temporizador.stop();
            labelSaldo.setText("Saldo: $" + jugador.getSaldo());
            labelMultiplicador.setForeground(Color.BLUE);
            botonAccion.setText("JUGAR DE NUEVO");
        }
        else if (controlador.getEstado() == ControladorJuego.EstadoJuego.CHOQUE) {
            labelMultiplicador.setText("1.00x");
            labelMultiplicador.setForeground(Color.GREEN);
            botonAccion.setText("APOSTAR $200 E INICIAR");
            controlador = new ControladorJuego(jugador);     // Resetea para la siguiente ronda
        }
    }

    // Punto de entrada de la aplicación
    public static void main(String[] args) {
        // Crea una partida efímera con $1000 iniciales
        Jugador jugadorPrueba = new Jugador("Abraham", 1000.0);
        ControladorJuego controladorPrueba = new ControladorJuego(jugadorPrueba);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaJuego(jugadorPrueba, controladorPrueba).setVisible(true);
            }
        });
    }
}