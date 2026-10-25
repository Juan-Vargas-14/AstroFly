package datos.dto;

public class JugadorDTO {
    private String nombre;
    private double saldo;

    public JugadorDTO(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
