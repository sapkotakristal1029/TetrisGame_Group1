package main.screens;

import javax.swing.*;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class ConfigurationScreen extends JPanel {

    private JSlider fieldWidthSlider;
    private JLabel fieldWidthLabel;
    private JSlider fieldHeightSlider;
    private JLabel fieldHeightLabel;
    private JSlider gameLevelSlider;
    private JLabel gameLevelLabel;
    private JCheckBox musicCheckBox;
    private JCheckBox soundEffectCheckBox;
    private JCheckBox aiPlayCheckBox;
    private JCheckBox extendModeCheckBox;
    private JButton backButton;

    String spaces = " ".repeat(100);
    String space1 = " ".repeat(25);
    String onText = spaces + "On";
    String offText = spaces + "Off";
    String labelText = space1+ "Configuration";
    Color softBlue = new Color(173, 216, 230); // Soft blue color

    public ConfigurationScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Set panel background color
        this.setBackground(softBlue);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Span across all columns
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel(labelText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.black);
        this.add(titleLabel, gbc);

        // Field Width Slider
        gbc.gridwidth = 1; // Reset to default
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Field Width:"), gbc);
        fieldWidthSlider = new JSlider(5, 15, 12);
        fieldWidthSlider.setMajorTickSpacing(1);
        fieldWidthSlider.setMinorTickSpacing(1);
        fieldWidthSlider.setPaintTicks(true);
        fieldWidthSlider.setPaintLabels(true);
        fieldWidthSlider.setLabelTable(createLabelTable(5, 15));
        fieldWidthSlider.setBackground(softBlue);
        fieldWidthLabel = new JLabel("12");
        gbc.gridx = 1;
        this.add(fieldWidthSlider, gbc);
        gbc.gridx = 2;
        this.add(fieldWidthLabel, gbc);
        fieldWidthSlider.addChangeListener(e -> fieldWidthLabel.setText(String.valueOf(fieldWidthSlider.getValue())));

        // Field Height Slider
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Field Height:"), gbc);
        fieldHeightSlider = new JSlider(15, 30, 15);
        fieldHeightSlider.setMajorTickSpacing(1);
        fieldHeightSlider.setMinorTickSpacing(1);
        fieldHeightSlider.setPaintTicks(true);
        fieldHeightSlider.setPaintLabels(true);
        fieldHeightSlider.setLabelTable(createLabelTable(15, 30));
        fieldHeightSlider.setBackground(softBlue);
        fieldHeightLabel = new JLabel("15");
        gbc.gridx = 1;
        this.add(fieldHeightSlider, gbc);
        gbc.gridx = 2;
        this.add(fieldHeightLabel, gbc);
        fieldHeightSlider.addChangeListener(e -> fieldHeightLabel.setText(String.valueOf(fieldHeightSlider.getValue())));

        // Game Level Slider
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(new JLabel("Game Level:"), gbc);
        gameLevelSlider = new JSlider(1, 10, 1);
        gameLevelSlider.setMajorTickSpacing(1);
        gameLevelSlider.setMinorTickSpacing(1);
        gameLevelSlider.setPaintTicks(true);
        gameLevelSlider.setPaintLabels(true);
        gameLevelSlider.setLabelTable(createLabelTable(1, 10));
        gameLevelSlider.setBackground(softBlue);
        gameLevelLabel = new JLabel("1");
        gbc.gridx = 1;
        this.add(gameLevelSlider, gbc);
        gbc.gridx = 2;
        this.add(gameLevelLabel, gbc);
        gameLevelSlider.addChangeListener(e -> gameLevelLabel.setText(String.valueOf(gameLevelSlider.getValue())));

        // Music On/Off
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(new JLabel("Music:"), gbc);
        musicCheckBox = new JCheckBox(offText);
        customizeCheckBox(musicCheckBox);
        gbc.gridx = 1;
        this.add(musicCheckBox, gbc);
        musicCheckBox.addActionListener(e -> updateCheckBoxLabel(musicCheckBox));

        // Sound Effect On/Off
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(new JLabel("Sound Effects:"), gbc);
        soundEffectCheckBox = new JCheckBox(offText);
        customizeCheckBox(soundEffectCheckBox);
        gbc.gridx = 1;
        this.add(soundEffectCheckBox, gbc);
        soundEffectCheckBox.addActionListener(e -> updateCheckBoxLabel(soundEffectCheckBox));

        // AI Play
        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(new JLabel("AI Play:"), gbc);
        aiPlayCheckBox = new JCheckBox(offText);
        customizeCheckBox(aiPlayCheckBox);
        gbc.gridx = 1;
        this.add(aiPlayCheckBox, gbc);
        aiPlayCheckBox.addActionListener(e -> updateCheckBoxLabel(aiPlayCheckBox));

        // Extended Mode
        gbc.gridx = 0;
        gbc.gridy = 7;
        this.add(new JLabel("Extended Mode:"), gbc);
        extendModeCheckBox = new JCheckBox(offText);
        customizeCheckBox(extendModeCheckBox);
        gbc.gridx = 1;
        this.add(extendModeCheckBox, gbc);
        extendModeCheckBox.addActionListener(e -> updateCheckBoxLabel(extendModeCheckBox));

        // Back Button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        backButton = new JButton("Back");
        backButton.setBackground(Color.black);
        backButton.setForeground(Color.white);
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setPreferredSize(new Dimension(100, 30)); // Set size to 100x30 pixels
        backButton.addActionListener(new BackButtonListener(cardLayout, cardPanel));
        this.add(backButton, gbc);
    }

    private void customizeCheckBox(JCheckBox checkBox) {
        checkBox.setForeground(Color.black);
        checkBox.setBackground(new Color(173, 216, 230));
        checkBox.setFocusPainted(false);
        checkBox.setOpaque(true);
        checkBox.setUI(new CustomCheckBoxUI());
    }

    private Hashtable<Integer, JLabel> createLabelTable(int min, int max) {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = min; i <= max; i++) {
            labelTable.put(i, new JLabel(String.valueOf(i)));
        }
        return labelTable;
    }

    private void updateCheckBoxLabel(JCheckBox checkBox) {
        checkBox.setText(checkBox.isSelected() ? onText : offText);
    }

    private static class CustomCheckBoxUI extends BasicCheckBoxUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            JCheckBox checkBox = (JCheckBox) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (checkBox.isSelected()) {
                g2.setColor(Color.black);
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
                g2.setColor(new Color(173, 216, 230));
                g2.drawLine(4, 10, 8, 14);
                g2.drawLine(8, 14, 16, 6);
            } else {
                g2.setColor(new Color(173, 216, 230));
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
            g2.dispose();
            super.paint(g, c);
        }
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
