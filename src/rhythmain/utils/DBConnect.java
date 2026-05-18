/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.utils;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 *  Cara Pakai?
 *  
 *  1. Gunakan header yang sesuai
 *     import java.sql.ResultSet;   // untuk mengambil hasil query
 *     import java.sql.Statement;   // untuk membuat query untuk operasi crud
 *     import rhythmain.utils.DBConnect;
 * 
 *  2. Buat objeknya
 *     contoh: DBConnect koneksi = new DBConnect();
 * 
 *  3. Eksplorasi sesuai kebutuhan
 * 
 * @author Admin
 */
public class DBConnect {
    public Connection con;
    
    public DBConnect() {
        String id, pass, driver, url;
        id = "root";
        pass = ""; // edit kalau berbeda
        driver = "com.mysql.cj.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/db_pbo_rythm?userTimezone=true&server=UTC";
        
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, id, pass);
            if (con == null) {
                System.out.println("Koneksi Gagal");
            } else {
                System.out.println("Koneksi Berhasil");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
//    public static void main(String[] args){
//        // Test Koneksi
//        DBConnect koneksi = new DBConnect();
//    }
}
