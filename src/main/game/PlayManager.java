package main.game;

import main.Sound;
import main.scoresRecord.Scores;
import mino.AbstractMino;
import mino.Block;
import mino.MinoFactory;
import main.screens.ConfigurationScreen;

import java.awt.*;
import java.util.ArrayList;

public class PlayManager {

    public static Boolean secondPlayerSelected = true;

    public static Sound gameStartSound;
    public static Sound gameOverSound;
    public static Sound gameScoreSound;
    public boolean musicPlaying = true; // Track music state
    public boolean soundEffectsOn = true; // Track sound effects state

    public int WIDTH = ConfigurationScreen.fieldWidthValue * Block.SIZE;
    public int HEIGHT = ConfigurationScreen.fieldHeightValue * Block.SIZE;

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // MINO
    AbstractMino currentAbstractMino;
    public int MINO_START_X;
    public int MINO_START_Y;
    AbstractMino nextAbstractMino;
    public int NEXTMINO_X;
    public int NEXTMINO_Y;

    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // Mino drop in every 60 frames
    public static int dropInterval = 60;
    boolean gameOver;

    // Effect and Animation of deleting line
    boolean effectCounterOn;
    ArrayList<Integer> effectY = new ArrayList<>();

    // Score
    int level = ConfigurationScreen.gameLevelValue;
    int lines;
    int score;

    // Animation-related variables
    private static final int ANIMATION_STEPS = 20; // Number of steps for the fade-out animation
    private int animationStep = 0;
    private boolean isAnimating = false;

    private static PlayManager instance;

    private PlayManager() {
        initialize();
        // Register the PlayManager in KeyHandler to allow key handling
        KeyHandler.setPlayManager(this);
        ConfigurationScreen.setPlayManager(this);
    }

    public static synchronized PlayManager getInstance() {
        if (instance == null) {
            instance = new PlayManager();
        }
        return instance;
    }

    private void initialize() {
        gameStartSound = new Sound("src/main/resources/MusicFiles/gameStart.wav");
        gameOverSound = new Sound("src/main/resources/MusicFiles/gameOver.wav");
        gameScoreSound = new Sound("src/main/resources/MusicFiles/gameScore.wav");

        // Play the game start sound
        gameStartSound.play();
        musicPlaying = true;

        System.out.println("Game Started at " + WIDTH + "," + HEIGHT);
        // Main Play Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 105;
        NEXTMINO_Y = top_y + 300;

        // Initialize game state
        resetGame();
    }

    // New method to update field size dynamically
    public void refreshFieldSize() {
        // Update field dimensions using the configuration values
        this.WIDTH = ConfigurationScreen.fieldWidthValue * Block.SIZE;
        this.HEIGHT = ConfigurationScreen.fieldHeightValue * Block.SIZE;

        // Update boundary positions
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        // Update the starting positions of the minos
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 105;
        NEXTMINO_Y = top_y + 300;

        System.out.println("Field size updated to: " + ConfigurationScreen.fieldWidthValue + "x" + ConfigurationScreen.fieldHeightValue);

        // Reset the game to apply the new size
        resetGame();
    }

    public void toggleMusic() {
        if (musicPlaying) {
            gameStartSound.stop();
        } else {
            gameStartSound.play();
        }
        musicPlaying = !musicPlaying;
    }

    // Method to toggle sound effects
    public void toggleSoundEffects() {
        if (soundEffectsOn) {
            gameOverSound.stop();
            gameScoreSound.stop();
        }
        soundEffectsOn = !soundEffectsOn;
    }

    public void playScoreSound() {
        if (soundEffectsOn) {
            gameScoreSound.play();
        }
    }

    public void playGameOverSound() {
        if (soundEffectsOn) {
            gameOverSound.play();
        }
    }

    private AbstractMino pickMino() {
        return MinoFactory.createMino();
    }

    public void update() {
        if (gameOver) {
            playGameOverSound();
            return;
        }

        // Check if the correct mino is active
        if (!currentAbstractMino.active) {
            // If not active, put it in the static blocks
            staticBlocks.add(currentAbstractMino.b[0]);
            staticBlocks.add(currentAbstractMino.b[1]);
            staticBlocks.add(currentAbstractMino.b[2]);
            staticBlocks.add(currentAbstractMino.b[3]);

            // Check if the game is over
            if (currentAbstractMino.b[0].x == MINO_START_X && currentAbstractMino.b[0].y == MINO_START_Y) {
                gameOver = true;
            }
            currentAbstractMino.deactivating = false;

            // Replace the current mino with next mino
            currentAbstractMino = nextAbstractMino;
            currentAbstractMino.setXY(MINO_START_X, MINO_START_Y);

            nextAbstractMino = pickMino();
            nextAbstractMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // Check if the line is full and delete that line
            checkDelete();
        }

        if (isAnimating) {
            animationStep++;
            if (animationStep > ANIMATION_STEPS) {
                isAnimating = false;
                animationStep = 0;
                finalizeLineDeletion(); // Finalize the line deletion after the animation
            }
        } else {
            currentAbstractMino.update();
        }
    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;

        while (x < right_x && y < bottom_y) {
            for (Block staticBlock : staticBlocks) {
                if (staticBlock.x == x && staticBlock.y == y) {
                    blockCount++;
                }
            }
            x += Block.SIZE;

            if (x == right_x) {
                if (blockCount == WIDTH / Block.SIZE) {
                    effectCounterOn = true;
                    effectY.add(y);
                    startAnimation(); // Start the fade-out animation
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
    }

    private void startAnimation() {
        isAnimating = true;
        animationStep = 0;
    }

    private void finalizeLineDeletion() {
        playScoreSound();
        int linesDeleted = effectY.size();  // Number of lines deleted at once

        for (Integer y : effectY) {
            for (int i = staticBlocks.size() - 1; i > -1; i--) {
                if (staticBlocks.get(i).y == y) {
                    staticBlocks.remove(i);
                }
            }
            for (Block staticBlock : staticBlocks) {
                if (staticBlock.y < y) {
                    staticBlock.y += Block.SIZE;
                }
            }
        }
        effectY.clear();

        // Update score based on the number of lines deleted at once
        int scoreIncrement = switch (linesDeleted) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 600;
            case 4 -> 1000;
            default -> 0;
        };

        score += scoreIncrement;

        // Update the total lines count
        lines += linesDeleted;

        // Update level and drop interval based on the rows deleted
        level = Math.min(lines / 10 + 1, 10); // Increase level for every 10 rows deleted, up to level 10
        dropInterval = Math.max(60 - (level - 1) * 6, 10); // Faster drop with higher levels, minimum interval of 10
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw a mino frame
        int x = right_x + 50;
        int y = top_y + 200;
        g2.setColor(new Color(128, 0, 128)); // Purple color
        g2.drawRect(x, y, 170, 175);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 50, y + 40);

        // Draw the current mino
        if (currentAbstractMino != null) {
            currentAbstractMino.draw(g2);
        }

        // Draw the next mino
        nextAbstractMino.draw(g2);

        // Draw static blocks
        for (Block staticBlock : staticBlocks) {
            staticBlock.draw(g2);
        }

        // Draw score frame
        g2.setColor(new Color(128, 0, 128)); // Purple color
        g2.drawRect(x, top_y, 170, 200);
        x += 25;
        y = top_y + 30;
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("START LEVEL: " + ConfigurationScreen.gameLevelValue, x, y);
        y += 30;
        g2.drawString("LEVEL: " + level, x, y);
        y += 30;

        g2.drawString("LINES: " + lines, x, y);
        y += 30;
        g2.drawString("SCORE: " + score, x, y);
        y += 30;
        g2.drawString("Score", x, y);
        y += 15;
        g2.drawString("to beat: " + Scores.lastScore(), x, y);

        if (effectCounterOn) {
            g2.setColor(new Color(255, 0, 0, 255 - (animationStep * 10))); // Red with decreasing opacity
            for (Integer yCoord : effectY) {
                g2.fillRect(left_x, yCoord, WIDTH, Block.SIZE);
            }
        }

        // Draw game over
        if (gameOver) {
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.setColor(Color.red);
            g2.drawString("Game Over", 540, 360);
        }

        // Draw pause
        if (KeyHandler.pausePressed) {
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.setColor(Color.yellow);
            g2.drawString("Paused", 540, 360);
            g2.setFont(new Font("Arial", Font.BOLD, 25));
            g2.drawString("Press P to continue", 510, 400);
        }
    }

    public void resetGame() {
        // Reset all game variables
        staticBlocks.clear();
        effectY.clear();

        score = 0;
        lines = 0;
        level = ConfigurationScreen.gameLevelValue;
        dropInterval = Math.max(59 - (level) * 6, 10);

        effectCounterOn = false;
        isAnimating = false;
        animationStep = 0;
        gameOver = false;

        // Unpause game if paused
        if (KeyHandler.pausePressed) {
            KeyHandler.pausePressed = false;
        }

        // Reinitialize the minos
        currentAbstractMino = pickMino();
        currentAbstractMino.setXY(MINO_START_X, MINO_START_Y);

        nextAbstractMino = pickMino();
        nextAbstractMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
}
