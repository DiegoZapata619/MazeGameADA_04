package game.io;

import java.io.*;
import javax.swing.*;

/*
    Encargada de leer un archivo de nivel (.maz) y construir la matriz del laberinto a partir
    de su contenido, determina las dimensiones, traduce cada carácter al tipo de celda
    correspondiente y localiza la posición de la salida.
 */

public class FileLoader {

    //Lee el archivo de nivel indicado, ubicado en resources/mazes,
    //y procesa cada línea para construir la matriz del laberinto
    public void loadFile(String fileName) {
        fileName = "resources/mazes/" + fileName;
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String x;
            int lineNum = 0;
            while ((x = in.readLine()) != null) {
                MatrixLoader(x, lineNum); //Procesa la línea según su número
                lineNum++; //Se usa más adelante para saber si es la primera línea
            }
        }
        catch (IOException e) {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "Ocurrió un error al leer el archivo" + e.getMessage());
        }
    }

    /*
        Procesa una línea del archivo de nivel.
        La primera línea (lineaNum == 0) define el tamaño de la matriz y la inicializa.
        Las líneas siguientes se recorren carácter por carácter para llenar la matriz,
        los puntos se normalizan a "N" y si se encuentra la salida "E" se guarda la posición
        para usarse después.
     */
    public void MatrixLoader(String fileTextLine, int lineNum) throws gameFileError {
        int sum = 0;
        char textVar;

        if (lineNum == 0) {
            //Primera línea: define el tamaño de la matriz (columnas filas)
            for (int i = 0; i < fileTextLine.length(); i++) {
                if (fileTextLine.charAt(i) == ' ')
                    sum += 1; //Cuenta espacios extra entre los dos números de tamaño
            }
            int locationOfSpace = fileTextLine.indexOf(" ");
            String c1 = fileTextLine.substring(0, locationOfSpace);
            String r1 = fileTextLine.substring(locationOfSpace + sum);
            column = Integer.parseInt(c1);
            row = Integer.parseInt(r1);
            GameMatrix = new String[row][column];
        }
        else {
            //Resto de líneas: contenido del laberinto, carácter por carácter
            for (int i = 0; i < fileTextLine.length(); i++) {
                textVar = fileTextLine.charAt(i);
                if (textVar == '.')
                    textVar = 'N'; //Se normaliza el punto a "N" para evitar conflictos con el sistema de archivos

                String textVar1 = "" + textVar;
                if (textVar == 'E') {
                    //Se guarda la posición de salida para revelarla más adelante
                    exitXCord = lineNum - 1;
                    exitYCord = i;
                    textVar1 = "" + textVar;
                }
                GameMatrix[lineNum - 1][i] = textVar1;
            }
        }
    }

    //Devuelve la matriz ya construida, validando primero que el nivel tenga
    //exactamente un jugador y una salida
    public String[][] getGameMatrix() {
        int exitCount = 0;
        int i1 = 0;
        int j1 = 0;
        int playerCount = 0;

        for (int i = 0; i < GameMatrix.length; i++) {
            for (int j = 0; j < GameMatrix[i].length; j++) {
                if (GameMatrix[i][j].equals("P")) {
                    playerCount += 1;
                } else if (GameMatrix[i][j].equals("E")) {
                    exitCount += 1;
                    i1 = i;
                    j1 = j;
                }
            }
        }

        if (playerCount > 1 || exitCount > 1) {
            throw new gameFileError();
        } else {
            GameMatrix[i1][j1] = "W"; //Oculta la salida hasta que se recolecten todos los diamantes
        }
        return GameMatrix;
    }

    public int getMatrixSizeColumn() {
        return column;
    }

    public int getMatrixSizeRow() {
        return row;

    }

    public int ExitXCord() {
        return exitXCord;
    }

    public int ExitYCord(){
        return exitYCord;
    }

    //Retorna la cantidad total de diamantes presentes en el nivel
    public int dimondCount() {
        int totalDimonds = 0;
        for (String[] gameMatrix : GameMatrix) {
            for (String matrix : gameMatrix) {
                if (matrix.equals("D") || matrix.equals("H"))
                    totalDimonds += 1;
            }
        }
        return totalDimonds;
    }

    //Excepción lanzada cuando el archivo de nivel es inválido, contiene más de un jugador o más de una salida
    private class gameFileError extends RuntimeException {
        public gameFileError() {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "El archivo del laberinto tiene más de un jugador o más de una salida.");
        }
    }

    private int exitXCord = 0;
    private int exitYCord = 0;
    private String[][] GameMatrix;
    private int column;
    private int row;

}