import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

//Implementación de sonido ante ciertas acciones
public class SoundController {
    private Clip clip;

    public void playSound(String ruta) {
        try {
            clip = AudioSystem.getClip();
            ruta = "sounds/" + ruta + ".wav";
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(ruta)));
            //pequeña validación, si se vuelve a activar el evento del sonido, se reinicia
            if (clip==null){
                return;
            }
            if (clip.isRunning()){
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            JOptionPane.showMessageDialog(null, "Error en audio:\n" + ex.getMessage());
        }
    }

}
