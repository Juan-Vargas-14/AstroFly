package vista;

import datos.dao.JugadorDAO;
import datos.dao.JugadorDAOImpl;
import datos.dto.JugadorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaRanking extends JFrame{
    public VentanaRanking(){
        setTitle("AstroFly - Ranking Global");
        setSize(400,350);
        setLocationRelativeTo(null); //centrar en pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // para que alcerrar el ranking no se cierre el juego
        inicializarComponentes();
    }
    private void inicializarComponentes(){
        setLayout(new BorderLayout());

        // Título superior
        JLabel titulo = new JLabel("Top 10 Jugadores", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // Configuración de la tabla
        String[] columnas = {"Posición", "Jugador", "Saldo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setEnabled(false); // Hace que la tabla sea de solo lectura
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        // Obtener datos de SQLite y llenar la tabla
        JugadorDAO dao = new JugadorDAOImpl();
        List<JugadorDTO> ranking = dao.obtenerRanking();

        int posicion = 1;
        for (JugadorDTO j : ranking) {
            modelo.addRow(new Object[]{
                    posicion,
                    j.getNombre(),
                    "$" + String.format("%.2f", j.getSaldo())
            });
            posicion++;
        }

        // Agregar scroll por si hay muchos datos
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Botón inferior para cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> this.dispose());
        JPanel panelSur = new JPanel();
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);
    }
}
