package game.controller;

import game.audio.SoundController;

import javax.swing.*;

/*
    Contiene la lógica de movimiento del jugador dentro del laberinto:
    detecta contra qué tipo de celda se mueve (pared, diamante, muro movible, salida)
    y actualiza la matriz, notificando el efecto de sonido correspondiente mediante SoundController.
 */

public class TheArchitect extends JFrame {
    SoundController snc = new SoundController();
    int foundPlayer = 0;
    String[][] updatedMatrix;
    int WallXCord;
    int WallYCord;
    int collected = 0;
    boolean level;
    int globalTotalDimonds = 0;

    //Guarda la posición de la salida para poder reverlarla más adelante.
    public void setExit(int x, int y) {
        WallXCord = x;
        WallYCord = y;
    }

    //Reve la salida en la matriz, marcándola con "E" en su posición guardada.
    //Se invoca una vez que el jugador recolectó todos los diamantes.
    public void showWall() {
        updatedMatrix[WallXCord][WallYCord] = "E";
    }

    //Procesa el intento de movimiento del jugador: localiza su posición actual en la matriz,
    //y según su celda de destino decide si se mueve, recoge un diamante, empuja un muro movible,
    //llega a la salida o choca contra un muro.
    public void playerMove(int xScale, int yScale, String[][] currentMatrix, int totalDimonds) {
        int x = 0;
        int y = 0;
        int found = 0;
        globalTotalDimonds = totalDimonds; //Se usa después para mostrar diamantes restantes en la GUI
        nextLevel(false); //todavía no se avanza de nivel

        String[][] junkMatrix = currentMatrix;
        //Localiza la posición actual del jugador en la matriz
        for (int i = 0; i < currentMatrix.length; i++) {
            for (int j = 0; j < currentMatrix[i].length; j++) {
                if (currentMatrix[i][j].equals("P")) {
                    x = i;
                    y = j;
                    found = 1;
                    break;
                }
            }
        }

        //Diamante oculto
        if (currentMatrix[x + xScale][y + yScale].equals("H")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            currentMatrix[x][y] = "N";
            collected += 1;
            snc.playSound("diamond!");
        }
        //Diamante visible
        else if (currentMatrix[x + xScale][y + yScale].equals("D")){
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            collected += 1;
            snc.playSound("diamond!");
        }
        //Empuja un muro movible hacia una celda libre
        else if (currentMatrix[x + xScale][y + yScale].equals("M") && currentMatrix[x + (xScale * 2)][y + (yScale * 2)].equals("N")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            currentMatrix[x + (xScale * 2)][y + (yScale * 2)] = "M";
            snc.playSound("moveWall");
        }
        //Avance normal a una celda vacía
        else if (currentMatrix[x + xScale][y + yScale].equals("N")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
        }
        //Llega a la salida y se habilita el avance de nivel
        else if (currentMatrix[x + xScale][y + yScale].equals("E")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            nextLevel(true);
            snc.playSound("victory!");
        }
        //Choca contra un muro
        else {
            snc.playSound("hitWall");
        }

        if (collected == totalDimonds) {
            showWall();
        }

        updatedMatrix = currentMatrix;  //Se regresa a la GUI
    }

    //Marca si se debe avanzar al siguiente nivel o solo actualizar el nivel actual
    public void nextLevel(boolean tOrF) {
        level = tOrF;
    }

    //Devuelve true si el jugador alcanzó la salida y debe avanzarse de nivel
    public boolean getLevel() {
        return level;
    }

    //Cantidad de diamantes que aún faltan por recolectar, para mostrarse en la GUI
    public int getDimondsLeft() {
        return globalTotalDimonds - collected;
    }

    //Retorna la matriz actualizada del tablero, para que la GUI la muestre
    public String[][] getUpdatedMatrix() {
        return updatedMatrix;
    }

}//end class
