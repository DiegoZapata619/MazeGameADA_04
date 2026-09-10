import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

//Clase que gestiona el uso del panel en cada movimiento o carga de archivo .maz

public class MazeBoardPanel extends JPanel {
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
        //este setting permite que el componente gráfico reciba foco del teclado
        setFocusable(true);
    }

    /*
    Método principal que actualiza el panel del juego
    Reconstruye todo si no existen labels en el layout o si hubo un cambio
    las dimensiones, de lo contrario, solo actualiza lo que cambió
     */

    public void render(String[][] matrix) {
        boolean rebuild =
                //si es primer nivel y no hay labelss
                labels == null
                        //si tamaño cambió = cambió de nivel
                        || labels.length != matrix.length //distinto numero de filas
                        || labels[0].length != matrix[0].length; //distinto numero de columnas

        if (rebuild) {
            rebuildGrid(matrix);
        } else {
            updateChangedCells(matrix);
        }

        currentMatrix = copyOf(matrix);
        requestFocusInWindow();
    }

    //reconstruye el panel cada que ocurre un cambio de nivel
    private void rebuildGrid(String[][] matrix) {
        //remueve todas las labels del contenedor principal (panel GridLayout)
        removeAll();
        setLayout(new GridLayout(matrix.length, matrix[0].length));
        labels = new JLabel[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                JLabel cell = new JLabel("", iconFor(matrix[i][j]), JLabel.LEFT);
                labels[i][j] = cell;
                add(cell);
            }
        }
        //revalida la posición de todos los labels para que se mantengan con tamaño ideal
        revalidate();
        //redibuja los labels
        repaint();
    }

    //ahora, en lugar de reconstruir desde 0 la tabla por cada movimiento
    //se cambia únicamente las celdas que se vieron afectadas, es decir
    //aquellas que no coinciden con la copia de la tabla (
    private void updateChangedCells(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (!matrix[i][j].equals(currentMatrix[i][j])) {
                    labels[i][j].setIcon(iconFor(matrix[i][j]));
                }
            }
        }
    }

    //En lugar de crear labels una y otra vez, se crea un hashmap que guarda
    //cada label usado hasta el momento
    //si no existe, usa cellType (normalmente un string tipo "H", "D", etc)
    //y crea esa clave para uso posterior
    private ImageIcon iconFor(String cellType) {
        if (!iconCache.containsKey(cellType)) {
            ImageIcon icon = new ImageIcon("images/" + cellType + ".png");
            iconCache.put(cellType, icon);
        }
        return iconCache.get(cellType);

    }

    //copia fila por fila la matriz enviada como parámetro
    //auxiliar para hacer una copia del tablero y comparar por cada movimiento
    private String[][] copyOf(String[][] source) {
        String[][] copy = new String[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    //copia idéntica del keyHandler que anteriormente estaba en la GUI
    private class BoardKeyHandler extends KeyAdapter {
        public void keyPressed(KeyEvent event) {
            //no se llama directamente a theArchitect, sino que se maneja
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