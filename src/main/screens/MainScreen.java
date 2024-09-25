package main.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.game.GamePanel;

public class MainScreen extends JPanel {

    private JButton playButton;
    private JButton configButton;
    private JButton highScoresButton;
    private JButton exitButton;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Image backgroundImage;

    public MainScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        // Load the background image
        backgroundImage = new ImageIcon("src/main/resources/images/mainmenu.png").getImage();

        this.setLayout(new BorderLayout());

        // Create and style Buttons
        playButton = createStyledButton("Play");
        configButton = createStyledButton("Configuration");
        highScoresButton = createStyledButton("High Scores");
        exitButton = createStyledButton("Exit");

        // Add Action Listeners
        playButton.addActionListener(new PlayButtonListener());
        configButton.addActionListener(new ConfigButtonListener());
        highScoresButton.addActionListener(new HighScoresButtonListener());
        exitButton.addActionListener(new ExitButtonListener());

        // Create Panel for Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin around buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        // Add Buttons to GridBagLayout
        buttonPanel.add(playButton, gbc);
        gbc.gridy++;
        buttonPanel.add(configButton, gbc);
        gbc.gridy++;
        buttonPanel.add(highScoresButton, gbc);
        gbc.gridy++;
        buttonPanel.add(exitButton, gbc);

        // Set background for button panel
        buttonPanel.setOpaque(false); // Make button panel transparent
        this.add(buttonPanel, BorderLayout.CENTER);

        // Set Main Screen properties
        this.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
    }

    // Override the paintComponent method to draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // Method to create a styled button with fixed size
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24)); // Smaller font size
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1), // Border around the button
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margins (top, left, bottom, right)
        ));
        button.setPreferredSize(new Dimension(250, 40)); // Fixed size for buttons
        return button;
    }

    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GamePanel gamePanel = new GamePanel(cardLayout, cardPanel); // Pass the parameters
            cardPanel.add(gamePanel, "GamePanel");
            cardLayout.show(cardPanel, "GamePanel");
            gamePanel.startGame();
        }
    }

    private class ConfigButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel, "ConfigurationScreen");
        }
    }

    private class HighScoresButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel, "HighScoreScreen");
        }
    }

    private class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(
                    MainScreen.this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}
