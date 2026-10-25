package datos.dao;

import datos.dto.JugadorDTO;

import java.io.*;

public class JugadorDAOImpl implements JugadorDAO{private static final String ARCHIVO = "jugador.txt";

    @Override
    public void guardarJugador(JugadorDTO jugadorDTO) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO))) {
            // Guarda los datos separados por coma
            writer.write(jugadorDTO.getNombre() + "," + jugadorDTO.getSaldo());
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @Override
    public JugadorDTO obtenerJugador(String nombre) {
        File archivo = new File(ARCHIVO);
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea = reader.readLine();
                if (linea != null && linea.contains(",")) {
                    String[] datos = linea.split(",");
                    return new JugadorDTO(datos[0], Double.parseDouble(datos[1]));
                }
            } catch (Exception e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
            }
        }
        // Si es la primera vez que juega o hay un error, devuelve el saldo inicial de $1000
        return new JugadorDTO(nombre, 1000.0);
    }
}
