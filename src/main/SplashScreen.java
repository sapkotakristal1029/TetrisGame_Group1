package main;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private final int duration;

    public SplashScreen(int duration) {
        this.duration = duration;
    }

    public void showSplash() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        int width = 450;
        int height = 300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Add splash screen content here
        JLabel label = new JLabel(new ImageIcon("src/main/images/splash.png"));
        JLabel copyright = new JLabel("Copyright 2024, Group - 1 ", JLabel.CENTER);
        copyright.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        content.add(label, BorderLayout.CENTER);
        content.add(copyright, BorderLayout.SOUTH);

        setVisible(true);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setVisible(false);
    }

    public void showSplashAndExit() {
        showSplash();
        System.exit(0);
    }
}
