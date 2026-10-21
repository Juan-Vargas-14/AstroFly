package datos;

public class Apuesta {
    private double montoApostado;
    private double multiplicadorRetiro;
    private boolean ganada;

    public Apuesta( double montoApostado){
        this.montoApostado=montoApostado;
        this.multiplicadorRetiro=0.0;
        this.ganada=false;
    }
    public void resolverComoGanada(double multiplicadorActual){
        this.multiplicadorRetiro=multiplicadorActual;
        this.ganada=true;
    }
    public double calcularPremio(){
        if (ganada)return montoApostado*multiplicadorRetiro;
        return 0.0; //si no gana el premio es cero
    }
}
