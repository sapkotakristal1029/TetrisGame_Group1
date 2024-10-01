// src/main/GameFacade.java
package main;

import main.game.GamePanel;
import main.screens.ConfigurationScreen;
import main.screens.HighScoreScreen;
import main.screens.MainScreen;

import javax.swing.*;
import java.awt.*;

public class GameFacade {

    private JFrame window;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private GamePanel gamePanel;

    // Constructor to initialize the facade
    public GameFacade(JFrame window) {
        this.window = window;
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);
        initializeScreens();
    }

    // Method to initialize all screens and add them to the cardPanel
    private void initializeScreens() {
        MainScreen mainScreen = new MainScreen(cardLayout, cardPanel);
        ConfigurationScreen configScreen = new ConfigurationScreen(cardLayout, cardPanel);
        HighScoreScreen highScoreScreen = new HighScoreScreen(cardLayout, cardPanel);

        // Initialize and add screens
        cardPanel.add(mainScreen, "MainScreen");
        cardPanel.add(configScreen, "ConfigurationScreen");
        cardPanel.add(highScoreScreen, "HighScoreScreen");

        // Add the card panel to the frame
        window.add(cardPanel);
    }

    // Method to show a particular screen
    public void showScreen(String screenName) {
        cardLayout.show(cardPanel, screenName);
    }

    // Method to start the game
    public void startGame() {
        // Pass the necessary arguments to GamePanel's constructor
        gamePanel = new GamePanel(cardLayout, cardPanel);
        cardPanel.add(gamePanel, "GamePanel");
        showScreen("GamePanel");
        gamePanel.startGame();
    }

    // Method to show the main screen
    public void showMainScreen() {
        showScreen("MainScreen");
    }

    // Method to show the configuration screen
    public void showConfigurationScreen() {
        showScreen("ConfigurationScreen");
    }

    // Method to show the high score screen
    public void showHighScoreScreen() {
        showScreen("HighScoreScreen");
    }

}
