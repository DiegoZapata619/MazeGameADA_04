package game.util;

public class TimeKeeper {
    public void TimeKeeper(int min, int sec)//a class to keep track of the total seconds and minuntes the player has used to get to a level
    {
        if (sec + seconds <= 60) {
            minutes += min;
            seconds = sec + seconds;
        } else {
            minutes += min;
            minutes += ((sec + seconds) / 60);
            seconds = (sec + seconds) % 60;
        }
    }//end game.util.TimeKeeper

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    int minutes = 0;
    int seconds = 0;
}//end class
