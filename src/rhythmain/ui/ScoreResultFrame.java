/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rhythmain.ui;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JLabel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import rhythmain.utils.DBConnect;
import rhythmain.utils.ScoreManager;
import rhythmain.utils.UserSession;
import rhythmain.utils.PerformanceEvaluator;
/**
 *
 * @author Admin
 */
public class ScoreResultFrame extends javax.swing.JFrame {
    char grade;
    int idBeatmap;
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
    String formatDateTime = now.format(formatter);
    
    // User Stat
    private int stats_id    = 0;
    private int level       = 0;
    private int totalXP     = 0;
    private double accuracy = 0;
    private int playCount   = 0;
    private int totalScore  = 0;
    private int sCount      = 0;
    private int aCount      = 0;
    private int totalHits   = 0;
    private int maxCombo    = 0;
    private int fcCount     = 0;
    private int perfectCount= 0;
    
    //defautlt test
    String judulLagu = "Hatsune Miku no Shoushitsu";
    String artist = "Hatsune Miku no Shoushitsu";
    String creator = "Hatsune Miku no Shoushitsu";
    
    
    ScoreManager skor = new ScoreManager();
    DBConnect koneksi = new DBConnect();
    
    /**
     * Creates new form ScoreResultFrame
     */   
    public ScoreResultFrame(int idBeatmap) {
        initComponents();
        
        ShowBeatmapInfo(idBeatmap);
        ShowGrade();
        ShowResultScore();
        ShowPerformance();
        
        
        int uid = UserSession.userId;   
        GetUserStats(uid);
        StoreScore(uid, idBeatmap);
        UpdateUserStats(uid);
        ShowLabel();
    }
    
    public void  ShowBeatmapInfo(int idBeatmap){
        String stmt = "SELECT s.title, s.artist, s.creator, s.bpm, s.audio_file_path FROM beatmaps b INNER JOIN songs s ON b.song_id = s.song_id WHERE b.beatmap_id = ?";
        try{
            PreparedStatement pStmt = koneksi.con.prepareStatement(stmt);
            pStmt.setInt(1, idBeatmap);
            
            ResultSet data = pStmt.executeQuery();
            
            if (data.next()) {
                judulLagu = data.getString("title");
                artist = data.getString("artist");
                creator = data.getString("creator");
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query execution error!"+ e);
        }
        jLabel_namaBeatmap.setText(judulLagu+" - "+artist);
        jLabel_creatorBeatmap.setText(creator);
        jLabel_clearedTimestamp.setText(formatDateTime);
    }
    
    public void ShowResultScore(){
        jPerfectCounter.setText(String.valueOf(skor.getPerfectHits()));
        jGoodCounter.setText(String.valueOf(skor.getGoodHits()));
        jBadCounter.setText(String.valueOf(skor.getBadHits()));
        jMissCounter.setText(String.valueOf(skor.getMissHits()));
        jComboCounter.setText(String.valueOf(skor.getMaxCombo()));
        jAccuracy.setText(String.format("%.2f%%", skor.getAccuracy()));
    }
    
    public String ShowGrade(){
        if (skor.getAccuracy() >= 95.00){
            jLabel_grade.setText("S");
            return "S";
        } else if (skor.getAccuracy() >= 90.00){
            jLabel_grade.setText("A");
            return "A";
        } else if (skor.getAccuracy() >= 80.00){
            jLabel_grade.setText("B");
            return "B";
        } else if (skor.getAccuracy() >= 70.00){
            jLabel_grade.setText("C");
            return "C";
        } else if (skor.getAccuracy() >= 60.00){
            jLabel_grade.setText("D");
            return "D";
        } else{
            jLabel_grade.setText("F");
            return "F";
        }
    }
    
    public void ShowPerformance(){
        jLabel_totalScore.setText(String.valueOf(skor.getTotalScore()));
    }
    
    public void StoreScore(int uid, int idBeatmap){
        String stmt = "INSERT INTO `scores`(`user_id`, `beatmap_id`, `score`, `max_combo`, `accuracy`, `grade`) VALUES (?,?,?,?,?,?)";
        
        try{
            PreparedStatement pStmt = koneksi.con.prepareStatement(stmt);
            
            pStmt.setInt(1, uid);
            pStmt.setInt(2, idBeatmap);
            pStmt.setInt(3, skor.getTotalScore());
            pStmt.setInt(4, skor.getMaxCombo());
            pStmt.setDouble(5, skor.getAccuracy());
            pStmt.setString(6, ShowGrade());

            int rowsInserted = pStmt.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query execution error!"+ e);
        }
    }
    
    private void GetUserStats(int uid){ //just for test
        String stmt = "SELECT * FROM user_stats WHERE user_id=?";
        
        try{
            PreparedStatement pStmt = koneksi.con.prepareStatement(stmt);
            pStmt.setInt(1, uid);
            
            ResultSet data = pStmt.executeQuery();
            if (data.next()) {
                stats_id    = data.getInt("stats_id");
                level       = data.getInt("level");
                totalXP     = data.getInt("total_xp");
                accuracy    = data.getDouble("accuracy");
                playCount   = data.getInt("play_count");
                totalScore  = data.getInt("total_score");
                sCount      = data.getInt("s_grade_count");
                aCount      = data.getInt("a_grade_count");
                totalHits   = data.getInt("total_hits");
                maxCombo    = data.getInt("max_combo");
                fcCount    = data.getInt("full_combo_count");
                perfectCount= data.getInt("perfect_count");
            }
        } catch (SQLException e) {
            System.err.println("Database connection or query execution error!"+ e);
        }
    }
    
    private void UpdateUserStats(int uid){
        if (skor.getAccuracy()==100){
            perfectCount++;
            sCount++;
            fcCount++;
            totalXP+= 50;
        } else if (skor.getAccuracy()>90){
            sCount++;
            totalXP+= 30;
        } else if (skor.getAccuracy()>80){
            aCount++;
            totalXP+= 20;
        } else{
            totalXP+= 5;
        }
        
        level += (totalXP/100 >= 0) ? totalXP/100 : 0;
        totalXP = totalXP%100;
        accuracy = ((accuracy==0)? (double) skor.getAccuracy(): (double) (accuracy + skor.getAccuracy())/2);
        totalScore += skor.getTotalScore();
        if(skor.getMaxCombo() > maxCombo){
            maxCombo = skor.getMaxCombo();
        }
        if(skor.isItFullCombo()){
                fcCount++;
        }
        totalHits += skor.getAllHits();
        
        String stmt = "UPDATE `user_stats` SET `level`=?,`total_xp`=?,`accuracy`=?,`play_count`=?,`total_score`=?,`s_grade_count`=?,`a_grade_count`=?,`total_hits`=?,`max_combo`=?,`full_combo_count`=?,`perfect_count`=? WHERE `user_id`=?";
        try{
            PreparedStatement pStmt = koneksi.con.prepareStatement(stmt);
            pStmt.setInt(1, level); // level done
            pStmt.setInt(2, totalXP); // totalxp done
            pStmt.setDouble(3, accuracy); // accuracy done
            pStmt.setInt(4, playCount++); // playcount done
            pStmt.setInt(5, totalScore); // totalscore done
            pStmt.setInt(6, sCount); // sgrade done
            pStmt.setInt(7, aCount); // agrade done
            pStmt.setInt(8, totalHits); // total hit done
            pStmt.setInt(9, maxCombo); // maxcombo done
            pStmt.setInt(10, fcCount); // fullcombo done
            pStmt.setInt(11, perfectCount); // perfect count done
            pStmt.setInt(12, uid); // uid done

            int rowsInserted = pStmt.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query execution error!"+ e);
        }
    }
    
    private void ShowLabel(){
        jPanel_labelBox.removeAll(); 
        PerformanceEvaluator evaluator = new PerformanceEvaluator();
        List<String> earnedLabels = evaluator.generatePerformanceLabels(totalHits, perfectCount, maxCombo, skor.getTotalNotesPassed(), ShowGrade());

        for (String labelText : earnedLabels) {
            JLabel uiLabel = new JLabel(" " + labelText + " ");
            uiLabel.setOpaque(true);
            uiLabel.setBackground(Color.DARK_GRAY);
            uiLabel.setForeground(Color.WHITE);

            jPanel_labelBox.add(uiLabel);
        }

        jPanel_labelBox.revalidate();
        jPanel_labelBox.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_Header = new javax.swing.JPanel();
        jLabel_namaBeatmap = new javax.swing.JLabel();
        jLabel_creatorBeatmap = new javax.swing.JLabel();
        jLabel_clearedTimestamp = new javax.swing.JLabel();
        jPanel_Stats = new javax.swing.JPanel();
        jLabel_perfectCount = new javax.swing.JLabel();
        jLabel_goodCount = new javax.swing.JLabel();
        jLabel_badCount = new javax.swing.JLabel();
        jLabel_missCount = new javax.swing.JLabel();
        jLabel_comboCount = new javax.swing.JLabel();
        jLabel_accuracy = new javax.swing.JLabel();
        jPerfectCounter = new javax.swing.JLabel();
        jGoodCounter = new javax.swing.JLabel();
        jBadCounter = new javax.swing.JLabel();
        jMissCounter = new javax.swing.JLabel();
        jComboCounter = new javax.swing.JLabel();
        jAccuracy = new javax.swing.JLabel();
        jButton_Return = new javax.swing.JButton();
        jPanel_Performances = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel_totalScore = new javax.swing.JLabel();
        jPanel_labelBox = new javax.swing.JPanel();
        jPanel_Grades = new javax.swing.JPanel();
        jLabel_grade = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RhythMain - Score Result");
        setBackground(new java.awt.Color(0, 153, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanel_Header.setBackground(new java.awt.Color(20, 20, 30));

        jLabel_namaBeatmap.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel_namaBeatmap.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_namaBeatmap.setLabelFor(jLabel_namaBeatmap);
        jLabel_namaBeatmap.setText("Beatmap Title - Artist");

        jLabel_creatorBeatmap.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_creatorBeatmap.setLabelFor(jLabel_creatorBeatmap);
        jLabel_creatorBeatmap.setText("Creator");

        jLabel_clearedTimestamp.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_clearedTimestamp.setText("clearance timestamp");

        javax.swing.GroupLayout jPanel_HeaderLayout = new javax.swing.GroupLayout(jPanel_Header);
        jPanel_Header.setLayout(jPanel_HeaderLayout);
        jPanel_HeaderLayout.setHorizontalGroup(
            jPanel_HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_HeaderLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel_HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_clearedTimestamp)
                    .addComponent(jLabel_creatorBeatmap)
                    .addComponent(jLabel_namaBeatmap))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_HeaderLayout.setVerticalGroup(
            jPanel_HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_HeaderLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel_namaBeatmap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_creatorBeatmap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_clearedTimestamp)
                .addGap(15, 15, 15))
        );

        jPanel_Stats.setBackground(new java.awt.Color(0, 153, 255));
        jPanel_Stats.setMaximumSize(new java.awt.Dimension(400, 600));
        jPanel_Stats.setMinimumSize(new java.awt.Dimension(150, 500));
        jPanel_Stats.setPreferredSize(new java.awt.Dimension(350, 425));

        jLabel_perfectCount.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel_perfectCount.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_perfectCount.setText("Perfect");

        jLabel_goodCount.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel_goodCount.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_goodCount.setText("Good");

        jLabel_badCount.setFont(jLabel_goodCount.getFont());
        jLabel_badCount.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_badCount.setText("Bad");

        jLabel_missCount.setFont(jLabel_goodCount.getFont());
        jLabel_missCount.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_missCount.setText("Miss");

        jLabel_comboCount.setFont(jLabel_goodCount.getFont());
        jLabel_comboCount.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_comboCount.setText("Max Combo");

        jLabel_accuracy.setFont(jLabel_goodCount.getFont());
        jLabel_accuracy.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_accuracy.setText("Accuracy");

        jPerfectCounter.setFont(jLabel_goodCount.getFont());
        jPerfectCounter.setForeground(new java.awt.Color(255, 255, 255));
        jPerfectCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPerfectCounter.setText("0");
        jPerfectCounter.setToolTipText("");

        jGoodCounter.setFont(jLabel_comboCount.getFont());
        jGoodCounter.setForeground(new java.awt.Color(255, 255, 255));
        jGoodCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jGoodCounter.setText("0");
        jGoodCounter.setToolTipText("");

        jBadCounter.setFont(jLabel_comboCount.getFont());
        jBadCounter.setForeground(new java.awt.Color(255, 255, 255));
        jBadCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jBadCounter.setText("0");
        jBadCounter.setToolTipText("");

        jMissCounter.setFont(jLabel_comboCount.getFont());
        jMissCounter.setForeground(new java.awt.Color(255, 255, 255));
        jMissCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jMissCounter.setText("0");
        jMissCounter.setToolTipText("");

        jComboCounter.setFont(jLabel_comboCount.getFont());
        jComboCounter.setForeground(new java.awt.Color(255, 255, 255));
        jComboCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jComboCounter.setText("0");
        jComboCounter.setToolTipText("");

        jAccuracy.setFont(jLabel_comboCount.getFont());
        jAccuracy.setForeground(new java.awt.Color(255, 255, 255));
        jAccuracy.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jAccuracy.setText("0");
        jAccuracy.setToolTipText("");

        javax.swing.GroupLayout jPanel_StatsLayout = new javax.swing.GroupLayout(jPanel_Stats);
        jPanel_Stats.setLayout(jPanel_StatsLayout);
        jPanel_StatsLayout.setHorizontalGroup(
            jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_StatsLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_perfectCount)
                    .addComponent(jLabel_goodCount)
                    .addComponent(jLabel_badCount)
                    .addComponent(jLabel_missCount)
                    .addComponent(jLabel_comboCount)
                    .addComponent(jLabel_accuracy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jAccuracy, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jComboCounter, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jMissCounter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBadCounter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jGoodCounter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPerfectCounter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );
        jPanel_StatsLayout.setVerticalGroup(
            jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_StatsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_perfectCount)
                    .addComponent(jPerfectCounter))
                .addGap(38, 38, 38)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_goodCount)
                    .addComponent(jGoodCounter))
                .addGap(38, 38, 38)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_badCount)
                    .addComponent(jBadCounter))
                .addGap(38, 38, 38)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_missCount)
                    .addComponent(jMissCounter))
                .addGap(38, 38, 38)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_comboCount)
                    .addComponent(jComboCounter))
                .addGap(38, 38, 38)
                .addGroup(jPanel_StatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_accuracy)
                    .addComponent(jAccuracy))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton_Return.setBackground(new java.awt.Color(0, 153, 255));
        jButton_Return.setFont(jLabel_goodCount.getFont());
        jButton_Return.setForeground(new java.awt.Color(255, 255, 255));
        jButton_Return.setText("Continue");
        jButton_Return.setBorder(null);
        jButton_Return.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_ReturnMouseClicked(evt);
            }
        });

        jPanel_Performances.setBackground(new java.awt.Color(0, 153, 255));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Performance:");

        jLabel_totalScore.setFont(jLabel_goodCount.getFont());
        jLabel_totalScore.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_totalScore.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel_totalScore.setText("000000000");

        jPanel_labelBox.setOpaque(false);

        javax.swing.GroupLayout jPanel_labelBoxLayout = new javax.swing.GroupLayout(jPanel_labelBox);
        jPanel_labelBox.setLayout(jPanel_labelBoxLayout);
        jPanel_labelBoxLayout.setHorizontalGroup(
            jPanel_labelBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel_labelBoxLayout.setVerticalGroup(
            jPanel_labelBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel_PerformancesLayout = new javax.swing.GroupLayout(jPanel_Performances);
        jPanel_Performances.setLayout(jPanel_PerformancesLayout);
        jPanel_PerformancesLayout.setHorizontalGroup(
            jPanel_PerformancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_PerformancesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_PerformancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_labelBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_totalScore, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addGroup(jPanel_PerformancesLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel_PerformancesLayout.setVerticalGroup(
            jPanel_PerformancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_PerformancesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_totalScore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_labelBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel_Grades.setBackground(new java.awt.Color(0, 153, 255));

        jLabel_grade.setFont(new java.awt.Font("Engravers MT", 3, 175)); // NOI18N
        jLabel_grade.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_grade.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel_grade.setText("S");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GRADE:");

        javax.swing.GroupLayout jPanel_GradesLayout = new javax.swing.GroupLayout(jPanel_Grades);
        jPanel_Grades.setLayout(jPanel_GradesLayout);
        jPanel_GradesLayout.setHorizontalGroup(
            jPanel_GradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GradesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_GradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel_grade, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel_GradesLayout.setVerticalGroup(
            jPanel_GradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_GradesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_grade)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Header, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jPanel_Stats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel_Grades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_Performances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Return, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel_Grades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel_Performances, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Return, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel_Stats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 73, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_ReturnMouseClicked
        // TODO add your handling code here:
        SongSelectionFrame frame = new SongSelectionFrame();
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton_ReturnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ScoreResultFrame(1).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jAccuracy;
    private javax.swing.JLabel jBadCounter;
    private javax.swing.JButton jButton_Return;
    private javax.swing.JLabel jComboCounter;
    private javax.swing.JLabel jGoodCounter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel_accuracy;
    private javax.swing.JLabel jLabel_badCount;
    private javax.swing.JLabel jLabel_clearedTimestamp;
    private javax.swing.JLabel jLabel_comboCount;
    private javax.swing.JLabel jLabel_creatorBeatmap;
    private javax.swing.JLabel jLabel_goodCount;
    private javax.swing.JLabel jLabel_grade;
    private javax.swing.JLabel jLabel_missCount;
    private javax.swing.JLabel jLabel_namaBeatmap;
    private javax.swing.JLabel jLabel_perfectCount;
    private javax.swing.JLabel jLabel_totalScore;
    private javax.swing.JLabel jMissCounter;
    private javax.swing.JPanel jPanel_Grades;
    private javax.swing.JPanel jPanel_Header;
    private javax.swing.JPanel jPanel_Performances;
    private javax.swing.JPanel jPanel_Stats;
    private javax.swing.JPanel jPanel_labelBox;
    private javax.swing.JLabel jPerfectCounter;
    // End of variables declaration//GEN-END:variables
}