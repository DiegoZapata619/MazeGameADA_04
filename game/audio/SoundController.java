package game.audio;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//Implementación de sonido ante ciertas acciones
public class SoundController {

    //Cada clip se relaciona con su nombre, más eficiente
    private final Map<String, Clip> clips = new HashMap<>();

    //synchronized es usado para que solo una ejecución de playSound manipule los clips a la vez
    public synchronized void playSound(String soundName) {

        try {
            Clip clip = clips.get(soundName);
            if (clip == null) {
                //URL que arroja null si no se encontró el archivo, más seguro
                URL resource = getClass().getResource("/sounds/" + soundName + ".wav");
                if (resource == null) {
                    throw new IOException("No se encontró /sounds/" + soundName + ".wav");
                }

                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(resource));

                //Float control permite manipular el sonido en escala. Primero se verifica
                //si el audio soporta la manipulación de volumen
                //Type.MASTER_GAIN maneja la escala en decibeles

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gain.setValue(Math.max(gain.getMinimum(), -12.0f));
                }
                clips.put(soundName, clip);
            }
            //Para evitar que se sature de sonido, se verifica si ya hay un clip corriendo
            //si es así, para el clip de sonido y lo reinicia
            if (clip.isRunning()) {
                if (soundName.equals("hitWall")) {
                    return;
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
