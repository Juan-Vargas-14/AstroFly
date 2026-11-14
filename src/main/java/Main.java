import vista.VentanaInicioSesion;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Activar el diseño moderno de Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si falla, usa el diseño normal
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Arrancamos el programa abriendo únicamente la ventana de login
                new VentanaInicioSesion().setVisible(true);
            }
        });
    }
}