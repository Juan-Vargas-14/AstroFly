package datos;

public class Jugador {
    private String nombre;
    private double saldo;

    public Jugador(String nombre,double saldoInicial){
        this.nombre =nombre;
        this.saldo=saldoInicial;
    }
    public boolean apostar(double monto){
        if (monto<=0 || monto>saldo) return false;
        this.saldo-=monto;
        return true;
    }
    public void agregarGanancia(double monto){
        if (monto > 0)this.saldo+=monto;
    }
    public String getNombre(){return nombre;}
    public double getSaldo(){return saldo;}
}
