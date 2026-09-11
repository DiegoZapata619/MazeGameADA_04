package game.util;

/*
    Lleva la cuenta del tiempo total que el jugador ha utilizado a lo largo de la partida,
    acumulando el tiempo restante de cada nivel completado.
 */

public class TimeKeeper {

    //Acumula el tiempo restante de un nivel recién completado
    //normalizando los segundos que se pasen de 60
    public void TimeKeeper(int min, int sec) {
        if (sec + seconds <= 60) {
            minutes += min;
            seconds = sec + seconds;
        } else {
            minutes += min;
            minutes += ((sec + seconds) / 60);
            seconds = (sec + seconds) % 60;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    int minutes = 0;
    int seconds = 0;
}
