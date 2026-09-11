package game.audio;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
    Controla la reproducción de efectos de sonido del juego.
    Reemplaza los antiguos diálogos de advertencia, en lugar de interrumpir al jugador
    se reproduce un sonido correspondiente a la acción realizada (chocar con muro, recoger diamante, etc.)
 */

public class SoundController {

    //Cada clip se relaciona con su nombre, más eficiente
    private final Map<String, Clip> clips = new HashMap<>();

    //Synchronized es usado para que solo una ejecución de playSound manipule los clips a la vez
    public synchronized void playSound(String soundName) {

        try {
            Clip clip = clips.get(soundName);
            if (clip == null) {
                //getResource regresa null si no encuentra el archivo, evita una excepción directa
                URL resource = getClass().getResource("/sounds/" + soundName + ".wav");
                if (resource == null) {
                    throw new IOException("No se encontró /sounds/" + soundName + ".wav");
                }

                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(resource));

                //Float control permite manipular el sonido en escala. Primero se verifica si el audio soporta la manipulación de volumen
                //Type.MASTER_GAIN maneja la escala en decibeles
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gain.setValue(Math.max(gain.getMinimum(), -12.0f));
                }
                clips.put(soundName, clip);
            }

            //Si el clip ya está sonando, se detiene y se reinicia para no saturar el audio
            if (clip.isRunning()) {
                if (soundName.equals("hitWall")) {
                    return; //Se ignoran solicitudes repetidas de "golpe contra muro"
                }
                clip.stop();
                clip.flush();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            JOptionPane.showMessageDialog(null, "Error en audio:\n" + ex.getMessage());
        }
    }
}
