import datos.Jugador;
import datos.dao.JugadorDAO;
import datos.dao.JugadorDAOImpl;
import datos.dto.JugadorDTO;
import operaciones.ControladorJuego;
import vista.VentanaInicioSesion;
import vista.VentanaJuego;

import javax.swing.*;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Arrancamos el programa abriendo únicamente la ventana de login
                new VentanaInicioSesion().setVisible(true);
            }
        });
    }