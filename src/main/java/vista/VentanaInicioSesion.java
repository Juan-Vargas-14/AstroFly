package vista;
import datos.Jugador;
import datos.dao.JugadorDAO;
import datos.dao.JugadorDAOImpl;
import datos.dto.JugadorDTO;
import operaciones.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class VentanaInicioSesion extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JugadorDAO jugadorDAO;

    public VentanaInicioSesion() {
        // Inicializar conexión a la base de datos
        jugadorDAO = new JugadorDAOImpl();

        setTitle("AstroFly - Inicio de Sesión");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 20));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelUsuario = new JLabel("Usuario:");
        campoUsuario = new JTextField();

        JLabel labelPassword = new JLabel("Contraseña:");
        campoContrasena = new JPasswordField();

        panelCentral.add(labelUsuario);
        panelCentral.add(campoUsuario);
        panelCentral.add(labelPassword);
        panelCentral.add(campoContrasena);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(e -> validarCredenciales());

        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.addActionListener(e -> registrarUsuario());

        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void validarCredenciales() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jugadorDAO.validarInicioSesion(usuario, contrasena)) {
            // Obtener datos del jugador desde la BD
            JugadorDTO dto = jugadorDAO.obtenerJugador(usuario);

            // Instanciar modelo y controlador
            Jugador jugador = new Jugador(dto.getNombre(), dto.getSaldo());
            ControladorJuego controlador = new ControladorJuego(jugador);

            // Abrir ventana de juego y cerrar inicio de sesion
            VentanaJuego ventanaJuego = new VentanaJuego(jugador, controlador);
            ventanaJuego.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Inicio de Sesion", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos para registrarte.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Saldo inicial de regalo: $1000.0
        JugadorDTO nuevoJugador = new JugadorDTO(usuario, 1000.0, new String(campoContrasena.getPassword()));

        if (jugadorDAO.registrarJugador(nuevoJugador)) {
            JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puedes iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            campoUsuario.setText("");
            campoContrasena.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar. El nombre de usuario ya podría estar en uso.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
