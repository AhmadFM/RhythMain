/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.dao;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rhythmain.model.Song;
import rhythmain.utils.DBConnect;

/**
 *
 * @author LENOVO
 */
public class SongDAO {
    public List<Song> getAllSongs() {

        List<Song> songs =
                new ArrayList<>();

        try {

            DBConnect db =
                    new DBConnect();

            String sql =
                    "SELECT * FROM songs";

            Statement stmt =
                    db.con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(sql);

            while (rs.next()) {

                Song song =
                        new Song(

                                rs.getInt("song_id"),

                                rs.getString("title"),

                                rs.getString("artist"),

                                rs.getString("creator"),

                                rs.getDouble("bpm"),

                                rs.getString("audio_file_path")
                        );

                songs.add(song);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return songs;
    }
    
}
