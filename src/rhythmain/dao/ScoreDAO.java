/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.dao;
import java.sql.ResultSet;
import java.sql.Statement;
import rhythmain.utils.DBConnect;

/**
 *
 * @author LENOVO
 */
public class ScoreDAO {
    public ResultSet getTop10Scores() {

        try {

            DBConnect db = new DBConnect();

            String sql =
                "SELECT u.username, MAX(s.score) AS best_score " +
                "FROM scores s " +
                "JOIN users u ON s.user_id = u.user_id " +
                "GROUP BY u.user_id " +
                "ORDER BY best_score DESC " +
                "LIMIT 10";

            Statement stmt =
                db.con.createStatement();

            return stmt.executeQuery(sql);

        } catch(Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}
