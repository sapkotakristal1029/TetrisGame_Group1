package main.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private static final int FPS = 60; // Frames per second
    private Thread gameThread;
    private PlayManager pm;
    private boolean running = false;
    private JButton backButton; // Add back button
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private boolean backPressed = false; // Flag to track if back button is pressed

    // Constructor with CardLayout and JPanel
    public GamePanel(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black); // Set background to black
        this.setLayout(null);

        // Implement Key handler
        KeyHandler keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.requestFocusInWindow(); // Ensure focus is set

        pm = new PlayManager();

        // Initialize and add back button
        backButton = new JButton("Back");
        backButton.setBounds(600, 670, 100, 30);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.white);
        backButton.addActionListener(new BackButtonListener());
        this.add(backButton);
    }

    // Start or resume the game
    public void startGame() {
        if (running) return; // Prevent multiple threads
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Pause the game
    public void stopGame() {
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // Interval between frames
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (!KeyHandler.pausePressed && !pm.gameOver && !backPressed) {
            pm.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw the black background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw Tetris-like title text on the left side
        g2.setFont(new Font("SansSerif", Font.BOLD, 100));
        g2.setColor(new Color(128, 0, 128));
        g2.drawString("TETRIS", 50, 350);
        g2.drawString("GAME", 75, 450);



        // Draw game elements
        pm.draw(g2);

        // If the game is paused due to back button press, display "Paused"
        if (backPressed) {
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.setColor(Color.yellow);
            g2.drawString("Paused", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); // Request focus after adding to the container
    }

    // Inner class to handle back button clicks
    private class BackButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Stop the game to pause it
            stopGame();

            // Set the backPressed flag to true and repaint the screen
            backPressed = true;
            repaint();

            int option = JOptionPane.showConfirmDialog(
                    GamePanel.this,
                    "Do you want to go back? This will forfeit the current game.",
                    "Confirm Back",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                resetAndShowMainScreen();
            } else if (option == JOptionPane.NO_OPTION) {
                // Resume the game if it was paused
                if (!running) {
                    backPressed = false; // Reset the flag
                    startGame();
                    requestFocusInWindow(); // Request focus back to the game panel
                }
            }
        }

        private void resetAndShowMainScreen() {
            // Reset game state (reinitialize PlayManager)
            pm = new PlayManager();

            // Clear static blocks
            PlayManager.staticBlocks.clear();

            // Switch to the MainScreen
            cardPanel.remove(GamePanel.this);
            cardLayout.show(cardPanel, "MainScreen");

            //Unpause game if paused
            if(KeyHandler.pausePressed){
                KeyHandler.pausePressed= false;
            }
        }
    }
}
