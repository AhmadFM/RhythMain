/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.model;

/**
 *
 * @author Fadhil
 */
public class GameplaySetting {
    private double scrollSpeed;
    private char senar1; 
    private char senar2;
    private char senar3;
    private char senar4;
    
    public GameplaySetting(double scrollSpeed, char senar1, char senar2, char senar3, char senar4) {
        this.scrollSpeed = scrollSpeed;
        this.senar1 = senar1;
        this.senar2 = senar2;
        this.senar3 = senar3;
        this.senar4 = senar4;
    }
    
    public double getScrollSpeed() {
        return scrollSpeed;
    }
    
    public char getSenar1() {
        return senar1;
    }
    
    public char getSenar2() {
        return senar2;
    }
    
    public char getSenar3() {
        return senar3;
    }
    
    public char getSenar4() {
        return senar4;
    }
}
