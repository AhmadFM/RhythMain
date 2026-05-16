/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatmapReader {
    
    // Method untuk membaca file TXT baris demi baris (T1.3)
    public static List<String[]> parseBeatmap(String filePath) {
        List<String[]> notesData = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Lewati baris kosong atau komentar jika ada
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                // Pisahkan berdasarkan tanda koma (Waktu, JalurTombol)
                String[] data = line.split(",");
                notesData.add(data);
            }
            System.out.println("Sukses memuat beatmap dengan " + notesData.size() + " notes.");
        } catch (IOException e) {
            System.out.println("Gagal membaca file beatmap: " + e.getMessage());
        }
        
        return notesData;
    }
}
/**
 *
 * @author gante
 */
