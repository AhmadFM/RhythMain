/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.dao;
import java.sql.ResultSet;
import java.sql.Statement;
import rhythmain.utils.DBConnect;
import java.sql.PreparedStatement;

/**
 *
 * @author LENOVO
 */

public class ScoreDAO {

        public ResultSet getTop10Scores(int songId, String difficulty) 
        {

            try {

                DBConnect db =
                    new DBConnect();

                String sql =
                    "SELECT u.username, s.score " +
                    "FROM scores s " +
                    "JOIN users u " +
                    "ON u.user_id = s.user_id " +
                    "JOIN beatmaps b " +
                    "ON b.beatmap_id = s.beatmap_id " +
                    "WHERE b.song_id = ? " +
                    "AND b.difficulty_name = ? " +
                    "ORDER BY s.score DESC " +
                    "LIMIT 10";

                PreparedStatement ps =
                    db.con.prepareStatement(sql);

                ps.setInt(1, songId);

                ps.setString(
                    2,
                    difficulty
                );


                return ps.executeQuery();

            } catch(Exception e) {

                e.printStackTrace();

            }

            return null;
        }
}