/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import rhythmain.model.UserStat;
import rhythmain.utils.DBConnect;

/**
 *
 * @author Fadhil
 */
public class UserStatDAO {
     public UserStat getUserStat(int userId) {
        try {
            DBConnect db = new DBConnect();

            String sql =
                "SELECT * " +
                "FROM user_stats " +
                "WHERE user_id = " + userId;

            Statement stmt =
                db.con.createStatement();

            ResultSet result = stmt.executeQuery(sql);
            // Apabila tidak ada, return null.
            if (!result.next()) {
                return null;
            }
            
            UserStat userStat = new UserStat(
                result.getInt("level"),
                result.getInt("total_xp"),
                result.getDouble("accuracy")
            );
            return userStat;
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
