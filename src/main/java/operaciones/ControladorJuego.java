package operaciones;

import datos.Apuesta;
import datos.Jugador;
public class ControladorJuego {
    public enum EstadoJuego{ESPERA,VUELO,CHOQUE}
    private Jugador jugadorActual;
    private Apuesta apuestaActual;
    private MotorChoque motor;

    private EstadoJuego estado;
    private double multiplicadorEnPantalla;
    private double puntoChoqueOculto;

    public ControladorJuego(Jugador jugador){
        this.jugadorActual=jugador;
        this.motor=new MotorChoque();
        this.estado=EstadoJuego.ESPERA;
    }
    public boolean iniciarRonda(double montoApostado){
        if(estado != EstadoJuego.ESPERA) return false;
        if(!jugadorActual.apostar(montoApostado)) return false;

        apuestaActual= new Apuesta(montoApostado);
        puntoChoqueOculto= motor.generarPuntoChoque();
        multiplicadorEnPantalla=1.0;
        estado=EstadoJuego.VUELO;
        return true;
    }
    public void procesarTickTimer(){
        if(estado !=EstadoJuego.VUELO)return;

        multiplicadorEnPantalla+=0.05; //Aumenta el multiplicador

        if(multiplicadorEnPantalla>=puntoChoqueOculto){
            multiplicadorEnPantalla=puntoChoqueOculto;
            estado=EstadoJuego.CHOQUE; //Bot explota
        }
    }
    public void evaluarRetiro(){
        if(estado!=EstadoJuego.VUELO)return;

        apuestaActual.resolverComoGanada(multiplicadorEnPantalla);
        jugadorActual.agregarGanancia(apuestaActual.calcularPremio()); //Paga
        estado=EstadoJuego.CHOQUE; //Finaliza ronda
    }
    public EstadoJuego getEstado(){return estado;}
    public double getMultiplicadorEnPantalla(){return multiplicadorEnPantalla;}
}
