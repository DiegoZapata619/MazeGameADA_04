package game;

import game.controller.GameController;
import game.view.CountdownTimer;
import game.view.MazeBoardPanel;
import game.view.MazeMenuBar;
import game.view.ScoreGui;

import javax.swing.*;
import java.awt.*;


/*
    Ventana principal del juego. Tras la refactorización, esta clase solo coordina los eventos que ocurren,
    actualiza las vistas y renderiza los componentes visuales, ya que no contiene reglas del juego ni lógica
    construcción de la matriz.
    Responsabilidades:
    - GameController: Contiene las reglas del juego y es utilizado por esta GUI para accionar
    - MazeBoardPanel: Renderiza el tablero, ya no se construye en cada movimiento,
        solo cuando cambian las dimensiones del laberinto (cambio de nivel)
    - MazeMenuBar: Despliega los componentes del menú principal
    - CountdownTimer: Encapsula la lógica de la barra de progreso de cada nivel
    - SoundController: Reproduce sonidos dinámicos según los movimientos del jugador
 */
public class GameGui extends JFrame
        implements MazeMenuBar.Listener, MazeBoardPanel.MoveListener, CountdownTimer.Listener {

    public static void main(String[] args) {
        new GameGui();
    }

    private final GameController controller = new GameController();
    private final Container cp;
    private final JLabel splashLabel;
    private final MazeBoardPanel board;
    private final JPanel progressPanel = new JPanel();
    private final JLabel diamondsLabel = new JLabel("", JLabel.CENTER);

    private CountdownTimer timer;

    public GameGui() {
        super("Maze, a game of wondering");
        cp = getContentPane();

        splashLabel = new JLabel("", new ImageIcon("resources/images/yeababyyea.jpg"), JLabel.LEFT);
        cp.add(splashLabel);

        board = new MazeBoardPanel(this);

        setJMenuBar(new MazeMenuBar(this));

        pack();
        setVisible(true);
    }

    // Implementación de MazeMenuBarListener
    // Permite que cada opción tenga una interacción a través de Gamecontroller

    public void newGame() {
        controller.startNewGame();
        showBoard();
    }

    public void openFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            controller.openFile(chooser.getSelectedFile().getName());
            showBoard();
        }
    }

    public void enterName() {
        controller.promptForPlayerName();
    }

    public void openHighScore() {
        ScoreGui sg = new ScoreGui();
        sg.ScoreGui();
    }

    public void onSaveScore() {
        controller.saveScore();
    }

    public void onExit() {
        if (timer != null) {
            timer.stop();
        }
        System.exit(0);
    }

    // Implementación de moveListener de game.controller.view.MazeBoardPanel.
    // Captura los movimientos del jugador reportados por el tablero

    public void onMove(int xScale, int yScale) {
        boolean levelComplete = controller.move(xScale, yScale);
        refreshBoard();
        if (levelComplete) {
            loadNextLevel();
        }
    }

    // Implementación de CountdownTimer.Listener
    // Se llama cuando el jugador se queda sin tiempo para completar el nivel
    public void onTimeout() {
        JLabel tooSlowLabel = new JLabel("", new ImageIcon("yousuck.jpg"), JLabel.LEFT);
        cp.add(tooSlowLabel);
        cp.remove(board);
        cp.remove(progressPanel);
        pack();
        setVisible(true);

        if (controller.retryCurrentLevel()) {
            showBoard();
        } else {
            JOptionPane.showMessageDialog(this, "Slow player. You need to hurry!");
        }
    }

    //  Renderiza elementos de la pantalla
    // Muestra el tablero del nivel actual, quita la pantalla de bienvenida
    // (o nivel anterior), reinicia el temporizador y renderiza el tablero.

    private void showBoard() {
        if (timer != null) {
            timer.stop();
        }
        cp.remove(splashLabel);
        cp.remove(progressPanel);
        progressPanel.removeAll();

        timer = new CountdownTimer(controller.getMinutesAllowed(), controller.getSecondsAllowed(), this);
        progressPanel.add(timer.getProgressBar());
        cp.add(progressPanel, BorderLayout.NORTH);
        timer.start();

        refreshBoard();
        cp.add(board);
        cp.add(diamondsLabel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        board.requestFocusInWindow();
    }

    //Actualiza el tablero y el contador de diamantes restantes con el estado actual de GameController
    private void refreshBoard() {
        board.render(controller.getMatrix());
        diamondsLabel.setText("Total Diamonds Left to Collect " + controller.getDiamondsLeft());
    }

    //Acumula el tiempo restante del nivel completado y carga el siguiente.
    private void loadNextLevel() {
        controller.advanceToNextLevel(timer.getMinutesLeft(), timer.getSecondsLeft());
        showBoard();
    }
}