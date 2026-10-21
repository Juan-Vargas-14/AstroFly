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
import java.io.IOException;


public class VentanaJuego extends JFrame {
    private ControladorJuego controlador;
    private Jugador jugador;

    private JLabel labelMultiplicador;
    private JLabel labelSaldo;
    private JButton botonAccion;
    private Timer temporizador;

    private Image imagenFondo;

    public VentanaJuego(Jugador jugador, ControladorJuego controlador) {
        this.jugador = jugador;
        this.controlador = controlador;

        setTitle("AstroFly - MVP");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

        inicializarComponentes();
        configurarTemporizador();
    }

    private void inicializarComponentes() {
        labelSaldo = new JLabel("Saldo: $" + jugador.getSaldo(), SwingConstants.CENTER);
        labelSaldo.setFont(new Font("Arial", Font.BOLD, 16));

        labelMultiplicador = new JLabel("1.00x", SwingConstants.CENTER);
        labelMultiplicador.setFont(new Font("Arial", Font.BOLD, 48));
        labelMultiplicador.setForeground(Color.GREEN);

        botonAccion = new JButton("APOSTAR $20 E INICIAR");
        botonAccion.setFont(new Font("Arial", Font.BOLD, 18));

        botonAccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarClicBoton();
            }
        });

        add(labelSaldo);
        add(labelMultiplicador);
        add(botonAccion);
    }

    private void configurarTemporizador() {
        // Ejecuta la actualización 20 veces por segundo
        temporizador = new Timer(50, new ActionListener() {
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
            if (controlador.iniciarRonda(20.0)) {            // Apuesta fija temporal
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
            botonAccion.setText("APOSTAR $20 E INICIAR");
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