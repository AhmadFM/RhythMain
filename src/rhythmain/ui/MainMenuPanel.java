/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.ui;

import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private MainFrame mainFrame;

    public MainMenuPanel(MainFrame frame) {
        this.mainFrame = frame;
        // Menggunakan GridBagLayout agar posisi komponen presisi di tengah-tengah layar
        setLayout(new GridBagLayout()); 
        // Mengatur warna background bertema gelap (dark gaming vibe)
        setBackground(new Color(20, 20, 30)); 

        GridBagConstraints gbc = new GridBagConstraints();
        // Mengatur jarak antar komponen (atas, kiri, bawah, kanan) sebesar 12 piksel
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.gridx = 0; // Semua komponen sejajar di kolom 0 (tengah vertikal)

        // 1. Label Judul Game
        JLabel titleLabel = new JLabel("RhythMain");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 54));
        titleLabel.setForeground(Color.CYAN); // Warna teks cyan agar kontras
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // 2. Status Akun Pemain ("Halo, Guest!")
        JLabel userLabel = new JLabel("Halo, Guest!");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(userLabel, gbc);

        // 3. Tombol PLAY
        JButton playButton = createMenuButton("PLAY");
        gbc.gridy = 2;
        add(playButton, gbc);
        playButton.addActionListener(e -> {
            // Logic buat navigasi ke modul Song Selection 
            // mainFrame.switchPanel("SongSelection");
            JOptionPane.showMessageDialog(this, "Navigasi ke Pemilihan Lagu (Modul A2)");
        });

        // 4. Tombol ACCOUNT
        JButton accountButton = createMenuButton("ACCOUNT");
        gbc.gridy = 3;
        add(accountButton, gbc);
        accountButton.addActionListener(e -> {
            // Logic buat navigasi ke modul Account & Settings 
            // mainFrame.switchPanel("AccountPanel");
            JOptionPane.showMessageDialog(this, "Navigasi ke Manajemen Akun (Modul A5)");
        });

        // 5. Tombol SETTINGS
        JButton settingsButton = createMenuButton("SETTINGS");
        gbc.gridy = 4;
        add(settingsButton, gbc);

        // 6. Tombol EXIT
        JButton exitButton = createMenuButton("EXIT");
        gbc.gridy = 5;
        add(exitButton, gbc);
        exitButton.addActionListener(e -> {
            // Keluar dari aplikasi secara bersih
            System.exit(0); 
        });
    }

    // Method pembantu untuk membuat cetakan tombol dengan desain seragam
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40)); // Ukuran seragam tombol menu
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false); // Menghilangkan garis border fokus kotak saat diklik
        return button;
    }
}

/**
 *
 * @author gante
 */
