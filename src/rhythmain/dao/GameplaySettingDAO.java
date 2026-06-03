/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.Statement;
import rhythmain.model.GameplaySetting;
import rhythmain.utils.DBConnect;

/**
 *
 * @author Fadhil
 */
public class GameplaySettingDAO {
     public GameplaySetting getSetting(int userId) {
        try {
            DBConnect db = new DBConnect();

            String sql =
                "SELECT * " +
                "FROM users " +
                "WHERE user_id = " + userId;
            
            System.out.println(sql);

            Statement stmt =
                db.con.createStatement();

            ResultSet result = stmt.executeQuery(sql);
            result.next();
            
            double scrollSpeed = result.getDouble("scroll_speed");
            String keyBindings = result.getString("key_bindings").toLowerCase();
            GameplaySetting setting = new GameplaySetting(
                scrollSpeed,
                keyBindings.charAt(0),
                keyBindings.charAt(1),
                keyBindings.charAt(2),
                keyBindings.charAt(3)
            );
        
            return setting;
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
