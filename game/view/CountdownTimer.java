package game.view;

import javax.swing.Timer;
import javax.swing.JProgressBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Encapsula la lógica del temporizador de cada nivel, maneja los ticks de la cuenta regresiva,
    actualiza la barra de progreso en pantalla y notifica cuando se acaba el tiempo.
 */

public class CountdownTimer {

    //Notifica cuando el tiempo del nivel se agotó
    public interface Listener {
        void onTimeout();
    }

    private final Listener listener;
    private final Timer timer;
    private final JProgressBar progressBar;

    private int minutesLeft;
    private int secondsLeft;
    private int elapsedSeconds;

    public CountdownTimer(int minutesAllowed, int secondsAllowed, Listener listener) {
        this.listener = listener;
        this.minutesLeft = minutesAllowed;
        this.secondsLeft = secondsAllowed;
        this.elapsedSeconds = 0;
        this.progressBar = new JProgressBar(0, minutesAllowed * 100);
        this.progressBar.setStringPainted(true);
        this.timer = new Timer(1000, tickHandler);
    }

    //Se ejecuta cada segundo: descuenta el tiempo restante, actualiza la barra
    //de progreso y notifica al listener cuando llega a cero.
    private final ActionListener tickHandler = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            secondsLeft -= 1;
            elapsedSeconds += 1;
            if (secondsLeft < 0) {
                secondsLeft = 60;
                minutesLeft -= 1;
            }
            progressBar.setValue(elapsedSeconds);
            progressBar.setString(minutesLeft + ":" + secondsLeft);

            if (minutesLeft == 0 && secondsLeft == 0) {
                timer.stop();
                listener.onTimeout();
            }
        }
    };

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}