/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rhythmain.ui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("RhythMain - Rhythm Game Desktop"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Ukuran standar resolusi window game
        setLocationRelativeTo(null); // Membuat window muncul di tengah layar
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi Panel Utama 
        MainMenuPanel mainMenu = new MainMenuPanel(this);

        // Tambahkan ke CardLayout
        mainPanel.add(mainMenu, "MainMenu");
        
        // Catatan untuk Integrasi: Anggota lain tinggal menempelkan panel mereka di sini:
        // mainPanel.add(songSelectionPanel, "SongSelection");
        // mainPanel.add(accountPanel, "AccountPanel");

        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }

    // Fungsi untuk berpindah antar layar/frame
    public void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}

/**
 *
 * @author gante
 */
