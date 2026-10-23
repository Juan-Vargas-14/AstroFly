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
}
