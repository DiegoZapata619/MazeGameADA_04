package game.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/*
    Renderiza el tablero del laberinto y captura los movimientos del jugador mediante el teclado.
    Ya no reconstruye el panel completo en cada movimiento, si las dimensiones de la matriz no
    cambiaron (mismo nivel), solo actualiza los íconos de las celdas que se modificaron.
    Si cambiaron (nuevo nivel), reconstruye la cuadrícula desde cero.
 */

public class MazeBoardPanel extends JPanel {

    //Notifica los movimientos del jugador sin que este panel conozca quién implementa la lógica de movimiento.
    public interface MoveListener {
        void onMove(int xScale, int yScale);
    }

    private final MoveListener moveListener;
    private final Map<String, ImageIcon> iconCache = new HashMap<>();

    private JLabel[][] labels;
    private String[][] currentMatrix;

    public MazeBoardPanel(MoveListener moveListener) {
        this.moveListener = moveListener;
        addKeyListener(new BoardKeyHandler());
        setFocusable(true); //Permite que el panel reciba el foco del teclado
    }

    /*
        Actualiza el panel con la matriz recibida. Reconstruye la cuadrícula completa si aún no existen
        labels o si cambiaron las dimensiones de la matriz (cambio de nivel), de los contrario solo
        actualiza las celdas que cambiaron.
     */

    public void render(String[][] matrix) {
        boolean rebuild =
                labels == null //Primer nivel: aún no hay labels
                        || labels.length != matrix.length //Cambió el número de filas
                        || labels[0].length != matrix[0].length; //Cambió el número de columnas

        if (rebuild) {
            rebuildGrid(matrix);
        } else {
            updateChangedCells(matrix);
        }

        currentMatrix = copyOf(matrix);
        requestFocusInWindow();
    }

    //Reconstruye la cuadrícula completa de labels. Se invoca únicamente cuando
    //cambian las dimensiones del laberitno (nuevo nivel)
    private void rebuildGrid(String[][] matrix) {
        removeAll(); //Quita todos los labels del panel anterior
        setLayout(new GridLayout(matrix.length, matrix[0].length));
        labels = new JLabel[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                JLabel cell = new JLabel("", iconFor(matrix[i][j]), JLabel.LEFT);
                labels[i][j] = cell;
                add(cell);
            }
        }
        revalidate(); //Reajusta la posición de toods los labels
        repaint(); //Redibuja los labels
    }

    //Actualiza únicamente las celdas cuyo contenido cambió respecto a la última matriz
    //renderizada, en lugar de reconstruir todo el panel.
    private void updateChangedCells(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (!matrix[i][j].equals(currentMatrix[i][j])) {
                    labels[i][j].setIcon(iconFor(matrix[i][j]));
                }
            }
        }
    }

    //Deuvelve el ícono correspondiente a un tipo de celda, reutilizándolo
    //desde la caché si ya fue cargado antes en vez de leerlo de nuevo desde disco.
    private ImageIcon iconFor(String cellType) {
        if (!iconCache.containsKey(cellType)) {
            ImageIcon icon = new ImageIcon("resources/images/" + cellType + ".png");
            iconCache.put(cellType, icon);
        }
        return iconCache.get(cellType);

    }

    //Crea una copia fila por fila de la matriz recibida, usada para comparar
    //contra la matriz siguiente en cada movimiento.
    private String[][] copyOf(String[][] source) {
        String[][] copy = new String[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    //Captura las teclas de movimeinto (flechas o WASD) y las traduce en llamadas.
    //Es la misma lógica que antes estaba en la GUI.
    private class BoardKeyHandler extends KeyAdapter {
        public void keyPressed(KeyEvent event) {
            //No se llama directamente a theArchitect, sino que se maneja
            //un moveListener para que clase no conozca directamente quien implementa
            switch (event.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    moveListener.onMove(-1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    moveListener.onMove(1, 0);
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    moveListener.onMove(0, -1);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    moveListener.onMove(0, 1);
                    break;
                default:
                    break;
            }
        }
    }
}