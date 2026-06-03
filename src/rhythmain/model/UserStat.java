/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.model;

/**
 *
 * @author Fadhil
 */
public class UserStat {
    private int level;
    private int totalXP;
    private double accuracy;
    
    public UserStat(int level, int totalXP, double accuracy) {
        this.level = level;
        this.totalXP = totalXP;
        this.accuracy = accuracy;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getTotalXP() {
        return totalXP;
    }
    
    public double getAccuracy() {
        return accuracy;
    }
}
