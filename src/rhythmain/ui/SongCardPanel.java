/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rhythmain.model.Song;

/**
 *
 * @author LENOVO
 */

public class SongCardPanel extends JPanel {

    private Song song;

    public SongCardPanel(Song song) {

        this.song = song;

        setLayout(new GridLayout(2, 1));

        setBackground(
                new Color(30, 40, 60));

        setBorder(
                BorderFactory.createLineBorder(
                        Color.CYAN,
                        1
                )
        );

        JLabel lblTitle =
                new JLabel(song.getTitle());

        JLabel lblArtist =
                new JLabel(song.getArtist());

        lblTitle.setForeground(Color.WHITE);

        lblArtist.setForeground(
                Color.LIGHT_GRAY);

        add(lblTitle);
        add(lblArtist);
    }

    public Song getSong() {
        return song;
    }
}