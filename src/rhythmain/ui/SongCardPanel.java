/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

    setLayout(null);

    setPreferredSize(
            new Dimension(
                    300,
                    70
            )
    );

    setBackground(
            new Color(
                    30,
                    40,
                    60
            )
    );

    setBorder(
            BorderFactory.createLineBorder(
                    Color.CYAN,
                    1
            )
    );

    JLabel lblTitle =
            new JLabel(
                    song.getTitle()
            );

    lblTitle.setBounds(
            15,
            10,
            300,
            25
    );

    lblTitle.setForeground(
            Color.WHITE
    );

    lblTitle.setFont(
            new Font(
                    "Arial",
                    Font.BOLD,
                    20
            )
    );

    add(lblTitle);

    JLabel lblArtist =
            new JLabel(
                    song.getArtist()
            );

    lblArtist.setBounds(
            15,
            38,
            100,
            20
    );

    lblArtist.setForeground(
            Color.LIGHT_GRAY
    );

    add(lblArtist);

    JLabel lblDuration =
            new JLabel(
                    "03:00"
            );

    lblDuration.setBounds(
            450,
            20,
            50,
            25
    );

    lblDuration.setForeground(
            Color.WHITE
    );

    lblDuration.setFont(
            new Font(
                    "Arial",
                    Font.BOLD,
                    18
            )
    );

    add(lblDuration);
}    
}