package operaciones;

import java.util.Random;
public class MotorChoque {
    private Random rng =new Random();

    public double generarPuntoChoque(){
        double probabilidad=rng.nextDouble();
        if(probabilidad<0.85){
            return 1.0+(rng.nextDouble()*4.0); //85% de probabilidad: Choque entre 1x y 5x
        }else{
            return 5.0 + (rng.nextDouble()*15.0); //15% de probabilidad: Vuelo alto hasta 20x
        }
    }
}
