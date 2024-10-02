package main.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GamePanelTest {

    private GamePanel gamePanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    @BeforeEach
    void setUp() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        gamePanel = new GamePanel(cardLayout, cardPanel);
    }
    @AfterEach
    void tearDown() {
        gamePanel = null;
        cardLayout = null;
        cardPanel = null;
    }
    @Test
    void run() {
        // This method runs the game loop, which is difficult to test directly.
        // Consider testing the effects of the run method instead.
    }

    @Test
    void update() {
        gamePanel.startGame();
        gamePanel.update();
        // Add assertions based on what update() is supposed to change
    }

    @Test
    void addNotify() {
        gamePanel.addNotify();
        assertTrue(gamePanel.isFocusable());
    }

}