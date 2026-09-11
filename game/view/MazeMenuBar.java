package game.view;

import javax.swing.*;
import java.awt.event.KeyEvent;

/*
    Construye la barra del menú del juego. El menú notifica cuándo se interactuó
    con alguna de sus opciones sin conocer la implementación de esa lógica.
 */

public class MazeMenuBar extends JMenuBar {

    //Notifica cada interacción del jugador con una opción del menú.
    public interface Listener {
        void newGame();

        void openFile();

        void enterName();

        void openHighScore();

        void onSaveScore();

        void onExit();
    }

    //Crea cada elemento del menú y lo conecta, mediante una expresión lambda,
    //con el metodo correspondiente del listener
    public MazeMenuBar(Listener listener) {
        //Items del menú
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> listener.newGame());

        JMenuItem openFileItem = new JMenuItem("Open Maze File.");
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        openFileItem.addActionListener(e -> listener.openFile());

        JMenuItem itemEnterName = new JMenuItem("Enter Player Name");
        itemEnterName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        itemEnterName.addActionListener(e -> listener.enterName());

        JMenuItem itemHighScore = new JMenuItem("High Score");
        itemHighScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        itemHighScore.addActionListener(e -> listener.openHighScore());

        JMenuItem itemSaveScore = new JMenuItem("Save High Score");
        itemSaveScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        itemSaveScore.addActionListener(e -> listener.onSaveScore());

        JMenuItem itemExit = new JMenuItem("Exit");
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        itemExit.addActionListener(e -> listener.onExit());

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(newGameItem);
        fileMenu.add(itemEnterName);
        fileMenu.add(openFileItem);
        fileMenu.add(itemHighScore);
        fileMenu.add(itemSaveScore);
        fileMenu.add(itemExit);
        add(fileMenu);
    }

}