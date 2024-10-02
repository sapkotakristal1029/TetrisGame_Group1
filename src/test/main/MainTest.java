package main;

import main.screens.MainScreen;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main() throws InterruptedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        // Run the main method
        Method mainMethod = Main.class.getDeclaredMethod("main", String[].class);
        mainMethod.setAccessible(true);
        mainMethod.invoke(null, (Object) new String[]{});

        // Wait for the splash screen duration plus a buffer
        Thread.sleep(3500);

        // Get all frames and find the main window
        Frame[] frames = Frame.getFrames();
        JFrame mainWindow = null;
        for (Frame frame : frames) {
            if (frame instanceof JFrame && "Game".equals(frame.getTitle())) {
                mainWindow = (JFrame) frame;
                break;
            }
        }

        // Verify that the main window is displayed
        assertNotNull(mainWindow);
        assertTrue(mainWindow.isVisible());

        // Verify that the main window contains the card panel with the correct screens
        Container contentPane = mainWindow.getContentPane();
        assertTrue(contentPane.getComponent(0) instanceof JPanel);
        JPanel cardPanel = (JPanel) contentPane.getComponent(0);
        assertTrue(cardPanel.getLayout() instanceof CardLayout);

        // Verify that the main screen is initially displayed
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        Component visibleComponent = getVisibleComponent(cardPanel);
        assertNotNull(visibleComponent);
        assertTrue(visibleComponent instanceof MainScreen);
    }

    // Helper method to get the currently visible component in a CardLayout
    private Component getVisibleComponent(JPanel cardPanel) {
        for (Component comp : cardPanel.getComponents()) {
            if (comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }
}