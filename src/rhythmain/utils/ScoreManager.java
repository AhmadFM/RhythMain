/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.utils;

/**
 *
 * @author Admin
 */
public class ScoreManager {
    // Konstanta Jendela Waktu (Milidetik)
    private static final int PERFECT_WINDOW = 50;
    private static final int GOOD_WINDOW = 100;
    private static final int BAD_WINDOW = 150;

    // Konstanta Bobot Nilai
    private static final int PERFECT_WEIGHT = 300;
    private static final int GOOD_WEIGHT = 100;
    private static final int BAD_WEIGHT = 50;
    private static final int MISS_WEIGHT = 0;

    // Variabel Sesi Permainan
    private int totalScore;
    private int currentCombo;
    private int maxCombo;

    // Variabel untuk Akurasi dan Result Screen
    private int totalNotesPassed;
    private int totalWeightAchieved;
    private int perfectHits;
    private int goodHits;
    private int badHits;
    private int missHits;
    
    // Constructor: Inisialisasi/Reset data saat lagu baru dimulai
    public ScoreManager() {
        resetScore();
    }

    public void resetScore() {
        totalScore = 0;
        currentCombo = 0;
        maxCombo = 0;
        totalNotesPassed = 0;
        totalWeightAchieved = 0;
        perfectHits = 0;
        goodHits = 0;
        badHits = 0;
        missHits = 0;
    }
    
    // timeDifference, Selisih waktu antara input dan target not
    public void registerHit(int timeDifference) {
        int diff = Math.abs(timeDifference);
        totalNotesPassed++;

        if (diff <= PERFECT_WINDOW) {
            processScore(PERFECT_WEIGHT);
            perfectHits++;
        } else if (diff <= GOOD_WINDOW) {
            processScore(GOOD_WEIGHT);
            goodHits++;
        } else if (diff <= BAD_WINDOW) {
            processScore(BAD_WEIGHT);
            badHits++;
        } else {
            // Jika ditekan tapi terlalu jauh dari window, dihitung Miss
            processMiss(false);
        }
    }


    // notePassed, true jika sukses, false jika salah tekan.
    public void processMiss(boolean notePassed) {
        if (notePassed) {
            totalNotesPassed++;
        }
        currentCombo = 0;
        missHits++;
    }

    // Penambahan skor dan combo.
    private void processScore(int baseWeight) {
        currentCombo++;
        if (currentCombo > maxCombo) {
            maxCombo = currentCombo;
        }

        totalWeightAchieved += baseWeight;

        // Kalkulasi skor dengan multiplier combo
        int scoreToAdd = baseWeight + (baseWeight * currentCombo / 25);
        totalScore += scoreToAdd;
    }

    // Mengkalkulasi akurasi saat ini.
    public double getAccuracy() {
        if (totalNotesPassed == 0) {
            return 0.0;
        }
        return ((double) totalWeightAchieved / (totalNotesPassed * PERFECT_WEIGHT)) * 100.0;
    }

    // Menentukan Grade akhir berdasarkan akurasi.
    public String getGrade() {
        double acc = getAccuracy();
        if (acc >= 95.0) {
            return "S";
        }
        if (acc >= 90.0) {
            return "A";
        }
        if (acc >= 80.0) {
            return "B";
        }
        if (acc >= 70.0) {
            return "C";
        }
        return "D";
    }

    // =========================================================================
    // GETTER METHODS 
    // =========================================================================
    public int getTotalScore() {
        return totalScore;
    }

    public int getCurrentCombo() {
        return currentCombo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public int getPerfectHits() {
        return perfectHits;
    }

    public int getGoodHits() {
        return goodHits;
    }

    public int getMissHits() {
        return missHits;
    }
}
