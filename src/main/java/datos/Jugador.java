package datos;

public class Jugador {
    private String nombre;
    private double saldo;

    public Jugador(String nombre,double saldoInicial){
        this.nombre =nombre;
        this.saldo=saldoInicial;
    }
    public boolean apostar(double monto) {
        if (monto > 0 && this.saldo >= monto) {
            this.saldo = this.saldo - monto;

            this.saldo = Math.round(this.saldo * 100.0) / 100.0;
            return true;
        }
        return false;
    }
    public void agregarGanancia(double monto) {
        this.saldo = this.saldo + monto;
        // Redondea a 2 decimales
        this.saldo = Math.round(this.saldo * 100.0) / 100.0;
    }
    public String getNombre(){return nombre;}
    public double getSaldo(){return saldo;}
}
