package datos.dao;

import datos.dto.JugadorDTO;

public interface JugadorDAO {
    void guardarJugador(JugadorDTO jugador);
    JugadorDTO obtenerJugador(String nombre);
}
