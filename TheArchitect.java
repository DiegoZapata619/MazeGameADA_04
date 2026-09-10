import javax.swing.*;
//Your life is the sum of a remainder of an unbalanced equation inherent to the programming
//of the matrix

public class TheArchitect extends JFrame {
    SoundController snc = new SoundController();
    int foundPlayer = 0;
    String[][] updatedMatrix;
    int WallXCord;
    int WallYCord;
    int collected = 0;
    boolean level;
    int globalTotalDimonds = 0;

    public void setExit(int x, int y)//records the location of the exit so we can show it when its time
    {
        WallXCord = x;
        WallYCord = y;
    }

    public void showWall()//used when its time to show the exit.
    {
        updatedMatrix[WallXCord][WallYCord] = "E";
    }

    public void playerMove(int xScale, int yScale, String[][] currentMatrix, int totalDimonds) {
        int x = 0;
        int y = 0;
        int found = 0;
        globalTotalDimonds = totalDimonds; //use this later for the gui dimond count
        nextLevel(false); //dont go to the next level yet.
        String[][] junkMatrix = currentMatrix;//we will be updating currentMatrix
        for (int i = 0; i < currentMatrix.length; i++) //for loop will find were the player is now
        {
            for (int j = 0; j < currentMatrix[i].length; j++) {
                if (currentMatrix[i][j].equals("P"))//we found the player
                {
                    x = i;//record the players position
                    y = j;
                    found = 1;
                    break;
                }
            }
        }//end both for loops
        //Se recoge un diamante
        if (currentMatrix[x + xScale][y + yScale].equals("H")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            currentMatrix[x][y] = "N";
            collected += 1;
            snc.playSound("diamond!");
        }
        //Se recoge un diamante oculto. Mismo comportamiento
        else if (currentMatrix[x + xScale][y + yScale].equals("D"))//its a dimond
        {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            collected += 1;
            snc.playSound("diamond!");
        } else if (currentMatrix[x + xScale][y + yScale].equals("M") && currentMatrix[x + (xScale * 2)][y + (yScale * 2)].equals("N")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            currentMatrix[x + (xScale * 2)][y + (yScale * 2)] = "M";
            snc.playSound("moveWall");
        }
        //Avance normal a un bloque vacío
        else if (currentMatrix[x + xScale][y + yScale].equals("N")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
        } else if (currentMatrix[x + xScale][y + yScale].equals("E")) {
            currentMatrix[x][y] = "N";
            currentMatrix[x + xScale][y + yScale] = "P";
            nextLevel(true);//allow the next level to be loaded.
            snc.playSound("victory!");
        } else {
            snc.playSound("hitWall");
        }

        if (collected == totalDimonds) {
            showWall();
        }

        updatedMatrix = currentMatrix;  //we will return updatedMatrix for the gui
    }//end method

    public void nextLevel(boolean tOrF)//true we go to next level, false we update current level's gui 
    {
        level = tOrF;
    }

    public boolean getLevel()//returs level true or false
    {
        return level;
    }

    public int getDimondsLeft() {
        return globalTotalDimonds - collected;//for GUI JLabel, show how many dimonds are left to be collected
    }

    public String[][] getUpdatedMatrix()//returns the updated matrix for the gui to display
    {
        return updatedMatrix;
    }

}//end class
