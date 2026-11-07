import datos.Jugador;
import operaciones.MotorChoque;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class AstroFlyTest {
    // --- PRUEBAS DEL JUGADOR ---

    @Test
    public void testApostarSaldoSuficiente() {
        Jugador jugador = new Jugador("Abraham", 1000.0);
        boolean exito = jugador.apostar(200.0);

        assertTrue(exito);
        assertEquals(800.0, jugador.getSaldo(), 0.01);
    }

    @Test
    public void testApostarSaldoInsuficiente() {
        Jugador jugador = new Jugador("Abraham", 50.0);
        boolean exito = jugador.apostar(100.0);

        assertFalse(exito);
        assertEquals(50.0, jugador.getSaldo(), 0.01);
    }

    @Test
    public void testApostarMontoInvalido() {
        Jugador jugador = new Jugador("Abraham", 100.0);
        boolean exito = jugador.apostar(-50.0);

        assertFalse(exito);
        assertEquals(100.0, jugador.getSaldo(), 0.01);
    }

    @Test
    public void testAgregarGanancia() {
        Jugador jugador = new Jugador("Abraham", 100.0);
        jugador.agregarGanancia(150.0);

        assertEquals(250.0, jugador.getSaldo(), 0.01);
    }

    // --- PRUEBAS DEL MOTOR DE CHOQUE ---

    @Test
    public void testPuntoChoqueMinimo() {
        MotorChoque motor = new MotorChoque();
        double puntoChoque = motor.generarPuntoChoque();

        // El multiplicador siempre debe arrancar en 1.00x o más, nunca menos.
        assertTrue(puntoChoque >= 1.0, "El multiplicador de choque no puede ser menor a 1.0");
    }

    @Test
    public void testGeneracionAleatoria() {
        MotorChoque motor = new MotorChoque();
        double choque1 = motor.generarPuntoChoque();
        double choque2 = motor.generarPuntoChoque();

        // Comprueba que el motor genera resultados dinámicos y no un número estático
        assertNotEquals(choque1, choque2, "El motor debería generar valores aleatorios distintos entre rondas");
    }
    @Test
    public void testCalcularGananciaApuesta() {
        datos.Apuesta apuesta = new datos.Apuesta(20.0);

        // SIMULAMOS QUE EL JUGADOR SE RETIRÓ A TIEMPO Y GANÓ
        apuesta.setGanada(true);

        double ganancia = apuesta.calcularPremio(2.50);

        // 20 * 2.50 = 50.0
        assertEquals(50.0, ganancia, 0.01, "El cálculo de la ganancia no es correcto");
    }
    @Test
    public void testIniciarRondaDescuentaSaldo() {
        Jugador jugador = new Jugador("Abraham", 1000.0);
        operaciones.ControladorJuego controlador = new operaciones.ControladorJuego(jugador);

        // Iniciamos ronda apostando 100
        boolean inicioExitoso = controlador.iniciarRonda(100.0);

        assertTrue(inicioExitoso);
        assertEquals(900.0, jugador.getSaldo(), 0.01, "El saldo no se descontó al iniciar la ronda");
    }
    @Test
    public void testIniciarRondaSinSaldo() {
        Jugador jugador = new Jugador("Abraham", 50.0);
        operaciones.ControladorJuego controlador = new operaciones.ControladorJuego(jugador);

        // Intenta apostar 100 teniendo solo 50
        boolean inicioExitoso = controlador.iniciarRonda(100.0);

        assertFalse(inicioExitoso, "El juego no debería iniciar si no hay saldo suficiente");
        assertEquals(50.0, jugador.getSaldo(), 0.01, "El saldo no debió cambiar");
    }
}
