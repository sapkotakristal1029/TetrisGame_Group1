package main;

import javax.swing.*;        // For JFrame, SwingUtilities, and Timer
import java.awt.*;           // For CardLayout and other AWT components

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create and show the splash screen
            SplashScreen splash = new SplashScreen(3000); // Display for 3 seconds
            splash.showSplash();

            // Schedule the main window to be shown after the splash screen is hidden
            Timer timer = new Timer(3000, e -> {
                // Create the main window
                JFrame window = new JFrame("Game");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);
                // Set up a CardLayout and retain default size
                window.setLayout(new CardLayout());  // Create a CardLayout for the window

                // Create and use the GameFacade for screen management
                GameFacade gameFacade = new GameFacade(window);

                // Show the main screen initially
                gameFacade.showMainScreen();

                // Display the main window
                window.pack();  // Pack components as per their preferred size
                window.setLocationRelativeTo(null); // Center the window
                window.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}
