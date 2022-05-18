package com.company;

import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame() {
        GamePanel panel = new GamePanel();
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(panel);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
