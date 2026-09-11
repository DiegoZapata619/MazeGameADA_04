package game.util;

/*
    Calcula el tiempo que se le asigna a un jugador para completar un nivel,
    en función del tamaño del laberinto y la cantidad de diamantes que contiene.
 */

public class TimeCalculator {
    int minutes = 0;
    int seconds = 0;

    //Calcula el tiempo permitido para el nivel actual
    public void calcTimeforMaze(int totalDimonds, int xSize, int ySize) {
        if (xSize / ySize < 1) {
            minutes += (ySize / xSize) + 1;
        } else
            minutes += (ySize / xSize) + 1;
        if (totalDimonds > 6 && totalDimonds * .10 + seconds <= 60)
            minutes += (ySize / xSize) + 1;
        else {
            minutes += 1;
        }
        if (minutes == 0)
            minutes = 2;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}
