import javax.swing.*;
import java.awt.*;


/*
    Refactorización grande:
    -GameGui: Solo coordina los eventos que ocurren, actualiza las vistas y renderiza los componentes visuales
    -GameController: Contiene las reglas del juego y es utilizado por la gui para accionar
    -SoundController: Sonidos dinámicos que dependen de los movimientos del jugador
    -MazeBoardPanel: Se elimina la lógica de creación de matrices de la gui y se crea una clase aparte encargada.
    Ya no se reinicia el tablero con cada movimiento, ahora se evalúa si el nivel cambio y, en respuesta,
    se crea el panel o se actualiza dependiendo del movimiento.
    -MazeMenuBar: Despliega los componentes de la vista del menú principal.
    -CountdownTimer: Encapsula la lógica del menú de progreso en cada nivel.
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

        splashLabel = new JLabel("", new ImageIcon("images/yeababyyea.jpg"), JLabel.LEFT);
        cp.add(splashLabel);

        board = new MazeBoardPanel(this);

        setJMenuBar(new MazeMenuBar(this));

        pack();
        setVisible(true);
    }

    // Implementaciones de MazeMenuBarListener
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

    // Implementación de moveListener de MazeBoardPanel. Captura los movimientos del jugador
    //


    public void onMove(int xScale, int yScale) {
        boolean levelComplete = controller.move(xScale, yScale);
        refreshBoard();
        if (levelComplete) {
            loadNextLevel();
        }
    }


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

    private void refreshBoard() {
        board.render(controller.getMatrix());
        diamondsLabel.setText("Total Diamonds Left to Collect " + controller.getDiamondsLeft());
    }

    private void loadNextLevel() {
        controller.advanceToNextLevel(timer.getMinutesLeft(), timer.getSecondsLeft());
        showBoard();
    }
}