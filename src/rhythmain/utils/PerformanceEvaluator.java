/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.utils;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class PerformanceEvaluator {
    public List<String> generatePerformanceLabels(int totalHits, int perfectCount, int maxCombo, int expectedMaxCombo, String grade) {
        List<String> labels = new ArrayList<>();

        // PERFECT
        if (perfectCount == expectedMaxCombo) {
            labels.add("PERFECT");
        }

        // FULL COMBO
        if (maxCombo == expectedMaxCombo && expectedMaxCombo > 0) {
            labels.add("FULL COMBO");
        }

        // GRADE Label
        if ("S".equalsIgnoreCase(grade)) {
            labels.add("AWESOME");
        } else if ("A".equalsIgnoreCase(grade)) {
            labels.add("GREAT");
        } else if ("B".equalsIgnoreCase(grade)){
            labels.add("OK");
        }

        // Default
        if (labels.isEmpty()) {
            labels.add("CLEAR");
        }

        return labels;
    }
}
