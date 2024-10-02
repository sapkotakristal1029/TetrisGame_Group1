package main;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SplashScreenTest {

    @Test
    void showSplash() {
        int duration = 1000; // 1 second
        SplashScreen splashScreen = new SplashScreen(duration);

        // Run the splash screen in a separate thread to avoid blocking the test
        SwingUtilities.invokeLater(splashScreen::showSplash);

        // Wait for the splash screen to be displayed
        try {
            Thread.sleep(500); // Wait for half the duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the splash screen is visible
        assertTrue(splashScreen.isVisible());

        // Wait for the splash screen to disappear
        try {
            Thread.sleep(duration + 500); // Wait for the duration plus a buffer
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the splash screen is no longer visible
        assertFalse(splashScreen.isVisible());
    }
}