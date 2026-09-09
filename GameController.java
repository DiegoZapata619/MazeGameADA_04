import javax.swing.JOptionPane;


/*
Clase que contiene todas las reglas del juego: nivel actual, matriz, diamantes, nombre de jugador
y score. Colabora con las clases FileLoader,TimeKeeper y TheArchitect para poder accionar.
No conoce ningún componente de la UI, pero es utilizado por esta cuando se ejecuta una acción
 */
public class GameController
{
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

    //Carga el primer nivel. Esto antes estaba deshabilitado
    public void startNewGame()
    {
        catFileName = 1;
        levelNum = 1;
        loadLevelFile("level1.maz");
    }

    //Habilita la opción de cargar un maze específico
    public void openFile(String fileName)
    {
        loadLevelFile(fileName);
    }

    private void loadLevelFile(String fileName)
    {
        fl.loadFile(fileName);
        theArc.setExit(fl.ExitXCord(), fl.ExitYCord());
        matrix = copyOf(fl.getGameMatrix());
        timeCalc = new TimeCalculator();
        timeCalc.calcTimeforMaze(fl.dimondCount(), fl.getMatrixSizeRow(), fl.getMatrixSizeColumn());
        minutesAllowed = timeCalc.getMinutes();
        secondsAllowed = timeCalc.getSeconds();
    }

    private String[][] copyOf(String[][] source)
    {
        String[][] copy = new String[source.length][];
        for (int i = 0; i < source.length; i++)
        {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    //Reacciona ante los movimientos del jugador. Actualiza la información del tablero
    // y devuelve un valor booleado para indicar si existe un cambio de nivel
    public boolean move(int xScale, int yScale)
    {
        theArc.playerMove(xScale, yScale, matrix, fl.dimondCount());
        matrix = theArc.getUpdatedMatrix();
        return theArc.getLevel();
    }

    //Permite el avance al siguiente nivel una vez alcanzada la salida
    public void advanceToNextLevel(int minutesLeft, int secondsLeft)
    {
        levelNum += 1;
        tk.TimeKeeper(minutesLeft, secondsLeft);
        theArc = new TheArchitect();
        catFileName += 1;
        loadLevelFile("level" + catFileName + ".maz");
    }

    //Método llamado cuando el jugador se queda sin tiempo
    public boolean retryCurrentLevel()
    {
        catFileName -= 1;
        if (catFileName < 1)
        {
            return false;
        }
        loadLevelFile("level" + catFileName + ".maz");
        return true;
    }

    //Habilita el nombre en el menú
    public void promptForPlayerName()
    {
        JOptionPane optionPane = new JOptionPane();
        playerName = optionPane.showInputDialog("Please Enter your Earth Name");
    }

    public void saveScore()
    {
        hs.addHighScore(playerName, tk.getMinutes(), tk.getSeconds(), levelNum);
    }

    public int getDiamondsLeft() { return theArc.getDimondsLeft(); }
    public String[][] getMatrix() { return matrix; }
    public int getMinutesAllowed() { return minutesAllowed; }
    public int getSecondsAllowed() { return secondsAllowed; }
    public int getLevelNum() { return levelNum; }
    public String getPlayerName() { return playerName; }
}