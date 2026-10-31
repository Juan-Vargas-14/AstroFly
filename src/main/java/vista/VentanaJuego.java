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

        setTitle("AstroFly");
        setSize(800, 700);
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
        // Configuramos el layout principal de la ventana
        setLayout(new java.awt.BorderLayout());

        // --- PANEL NORTE: Saldo y Cerrar Sesión ---
        JPanel panel1 = new JPanel(new java.awt.BorderLayout());
        panel1.setOpaque(false);
        panel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));

        labelSaldo = new JLabel("Saldo: $" + String.format("%.2f", jugador.getSaldo()));
        labelSaldo.setForeground(java.awt.Color.WHITE);
        labelSaldo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
        panel1.add(labelSaldo, java.awt.BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFocusable(false); // Para evitar problemas con el foco
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panel1.add(btnCerrarSesion, java.awt.BorderLayout.EAST);

        add(panel1, java.awt.BorderLayout.NORTH); // Anclado arriba

        // --- PANEL CENTRO: Agrupa Multiplicador y Animación ---
        JPanel panelCentro = new JPanel(new java.awt.BorderLayout());
        panelCentro.setOpaque(false);

        // 1. Multiplicador (Anclado arriba dentro del centro, centrado horizontalmente)
        labelMultiplicador = new JLabel("1.00x");
        labelMultiplicador.setForeground(java.awt.Color.GREEN);
        labelMultiplicador.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
        labelMultiplicador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Margen para separar el texto del techo y darle aire antes del robot
        labelMultiplicador.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 0, 20, 0));

        panelCentro.add(labelMultiplicador, java.awt.BorderLayout.NORTH);

        // 2. Animación del Robot (Ocupa TODO el espacio sobrante en el centro)
        labelAnimacion = new JLabel();
        labelAnimacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAnimacion.setVerticalAlignment(javax.swing.SwingConstants.CENTER); // Centrado vertical puro
        if (gifVuelo != null) {
            labelAnimacion.setIcon(gifVuelo);
        }

        panelCentro.add(labelAnimacion, java.awt.BorderLayout.CENTER);

        add(panelCentro, java.awt.BorderLayout.CENTER); // Anclado al medio de la ventana

        // --- PANEL SUR: Controles de Apuesta  y Ranking ---
        JPanel panelSurPrincipal = new JPanel(new java.awt.BorderLayout());
        panelSurPrincipal.setOpaque(false);
        panelSurPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 30, 15));

        // Sub-panel de Apuestas (Centrado)
        JPanel panelApuesta = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));
        panelApuesta.setOpaque(false);

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

        panelApuesta.add(labelIndicador);
        panelApuesta.add(campoApuesta);
        panelApuesta.add(btnMas10);
        panelApuesta.add(btnMas50);
        panelApuesta.add(botonAccion);

        panelSurPrincipal.add(panelApuesta, java.awt.BorderLayout.CENTER);

        // Sub-panel Ranking (A la derecha)
        JPanel panelRanking = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        panelRanking.setOpaque(false);

        JButton btnRanking = new JButton("Ver Ranking");
        btnRanking.setFocusable(false);
        btnRanking.addActionListener(e -> new VentanaRanking().setVisible(true));

        panelRanking.add(btnRanking);

        panelSurPrincipal.add(panelRanking, java.awt.BorderLayout.EAST); // Anclado abajo a la derecha

        add(panelSurPrincipal, java.awt.BorderLayout.SOUTH); // Agregado a la ventana principal
    }
    private void cerrarSesion(){
        // Guarda el saldo actual en la base de datos por seguridad antes de salir
        datos.dao.JugadorDAO dao = new datos.dao.JugadorDAOImpl();
        dao.actualizarSaldo(jugador.getNombre(), jugador.getSaldo());

        // Abre la ventana de login
        VentanaInicioSesion login = new VentanaInicioSesion();
        login.setVisible(true);

        // Cierra la ventana del juego actual
        this.dispose();
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