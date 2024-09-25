package main;

import main.game.GamePanel;

import main.screens.*;

import javax.swing.*;
import java.awt.*;



public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create and show the splash screen
            SplashScreen splash = new SplashScreen(3000); // Display for 3 seconds
            splash.showSplash();

            // Schedule the main window to be shown after the splash screen is hidden
            Timer timer = new Timer(3000, e -> createAndShowGUI());
            timer.setRepeats(false);
            timer.start();
        });
    }

    private static void createAndShowGUI() {
        JFrame window = new JFrame("Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Set up CardLayout and JPanel to hold the screens
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Create and add screens
        MainScreen mainScreen = new MainScreen(cardLayout, cardPanel);
        ConfigurationScreen configScreen = new ConfigurationScreen(cardLayout, cardPanel);
        HighScoreScreen highScoreScreen = new HighScoreScreen(cardLayout, cardPanel);

        cardPanel.add(mainScreen, "MainScreen");
        cardPanel.add(configScreen, "ConfigurationScreen");
        cardPanel.add(highScoreScreen, "HighScoreScreen");

        // Add the cardPanel to the frame
        window.add(cardPanel);

        // Display the main screen initially
        cardLayout.show(cardPanel, "MainScreen");

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
