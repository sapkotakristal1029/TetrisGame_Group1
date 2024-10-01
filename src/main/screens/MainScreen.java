package main.screens;

import javax.swing.*;
import java.awt.*;
import main.game.GamePanel;
import main.screens.ConfigurationScreen;
import mino.Block;


public class MainScreen extends JPanel {

    private JButton playButton;
    private JButton configButton;
    private JButton highScoresButton;
    private JButton exitButton;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Image backgroundImage;

    // Default frame sizes for each screen
    public int fieldWidth;
    public int fieldHeight;


    private final Dimension defaultFrameSize = new Dimension(700, 350);
//    private final Dimension gameFrameSize = new Dimension(1100, 600);
    private final Dimension configFrameSize = new Dimension(800, 600);
    private final Dimension highScoreFrameSize = new Dimension(1300, 750);

    public MainScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        // Load the background image
        backgroundImage = new ImageIcon("src/main/resources/images/mainmenu.png").getImage();

        this.setLayout(new BorderLayout());

        // Create and style Buttons
        playButton = createStyledButton("Play");
        configButton = createStyledButton("Configuration");
        highScoresButton = createStyledButton("High Scores");
        exitButton = createStyledButton("Exit");

        // Add Action Listeners using lambda expressions
        playButton.addActionListener(e -> startGame());
        configButton.addActionListener(e -> showConfigurationScreen());
        highScoresButton.addActionListener(e -> showHighScoreScreen());
        exitButton.addActionListener(e -> confirmExit());

        // Create Panel for Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin around buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        // Add Buttons to GridBagLayout
        buttonPanel.add(playButton, gbc);
        gbc.gridy++;
        buttonPanel.add(configButton, gbc);
        gbc.gridy++;
        buttonPanel.add(highScoresButton, gbc);
        gbc.gridy++;
        buttonPanel.add(exitButton, gbc);

        // Set background for button panel
        buttonPanel.setOpaque(false); // Make button panel transparent
        this.add(buttonPanel, BorderLayout.CENTER);

        // Set Main Screen properties
        this.setPreferredSize(defaultFrameSize);
    }

    // Override the paintComponent method to draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // Method to create a styled button with fixed size
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24)); // Smaller font size
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1), // Border around the button
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Margins (top, left, bottom, right)
        ));
        button.setPreferredSize(new Dimension(250, 40)); // Fixed size for buttons
        return button;
    }

    // Method to start the game
    private void startGame() {
        fieldWidth = ConfigurationScreen.fieldWidthValue;
        fieldHeight = ConfigurationScreen.fieldHeightValue;

        // Calculate the game panel width and height dynamically
        int calculatedGameWidth = 1100 + (fieldWidth - 10) * Block.SIZE;
        int calculatedGameHeight = 650 + (fieldHeight - 20) * Block.SIZE;
        Dimension dynamicGameFrameSize = new Dimension(calculatedGameWidth, calculatedGameHeight);

        // Adjust the frame size for the GamePanel
        adjustFrameSize(dynamicGameFrameSize);


        GamePanel gamePanel = new GamePanel(cardLayout, cardPanel);
        cardPanel.add(gamePanel, "GamePanel");
        cardLayout.show(cardPanel, "GamePanel");
        gamePanel.startGame();

        // After the game ends, reset frame size when switching back to the MainScreen
        gamePanel.addPropertyChangeListener("gameOver", evt -> {
            if ((boolean) evt.getNewValue()) {
                resetFrameSize();
            }
        });
    }

    // Method to show the configuration screen and adjust frame size
    private void showConfigurationScreen() {
        adjustFrameSize(configFrameSize);
        cardLayout.show(cardPanel, "ConfigurationScreen");
    }

    // Method to show the high score screen and adjust frame size
    private void showHighScoreScreen() {
        adjustFrameSize(highScoreFrameSize);
        cardLayout.show(cardPanel, "HighScoreScreen");
    }

    // Method to adjust the frame size based on the target dimension
    private void adjustFrameSize(Dimension targetSize) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setSize(targetSize); // Set the frame to the target size
            frame.setLocationRelativeTo(null); // Center the frame
        }
    }

    // Method to reset frame size back to the default size after game or screen switch
    private void resetFrameSize() {
        adjustFrameSize(defaultFrameSize);
    }

    // Method to confirm exit using lambda expression
    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(
                MainScreen.this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
