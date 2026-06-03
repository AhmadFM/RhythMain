/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.ui;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author LENOVO
 */

public class ScoreCardPanel extends JPanel {

    public ScoreCardPanel(
            int rank,
            String username,
            int score) {

        initComponents(rank, username, score);
    }

    private void initComponents(
            int rank,
            String username,
            int score) {

        setLayout(null);

        setSize(450, 65);
        setPreferredSize(
                new java.awt.Dimension(
                        450,
                        65
                )
        );

        Color bg;

        switch(rank) {

            case 1:
                bg = new Color(240,170,0);
                break;

            case 2:
                bg = new Color(30,100,240);
                break;

            case 3:
                bg = new Color(210,110,0);
                break;

            default:
                bg = new Color(10,20,60);
        }

        setBackground(bg);

        JLabel lblRank =
                new JLabel(
                        String.valueOf(rank)
                );

        lblRank.setBounds(
                20,
                10,
                40,
                40
        );

        lblRank.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        lblRank.setForeground(
                Color.WHITE
        );

        add(lblRank);

        JLabel lblUser =
                new JLabel(username);

        lblUser.setBounds(
                70,
                10,
                220,
                40
        );

        lblUser.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        lblUser.setForeground(
                Color.WHITE
        );

        add(lblUser);

        JLabel lblScore =
                new JLabel(
                        String.valueOf(score)
                );

        lblScore.setBounds(
                320,
                10,
                120,
                40
        );

        lblScore.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        lblScore.setForeground(
                Color.WHITE
        );

        add(lblScore);
    }
}