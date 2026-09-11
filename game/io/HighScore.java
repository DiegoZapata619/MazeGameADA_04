package game.io;

import java.io.*;

/*
    Encargada de registrar los puntajes de los jugadores en un archivo de texto.
 */

public class HighScore {

    //Agrega una nueva entrada de puntaje al archivo scores.txt
    public void addHighScore(String name, int min, int sec, int level) {
        try {
            String outData = "PlayerName: " + name + " Total Time for Levels: " + min + ":" + sec + "(Minutes:Seconds)" + "Level Reached:*" + level;
            PrintWriter out = new PrintWriter(new FileOutputStream("scores.txt", true));
            out.println("");
            out.println(outData);
            out.close();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

