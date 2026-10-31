package datos.dao;

import datos.dto.JugadorDTO;

import java.util.List;

public interface JugadorDAO {
    boolean registrarJugador(JugadorDTO jugador);
    boolean validarInicioSesion(String nombre, String contrasena);
    JugadorDTO obtenerJugador(String nombre);
    void actualizarSaldo(String nombre, double nuevoSaldo);
    List<JugadorDTO>obtenerRanking();
}
