/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.ui;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
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

        setLayout(new BorderLayout());

        setPreferredSize(
            new Dimension(
                450,
                70
            )
        );

        setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                70
            )
        );

        setMinimumSize(
            new Dimension(
                250,
                70
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
                bg = new Color(8,18,60);
        }

        setBackground(bg);

        JLabel lblRank =
                new JLabel(
                        String.valueOf(rank)
                );

        lblRank.setBounds(
                20,
                15,
                40,
                35
        );

        lblRank.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        lblRank.setForeground(
                Color.WHITE
        );

        add(lblRank);

        JLabel lblUser =
                new JLabel(
                        username
                );

        lblUser.setBounds(
                70,
                15,
                200,
                35
        );

        lblUser.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
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
            250,
            15,
            150,
            35
        );

        lblScore.setHorizontalAlignment(
            JLabel.RIGHT
        );

        lblScore.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        lblScore.setForeground(
                Color.WHITE
        );

        add(lblScore);
    }
}