/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package rhythmain.ui;

import java.awt.Color;
import java.util.Arrays;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;
import javax.swing.Timer;
import rhythmain.audio.AudioPlayer;
import rhythmain.dao.BeatmapDAO;
import rhythmain.model.Song;
import rhythmain.utils.BeatmapReader;
import rhythmain.utils.Note;
import rhythmain.utils.ScoreManager;


class NoteSenar {
    public Note note;
    public int y;
    public JPanel component;
    
    public NoteSenar(Note note, int y, JPanel component) {
        this.note = note;
        this.y = y;
        this.component = component;
    }
}

/**
 *
 * @author Fadhil
 */
public class GameplayFrame extends javax.swing.JFrame implements KeyListener {
    // Informasi penting.
    Song song;
    String difficulty;
    
    // Skor game.
    int skor = 0;
    // Kecepatan note.
    int kecepatanNote = 4;
    // Waktu kapan game pertama kali dimulai.
    long startTime;
    
    // Untuk memainkan musik.
    AudioPlayer audioPlayer = new AudioPlayer();
    ScoreManager scoreManager = new ScoreManager();
    
    private int senarButtonY;
    Queue<Note> daftarNoteBelumDitambahkan = new LinkedList();
    Queue<NoteSenar> daftarNoteSenarD = new LinkedList();
    Queue<NoteSenar> daftarNoteSenarF = new LinkedList();
    Queue<NoteSenar> daftarNoteSenarJ = new LinkedList();
    Queue<NoteSenar> daftarNoteSenarK = new LinkedList();
    
    
    
    public GameplayFrame(Song song, String difficulty) {
        initComponents();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        setLocationRelativeTo(null);
        
        this.song = song;
        this.difficulty = difficulty;
        
        // Ambil posisi button tiap senar (tapi ambil dari senar D aja).
        senarButtonY = senarD.getSize().height;
        // Sembunyikan noteInfoText (Teks informasi buat Perfect, Miss, Offbeat)
        noteInfoText.setVisible(false);

        // Baca beatmap.
        bacaBeatmap();
        // Mulai game.
        mulaiGame();
    }
    
    private void bacaBeatmap() {
        // Ambil beatmap dari database.
        BeatmapDAO koneksiDatabase = new BeatmapDAO();
        String beatmapJson = koneksiDatabase.getBeatmap(song.getSongId(), difficulty);

        // Baca beatmap.
        BeatmapReader beatmapReader = new BeatmapReader();
        Note[] notes = beatmapReader.bacaBeatMap(beatmapJson);
        // Urutkan beatmap berdasarkan timing yang paling awal.
        Arrays.sort(notes, Comparator.comparingDouble(note -> note.timing));
        
        // Tambahkan semua note ke Queue daftar note.
        for (Note note : notes) {
            daftarNoteBelumDitambahkan.add(note);
        }
    }
    
    private void mulaiGame() {
        // Mainkan lagu.
        audioPlayer.loadAudio("assets/songs/a/audio.wav");
        audioPlayer.play();
        
        // Simpan waktu pertama kali game dimulai.
        startTime = System.currentTimeMillis();
        
        // Jalankan looping.
        Timer mainLoop = new Timer(16, e -> {
            cekQueueNote();
            prosesNotePadaSenar(senarD, daftarNoteSenarD);
            prosesNotePadaSenar(senarF, daftarNoteSenarF);
            prosesNotePadaSenar(senarJ, daftarNoteSenarJ);
            prosesNotePadaSenar(senarK, daftarNoteSenarK);
        });
        mainLoop.start(); 
    }
    
    private void cekQueueNote() {
        float elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0f;
        
        while (!daftarNoteBelumDitambahkan.isEmpty()) {
            Note notePertama = daftarNoteBelumDitambahkan.peek();
            
            if (elapsedSeconds >= notePertama.timing) {
                System.out.println("Tambah note karena sudah waktunya.");
                daftarNoteBelumDitambahkan.remove(notePertama);
                try {
                    tambahNote(notePertama);
                } catch (Exception ex) {
                    System.getLogger(GameplayFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            } else {
                break;
            }
        }
    };
    
    private void prosesNotePadaSenar(JPanel senar, Queue<NoteSenar> daftarNoteSenar) {
        Iterator<NoteSenar> iterator = daftarNoteSenar.iterator();
        while (iterator.hasNext()) {
            NoteSenar noteSenar = iterator.next();
            noteSenar.y += kecepatanNote;
            
            if (noteSenar.y >= senarButtonY + 15) {
                senar.remove(noteSenar.component);
                senar.revalidate();
                senar.repaint();
                iterator.remove();
                noteApabilaMiss(senar);
            } else {
                noteSenar.component.setLocation(0, noteSenar.y);
            }
        }
    }
    
    private void tambahNote(Note note) throws Exception {
        JPanel noteComponent = new JPanel();
        noteComponent.setBackground(Color.blue);
        noteComponent.setSize(100, 20);
        
        JPanel column;
        Queue<NoteSenar> columnNotes;
        switch (note.posisi) {
            case 1 -> {
                column = senarD;
                columnNotes = daftarNoteSenarD;
            }
            case 2 -> {
                column = senarF;
                columnNotes = daftarNoteSenarF;
            }
            case 3 -> {
                column = senarJ;
                columnNotes = daftarNoteSenarJ;
            }
            case 4 -> {
                column = senarK;
                columnNotes = daftarNoteSenarK;
            }
            default -> throw new Exception("Posisi note salah");
        }
        
        // Tambah informasi note (informasi note, posisi Y note, komponen note) ke queue dari kolom (senar) yang dipilih.
        NoteSenar columnNote = new NoteSenar(note, 0, noteComponent);
        columnNotes.add(columnNote);
        // Tambah komponen note ke kolom (senar).
        column.add(columnNote.component);
    }
    
    private void cekNoteApakahKena(JPanel senar, Queue<NoteSenar> daftarNoteSenar) {
        // Ambil note paling pertama pada senar.
        NoteSenar notePertama = daftarNoteSenar.poll();
        // Kalau ga ada note sama sekali dalam senar, hentikan eksekusi.
        if (notePertama == null) {
            return;
        }
       
        // Cek apakah note kena tombol senar.
        if (notePertama.y + 20 >= senarButtonY) {
            noteApabilaKena(senar);
        } else {
            noteApabilaOffbeat(senar);
        }
        
        senar.remove(notePertama.component);
        senar.revalidate();
        senar.repaint();
    }
    
    private void noteApabilaKena(JPanel senar) {
        senar.setBackground(Color.green);
        noteInfoText.setText("Perfect");
        noteInfoText.setVisible(true);
        
        Timer timer = new Timer(250, e -> {
           senar.setBackground(Color.black);
           noteInfoText.setVisible(false);
           ((Timer)e.getSource()).stop();
        });
        timer.start();
        
        AudioPlayer soundEffect = new AudioPlayer();
        soundEffect.loadAudio("assets/songs/a/clap (fixed).wav");
        soundEffect.play();
        
        ubahSkor(10);
    }
    
    private void noteApabilaOffbeat(JPanel senar) {
        senar.setBackground(Color.magenta);
        noteInfoText.setText("Offbeat");
        noteInfoText.setVisible(true);
        
        var bungkusVariabel = new Object(){ int jogetKeBerapa = 1; };
        Timer timerWindowJoget = new Timer(25, e -> {
            Point posisiWindow = this.getLocation();
            
            int kePosisiX = posisiWindow.x;
            if (bungkusVariabel.jogetKeBerapa % 2 == 0) {
                kePosisiX += 10;
            } else {
                kePosisiX -= 10;
            }
                    
            this.setLocation(kePosisiX, posisiWindow.y);
            
            if (bungkusVariabel.jogetKeBerapa == 15) {
                ((Timer)e.getSource()).stop();
            }
            bungkusVariabel.jogetKeBerapa++;
        });
        Timer timerTextDanSenar = new Timer(250, e -> {
           senar.setBackground(Color.black);
           noteInfoText.setVisible(false);
           ((Timer)e.getSource()).stop();
        });
        
        timerWindowJoget.start();
        timerTextDanSenar.start();
        
        AudioPlayer soundEffect = new AudioPlayer();
        soundEffect.loadAudio("assets/songs/a/LS-HKI Big Kick 09 (fixed).wav");
        soundEffect.play();
        
        ubahSkor(-5);
    }
    
    private void noteApabilaMiss(JPanel senar) {
        senar.setBackground(Color.red);
        noteInfoText.setText("Miss");
        noteInfoText.setVisible(true);
        
        Timer timer = new Timer(250, e -> {
           senar.setBackground(Color.black);
           noteInfoText.setVisible(false);
           ((Timer)e.getSource()).stop();
        });
        timer.start();
        
        ubahSkor(-10);
    }
    
    private void ubahSkor(int berapa) {
        skor += berapa;
        skorText.setText("Skor: " + skor);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        noteInfoText = new javax.swing.JLabel();
        skorText = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        senarD = new javax.swing.JPanel();
        senarDButton = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        senarF = new javax.swing.JPanel();
        senarFButton = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        senarJ = new javax.swing.JPanel();
        senarJButton = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        senarK = new javax.swing.JPanel();
        senarKButton = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setLayout(new javax.swing.OverlayLayout(jPanel6));

        jPanel10.setOpaque(false);

        noteInfoText.setBackground(new java.awt.Color(255, 255, 255));
        noteInfoText.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        noteInfoText.setForeground(new java.awt.Color(255, 255, 255));
        noteInfoText.setText("Perfect");

        skorText.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        skorText.setForeground(new java.awt.Color(255, 255, 255));
        skorText.setText("Skor: 0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(768, Short.MAX_VALUE)
                .addComponent(skorText)
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(noteInfoText)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(skorText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 434, Short.MAX_VALUE)
                .addComponent(noteInfoText)
                .addGap(59, 59, 59))
        );

        jPanel6.add(jPanel10);

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jPanel4.setPreferredSize(new java.awt.Dimension(100, 85));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        senarD.setBackground(new java.awt.Color(0, 0, 0));
        senarD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        senarD.setPreferredSize(new java.awt.Dimension(56, 50));

        javax.swing.GroupLayout senarDLayout = new javax.swing.GroupLayout(senarD);
        senarD.setLayout(senarDLayout);
        senarDLayout.setHorizontalGroup(
            senarDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        senarDLayout.setVerticalGroup(
            senarDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(senarD, gridBagConstraints);

        senarDButton.setBackground(new java.awt.Color(255, 51, 51));
        senarDButton.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("D");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        senarDButton.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(senarDButton, gridBagConstraints);

        jPanel2.add(jPanel4);

        jPanel7.setPreferredSize(new java.awt.Dimension(100, 76));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        senarF.setBackground(new java.awt.Color(0, 0, 0));
        senarF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        senarF.setPreferredSize(new java.awt.Dimension(56, 50));

        javax.swing.GroupLayout senarFLayout = new javax.swing.GroupLayout(senarF);
        senarF.setLayout(senarFLayout);
        senarFLayout.setHorizontalGroup(
            senarFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        senarFLayout.setVerticalGroup(
            senarFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(senarF, gridBagConstraints);

        senarFButton.setBackground(new java.awt.Color(255, 51, 51));
        senarFButton.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("F");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        senarFButton.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(senarFButton, gridBagConstraints);

        jPanel2.add(jPanel7);

        jPanel11.setPreferredSize(new java.awt.Dimension(100, 76));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        senarJ.setBackground(new java.awt.Color(0, 0, 0));
        senarJ.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        senarJ.setPreferredSize(new java.awt.Dimension(56, 50));

        javax.swing.GroupLayout senarJLayout = new javax.swing.GroupLayout(senarJ);
        senarJ.setLayout(senarJLayout);
        senarJLayout.setHorizontalGroup(
            senarJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        senarJLayout.setVerticalGroup(
            senarJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel11.add(senarJ, gridBagConstraints);

        senarJButton.setBackground(new java.awt.Color(255, 51, 51));
        senarJButton.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("J");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        senarJButton.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.weightx = 1.0;
        jPanel11.add(senarJButton, gridBagConstraints);

        jPanel2.add(jPanel11);

        jPanel14.setPreferredSize(new java.awt.Dimension(100, 76));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        senarK.setBackground(new java.awt.Color(0, 0, 0));
        senarK.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        senarK.setPreferredSize(new java.awt.Dimension(56, 50));

        javax.swing.GroupLayout senarKLayout = new javax.swing.GroupLayout(senarK);
        senarK.setLayout(senarKLayout);
        senarKLayout.setHorizontalGroup(
            senarKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        senarKLayout.setVerticalGroup(
            senarKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel14.add(senarK, gridBagConstraints);

        senarKButton.setBackground(new java.awt.Color(255, 51, 51));
        senarKButton.setLayout(new java.awt.GridBagLayout());

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("K");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        senarKButton.add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.weightx = 1.0;
        jPanel14.add(senarKButton, gridBagConstraints);

        jPanel2.add(jPanel14);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel noteInfoText;
    private javax.swing.JPanel senarD;
    private javax.swing.JPanel senarDButton;
    private javax.swing.JPanel senarF;
    private javax.swing.JPanel senarFButton;
    private javax.swing.JPanel senarJ;
    private javax.swing.JPanel senarJButton;
    private javax.swing.JPanel senarK;
    private javax.swing.JPanel senarKButton;
    private javax.swing.JLabel skorText;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key pressed");
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D -> {
                cekNoteApakahKena(senarD, daftarNoteSenarD);
            }
            case KeyEvent.VK_F -> {
                cekNoteApakahKena(senarF, daftarNoteSenarF);
            }
            case KeyEvent.VK_J -> {
                cekNoteApakahKena(senarJ, daftarNoteSenarJ);
            }
            case KeyEvent.VK_K -> {
                cekNoteApakahKena(senarK, daftarNoteSenarK);
            }
        }

    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ScoreResultFrame.class.getName());
}
