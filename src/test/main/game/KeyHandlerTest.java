package main.game;

import main.screens.ConfigurationScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class KeyHandlerTest {

    private KeyHandler keyHandler;
    private ConfigurationScreen configScreen;
    private PlayManager playManager;

    @BeforeEach
    void setUp() {
        keyHandler = new KeyHandler();
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        configScreen = new ConfigurationScreen(cardLayout, cardPanel);
        playManager = PlayManager.getInstance();
    }

    @Test
    void setConfigurationScreen() {
        KeyHandler.setConfigurationScreen(configScreen);
        assertNotNull(getPrivateField(KeyHandler.class, "configScreen"));
    }

    @Test
    void setPlayManager() {
        KeyHandler.setPlayManager(playManager);
        assertNotNull(getPrivateField(KeyHandler.class, "playManager"));
    }

    @Test
    void keyTyped() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        keyHandler.keyTyped(keyEvent);
        // No assertions needed as keyTyped does nothing
    }

    @Test
    void keyPressed() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(keyEvent);
        assertTrue(KeyHandler.upPressed);

        keyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_M, 'M');
        keyHandler.keyPressed(keyEvent);
        assertTrue(KeyHandler.musicPressed);
    }

    @Test
    void keyReleased() {
        KeyEvent keyEvent = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        keyHandler.keyReleased(keyEvent);
        assertFalse(KeyHandler.upPressed);
    }

    private Object getPrivateField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}