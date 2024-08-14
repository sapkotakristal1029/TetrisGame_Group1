package main.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HighScoreScreen extends JPanel {

    private JButton backButton;

    public HighScoreScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setLayout(new BorderLayout());

        // Top 10 Scores List
        String[] columnNames = {"Player Name", "Score"};
        Object[][] data = {
                {"Alice", 1500},
                {"Bob", 1400},
                {"Charlie", 1300},
                {"David", 1200},
                {"Eve", 1100},
                {"Frank", 1000},
                {"Grace", 900},
                {"Heidi", 800},
                {"Ivan", 700},
                {"Judy", 600}
        };
        JTable scoreTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(scoreTable);

        // Add Components
        this.add(scrollPane, BorderLayout.CENTER);

        // Back Button
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonListener(cardLayout, cardPanel));
        this.add(backButton, BorderLayout.SOUTH);

        // Configure Panel
        this.setPreferredSize(new Dimension(400, 300));
        this.setBackground(Color.white);
    }

    private class BackButtonListener implements ActionListener {
        private CardLayout cardLayout;
        private JPanel cardPanel;

        public BackButtonListener(CardLayout cardLayout, JPanel cardPanel) {
            this.cardLayout = cardLayout;
            this.cardPanel = cardPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel, "MainScreen");
        }
    }
}
