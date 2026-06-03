/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.Statement;
import rhythmain.utils.DBConnect;

/**
 *
 * @author Fadhil
 */
public class BeatmapDAO {
    public String getBeatmap(int songId, String difficulty) {
        try {
            DBConnect db = new DBConnect();

            String sql =
                "SELECT * " +
                "FROM beatmaps " +
                "WHERE song_id=" + songId + " AND difficulty_name=" + difficulty;

            Statement stmt =
                db.con.createStatement();

            ResultSet result = stmt.executeQuery(sql);
            result.next();
            String filePath = result.getString("chart_file_path");
            
            return Files.readString(Path.of(filePath));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
