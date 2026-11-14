package datos.dao;

import datos.dto.JugadorDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAOImpl implements JugadorDAO {
    private static final String URL = "jdbc:postgresql://ep-bitter-sunset-b6wbzqvy-pooler.c-2.sa-east-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USUARIO = "neondb_owner";
    private static final String CONTRA = "npg_UmEKaJi5n1zw";

    public JugadorDAOImpl() {
        crearTablaSiNoExiste();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRA);
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS jugadores ("
                + "nombre TEXT PRIMARY KEY, "
                + "saldo DOUBLE PRECISION NOT NULL, "
                + "contrasena TEXT NOT NULL"
                + ");";
        try (Connection conexion = conectar();
             Statement statement = conexion.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    @Override
    public boolean registrarJugador(JugadorDTO jugador) {
        String sql = "INSERT INTO jugadores(nombre, saldo, contrasena) VALUES(?,?,?)";
        try (Connection conexion = conectar();
             PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            preparedStatement.setString(1, jugador.getNombre());
            preparedStatement.setDouble(2, jugador.getSaldo());
            preparedStatement.setString(3, jugador.getContrasena());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al registrar (quizás el usuario ya existe): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validarInicioSesion(String nombre, String contrasena) {
        String sql = "SELECT * FROM jugadores WHERE nombre = ? AND contrasena = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre.trim());
            pstmt.setString(2, contrasena.trim());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al validar login: " + e.getMessage());
            return false;
        }
    }

    @Override
    public JugadorDTO obtenerJugador(String nombre) {
        String sql = "SELECT * FROM jugadores WHERE nombre = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new JugadorDTO(rs.getString("nombre"), rs.getDouble("saldo"), rs.getString("contrasena"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener jugador: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void actualizarSaldo(String nombre, double nuevoSaldo) {
        String sql = "UPDATE jugadores SET saldo = ? WHERE nombre = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setString(2, nombre);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar saldo: " + e.getMessage());
        }
    }

    @Override
    public List<JugadorDTO> obtenerRanking() {
        List<JugadorDTO> ranking = new ArrayList<>();
        String sql = "SELECT * FROM jugadores ORDER BY saldo DESC LIMIT 10";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ranking.add(new JugadorDTO(rs.getString("nombre"), rs.getDouble("saldo"), rs.getString("contrasena")));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ranking: " + e.getMessage());
        }
        return ranking;
    }
}
