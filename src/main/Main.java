package main;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {

        SplashScreen splash = new SplashScreen(3000); // Display for 3 seconds
        splash.showSplash();


        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame(); // Start the game loop
    }
}
