package main.game;

import main.screens.ConfigurationScreen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {


     static ConfigurationScreen configScreen;

    // Key press states
    public static boolean upPressed, downPressed, leftPressed, rightPressed;
    public static boolean pausePressed = false;
    public static boolean musicPressed = false;
    public static boolean soundPressed = false;

    // Setter for ConfigurationScreen reference
    public static void setConfigurationScreen(ConfigurationScreen screen) {
        configScreen = screen;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_P) {
            pausePressed = !pausePressed;
        }
        if (code == KeyEvent.VK_M) {
            System.out.println("M is pressed");

            musicPressed = !musicPressed;
            if (configScreen != null) {
                configScreen.toggleMusic(); // Toggle music checkbox
            }
        }
        if (code == KeyEvent.VK_N) {
            soundPressed = !soundPressed;
            if (configScreen != null) {
                configScreen.toggleSound(); // Toggle sound effect checkbox
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }
}
