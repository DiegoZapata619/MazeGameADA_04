package game.controller;

import game.io.FileLoader;
import game.io.HighScore;
import game.util.TimeCalculator;
import game.util.TimeKeeper;

import javax.swing.JOptionPane;

/*
    Contiene todas las reglas del juego: nivel actual, matriz del laberinto, diamantes recolectados,
    nombre del jugador y puntaje.
    Colabora con las clases game.io.FileLoader,game.util.TimeKeeper y game.controller.TheArchitect para poder accionar.
    No conoce ningún componente de la UI, pero es utilizado por esta cuando se ejecuta una acción.
 */

public class GameController {
    private final FileLoader fl = new FileLoader();
    private final HighScore hs = new HighScore();
    private final TimeKeeper tk = new TimeKeeper();
    private TimeCalculator timeCalc;
    private TheArchitect theArc = new TheArchitect();
    private String[][] matrix;
    private int catFileName = 1;
    private int levelNum = 1;
    private String playerName;

    private int minutesAllowed;
    private int secondsAllowed;

    //Reinicia el estado del juego y carga el primer nivel. Esto antes estaba deshabilitado.
    public void startNewGame() {
        catFileName = 1;
        levelNum = 1;
        loadLevelFile("level1.maz");
    }

    //Carga un archivo de laberinto específico, elegido por el jugador.
    public void openFile(String fileName) {
        loadLevelFile(fileName);
    }

    //Carga el archivo del nivel indicado: crea un nuevo TheArchitect,
    //obtiene la matriz y calcula el tiempo permitido para ese nivel.
    private void loadLevelFile(String fileName) {
        theArc = new TheArchitect();
        fl.loadFile(fileName);
        theArc.setExit(fl.ExitXCord(), fl.ExitYCord());
        matrix = copyOf(fl.getGameMatrix());
        timeCalc = new TimeCalculator();
        timeCalc.calcTimeforMaze(fl.dimondCount(), fl.getMatrixSizeRow(), fl.getMatrixSizeColumn());
        minutesAllowed = timeCalc.getMinutes();
        secondsAllowed = timeCalc.getSeconds();
    }

    //Crea una copia de la matriz para no compartir referencia con la matriz orginal de FileLoader.
    private String[][] copyOf(String[][] source) {
        String[][] copy = new String[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    //Reacciona ante los movimientos del jugador y actualiza la matriz del tablero.
    public boolean move(int xScale, int yScale) {
        theArc.playerMove(xScale, yScale, matrix, fl.dimondCount());
        matrix = theArc.getUpdatedMatrix();
        return theArc.getLevel();
    }

    //Avanza al siguiente nivel una vez que el jugador alcanzó la salida,
    //acumulando el tiempo usado en el nivel anterior.
    public void advanceToNextLevel(int minutesLeft, int secondsLeft) {
        levelNum += 1;
        tk.TimeKeeper(minutesLeft, secondsLeft);
        catFileName += 1;
        loadLevelFile("level" + catFileName + ".maz");
    }

    //Se llama cuando el jugador se queda sin tiempo; recarga el nivel actual.
    public boolean retryCurrentLevel() {
        catFileName -= 1;
        if (catFileName < 1) {
            return false;
        }
        loadLevelFile("level" + catFileName + ".maz");
        return true;
    }

    //Solicita al jugador su nombre mediante diálogo, para usarlo en el high score.
    public void promptForPlayerName() {
        JOptionPane optionPane = new JOptionPane();
        playerName = JOptionPane.showInputDialog("Ingresa tu nombre de jugador");
    }

    //Guarda el puntaje actual del jugador en el archivo de high score.
    public void saveScore() {
        hs.addHighScore(playerName, tk.getMinutes(), tk.getSeconds(), levelNum);
    }

    public int getDiamondsLeft() {
        return theArc.getDimondsLeft();
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public int getMinutesAllowed() {
        return minutesAllowed;
    }

    public int getSecondsAllowed() {
        return secondsAllowed;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public String getPlayerName() {
        return playerName;
    }
}