/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.audio;

import java.io.File;
import javax.sound.sampled.*;

public class AudioPlayer {
    private Clip audioClip;
    private Thread audioThread;

    public void loadAudio(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.out.println("File audio tidak ditemukan di: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            audioClip.open(audioStream);
        } catch (Exception e) {
            System.out.println("Error memuat audio: " + e.getMessage());
        }
    }

    // Memutar musik menggunakan Thread agar asinkronus (T1.4)
    public void play() {
        if (audioClip == null) return;

        audioThread = new Thread(() -> {
            audioClip.setFramePosition(0); // Mulai dari awal detik ke-0
            audioClip.start();
        });
        audioThread.start();
    }

    public void stop() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

    public void pause() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

    public boolean isPlaying() {
        return audioClip != null && audioClip.isRunning();
    }
}
/**
 *
 * @author gante
 */
