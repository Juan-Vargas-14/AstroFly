import datos.Jugador;
import datos.dao.JugadorDAO;
import datos.dao.JugadorDAOImpl;
import datos.dto.JugadorDTO;
import operaciones.ControladorJuego;
import vista.VentanaJuego;

import javax.swing.*;
    public static void main(String[] args) {
        JugadorDAO dao = new JugadorDAOImpl();
        JugadorDTO jugadorDTO = dao.obtenerJugador("Abraham");
        Jugador jugador = new Jugador(jugadorDTO.getNombre(), jugadorDTO.getSaldo());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControladorJuego controlador=new ControladorJuego(jugador);
                new VentanaJuego(jugador, controlador).setVisible(true);
            }
        });
    }