/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.audio;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
    private Clip audioClip;
    private long currentFrame; // Menyimpan posisi audio saat di-pause
    private String filePath;

    // 1. Method untuk memuat file audio ke memori
    public void loadAudio(String filePath) {
        this.filePath = filePath;
        
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.out.println("Audio Error: File tidak ditemukan di " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = (Clip) AudioSystem.getLine(new javax.sound.sampled.Line.Info(Clip.class));
            audioClip.open(audioStream);
        } catch (Exception e) {
            System.out.println("Audio Error: Gagal memuat file! " + e.getMessage());
        }
    }

    // 2. Method untuk memutar musik menggunakan THREAD terpisah (Sesuai T1.4)
    public void play() {
        if (audioClip == null) return;

        new Thread(() -> {
            if (!audioClip.isRunning()) {
                audioClip.setFramePosition(0); // Mulai dari awal (detik 0)
                audioClip.start();
            }
        }).start();
    }

    // 3. Method untuk melanjutkan pemutaran setelah di-pause
    public void resume() {
        if (audioClip == null) return;

        new Thread(() -> {
            if (!audioClip.isRunning()) {
//                audioClip.setFramePosition((int) currentFrame); // Lanjut dari posisi terakhir
                audioClip.start();
            }
        }).start();
    }

    // 4. Method untuk menjeda (Pause) lagu sementara
    public void pause() {
        if (audioClip != null && audioClip.isRunning()) {
            currentFrame = audioClip.getMicrosecondPosition(); // Catat posisi microsecond terakhir
            audioClip.stop();
        }
    }

    // 5. Method untuk menghentikan (Stop) musik secara total
    public void stop() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0); // Reset penunjuk waktu ke awal
            currentFrame = 0;
        }
    }

    // 6. Method tambahan untuk cek status lagu
    public boolean isPlaying() {
        return audioClip != null && audioClip.isRunning();
    }
    
    public int getDuration() {
        return (int) (audioClip.getMicrosecondLength() / 1_000);
    }
}

/**
 *
 * @author gante
 */
