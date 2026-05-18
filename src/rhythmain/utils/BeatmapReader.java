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
import com.google.gson.Gson;

public class BeatmapReader {
    public Note[] bacaBeatMap (String baca) {
        Gson gson1 = new Gson ();
        
        Note[] daftarNote =  gson1.fromJson(baca, Note[].class);
        return daftarNote;
    }
    // Method untuk membaca file TXT baris demi baris (T1.3)
}
/**
 *
 * @author gante
 */
