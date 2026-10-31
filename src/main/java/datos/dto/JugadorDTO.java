package datos.dto;

public class JugadorDTO {
    private String nombre;
    private double saldo;
    private String contrasena;

    // Constructor 1: El que usa la base de datos (3 parámetros)
    public JugadorDTO(String nombre, double saldo, String password) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.contrasena = contrasena;
    }

    // Constructor 2: Por si tu código viejo aún manda solo 2 parámetros
    public JugadorDTO(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.contrasena = "";
    }

    public String getNombre() { return nombre; }
    public double getSaldo() { return saldo; }
    public String getContrasena() { return contrasena; }

    public void setSaldo(double saldo) { this.saldo = saldo; }
}
