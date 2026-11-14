package datos.dto;

public class JugadorDTO {
    private String nombre;
    private double saldo;
    private String contrasena;

    public JugadorDTO(String nombre, double saldo, String contrasena) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.contrasena = contrasena;
    }

    public JugadorDTO(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.contrasena = "";
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getContrasena() {
        return contrasena;
    }
}