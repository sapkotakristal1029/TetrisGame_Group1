package main.game;

import main.scoresRecord.Player;
import main.scoresRecord.Scores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1300;
    public static final int HEIGHT = 1000;

    private static final int FPS = 60;
    private Thread gameThread;
    private PlayManager pm;
    private boolean running = false;
    private JButton backButton; // Add back button
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ExecutorService executorService; // ExecutorService for managing threads

    private final Dimension defaultFrameSize = new Dimension(1000, 750); // Default frame size for MainScreen
    private boolean backPressed = false; // Flag to track if back button is pressed

    // Constructor with CardLayout and JPanel
    public GamePanel(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.setBackground(Color.black);
        this.setLayout(null);

        // Implement Key handler
        KeyHandler keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.requestFocusInWindow();

        pm = PlayManager.getInstance();

        // Initialize and add back button
        backButton = new JButton("Back");
        backButton.setBounds(20, 30, 100, 30);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.white);
        // Replace ActionListener with lambda expression
        backButton.addActionListener(e -> handleBackButtonClick());
        this.add(backButton);

        // Initialize ExecutorService with a fixed thread pool
        executorService = Executors.newFixedThreadPool(2);
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
            // Shut down executor service when game is stopped
            if (executorService != null) {
                executorService.shutdown();
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
            try {
                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;

                if (delta >= 1) {
                    // Offload heavy game updates to the executor service
                    executorService.submit(() -> update());
                    repaint();
                    delta--;
                }
            } catch (Exception e) {
                System.err.println("An error occurred during game update: " + e.getMessage());
                e.printStackTrace();
                stopGame(); // Stop the game safely in case of an error
            }
        }
    }

    public void update() {
        if (!KeyHandler.pausePressed && !backPressed) {
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

    private void saveScoreDialog() {
        if (!Scores.have10() || (pm.score > Scores.lastScore())) {
            JDialog dialog = new JDialog();
            dialog.setTitle("Save Your Score");
            dialog.setSize(500, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            JLabel scoreLabel = new JLabel("Score: " + pm.score);
            scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            JLabel nameLabel = new JLabel("Please enter your name: ");
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            JTextField nameField = new JTextField();
            nameField.setFont(new Font("SansSerif", Font.BOLD, 20));
            JButton Button = new JButton("Submit");

            JPanel centerPanel = new JPanel(new GridLayout(2, 1));
            centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            centerPanel.add(nameLabel);
            centerPanel.add(nameField);

            dialog.add(scoreLabel, BorderLayout.NORTH);
            dialog.add(centerPanel, BorderLayout.CENTER);
            dialog.add(Button, BorderLayout.SOUTH);

            dialog.setVisible(true);
            // Use lambda expression for button action listener
            Button.addActionListener(e -> {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(GamePanel.this, "Please enter your name.");
                } else {
                    Player player = new Player(name, pm.score);
                    Scores.saveScore(player);
                    dialog.dispose();
                    resetAndShowMainScreen();
                }
            });
        } else {
            resetAndShowMainScreen();
        }
    }

    private void resetAndShowMainScreen() {
        // Clear static blocks
        pm.resetGame();

        // Switch to the MainScreen
        cardPanel.remove(GamePanel.this);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(cardPanel);
        if (frame != null) {
            frame.setSize(defaultFrameSize); // Set the frame to the target size
            frame.setLocationRelativeTo(null); // Center the frame on the screen
        }

        cardLayout.show(cardPanel, "MainScreen");

        // Unpause game if paused
        if (KeyHandler.pausePressed) {
            KeyHandler.pausePressed = false;
        }
    }

    // Method to handle back button click using lambda expression
    private void handleBackButtonClick() {
        // Stop the game to pause it
        stopGame();

        // Set the backPressed flag to true and repaint the screen
        backPressed = true;
        repaint();

        // If game is over, save score directly
        if (pm.gameOver) {
            saveScoreDialog();
        } else {
            int option = JOptionPane.showConfirmDialog(
                    GamePanel.this,
                    "Do you want to go back? This will forfeit the current game.",
                    "Confirm Back",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                saveScoreDialog();
            } else if (option == JOptionPane.NO_OPTION) {
                // Resume the game if it was paused
                if (!running) {
                    backPressed = false; // Reset the flag
                    startGame();
                    requestFocusInWindow(); // Request focus back to the game panel
                }
            }
        }
    }
}