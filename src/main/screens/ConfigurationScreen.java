package main.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationScreen extends JPanel {

    private JTextField fieldWidthField;
    private JTextField fieldHeightField;
    private JTextField gameLevelField;
    private JCheckBox musicCheckBox;
    private JCheckBox soundEffectCheckBox;
    private JCheckBox aiPlayCheckBox;
    private JCheckBox extendModeCheckBox;
    private JButton backButton;

    public ConfigurationScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Field Width
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Field Width:"), gbc);
        fieldWidthField = new JTextField("10");
        gbc.gridx = 1;
        this.add(fieldWidthField, gbc);

        // Field Height
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Field Height:"), gbc);
        fieldHeightField = new JTextField("20");
        gbc.gridx = 1;
        this.add(fieldHeightField, gbc);

        // Game Level
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Game Level:"), gbc);
        gameLevelField = new JTextField("1");
        gbc.gridx = 1;
        this.add(gameLevelField, gbc);

        // Music On/Off
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(new JLabel("Music:"), gbc);
        musicCheckBox = new JCheckBox("On");
        gbc.gridx = 1;
        this.add(musicCheckBox, gbc);

        // Sound Effect On/Off
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(new JLabel("Sound Effects:"), gbc);
        soundEffectCheckBox = new JCheckBox("On");
        gbc.gridx = 1;
        this.add(soundEffectCheckBox, gbc);

        // AI Play
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(new JLabel("AI Play:"), gbc);
        aiPlayCheckBox = new JCheckBox("Enabled");
        gbc.gridx = 1;
        this.add(aiPlayCheckBox, gbc);

        // Extended Mode
        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(new JLabel("Extended Mode:"), gbc);
        extendModeCheckBox = new JCheckBox("Enabled");
        gbc.gridx = 1;
        this.add(extendModeCheckBox, gbc);

        // Back Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonListener(cardLayout, cardPanel));
        this.add(backButton, gbc);

        // Configure Panel
        this.setPreferredSize(new Dimension(400, 300));
        this.setBackground(Color.lightGray);
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

