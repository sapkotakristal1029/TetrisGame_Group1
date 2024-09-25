package main.game;

import main.Sound;
import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import main.screens.ConfigurationScreen;

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
    private GamePanel gp;


    // MINO
    Mino currentMino;
    public int MINO_START_X;
    public int MINO_START_Y;
    Mino nextMino;
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

    public PlayManager() {
        initialize();
        // Register the PlayManager in KeyHandler to allow key handling
        KeyHandler.setPlayManager(this);
    }

    private void initialize() {
        gameStartSound = new Sound("src/main/resources/MusicFiles/gameStart.wav");
        gameOverSound = new Sound("src/main/resources/MusicFiles/gameOver.wav");
        gameScoreSound = new Sound("src/main/resources/MusicFiles/gameScore.wav");

        // Play the game start sound
        gameStartSound.play();
        musicPlaying=true;

        System.out.println("GAme Started at "+WIDTH+","+HEIGHT);
        // Main Play Area Frame
        left_x = (GamePanel.WIDTH / 4);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2)- Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 100;
        NEXTMINO_Y = top_y + 300;

        // Initialize game state
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

    private Mino pickMino() {
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i) {
            case 0 -> mino = new Mino_L1();
            case 1 -> mino = new Mino_l2();
            case 2 -> mino = new Mino_Bar();
            case 3 -> mino = new Mino_Square();
            case 4 -> mino = new Mino_T();
            case 5 -> mino = new Mino_Z1();
            case 6 -> mino = new Mino_Z2();
        }
        return mino;
    }

    public void update() {
        if (gameOver) {
            playGameOverSound();
            return;
        }

        // Check if the correct mino is active
        if (!currentMino.active) {
            // If not active, put it in the static blocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // Check if the game is over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                gameOver = true;
            }
            currentMino.deactivating = false;

            // Replace the current mino with next mino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);

            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

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
            currentMino.update();
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
                if (blockCount == WIDTH/Block.SIZE) {
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
        int multiplier = switch (linesDeleted) {
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            default -> 1;
        };

        int singleLineScore = 10;
        score += singleLineScore * linesDeleted * multiplier;


        // Update the total lines count
        lines += linesDeleted;

        // Update level and drop interval based on the rows deleted
        level = Math.min(lines / 5+1, 10); // Increase level for every 50 points, up to level 10
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
        g2.drawRect(x, y, 150, 175);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 50, y + 60);

        // Draw the current mino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // Draw the next mino
        nextMino.draw(g2);

        // Draw static blocks
        for (Block staticBlock : staticBlocks) {
            staticBlock.draw(g2);
        }

        // Draw score frame
        g2.setColor(new Color(128, 0, 128)); // Purple color
        g2.drawRect(x, top_y, 150, 200);
        x += 25;
        y = top_y + 40;
        g2.drawString("LEVEL: " + level, x, y);
        y += 60;
        g2.drawString("LINES: " + lines, x, y);
        y += 60;
        g2.drawString("SCORE: " + score, x, y);

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
            System.out.println("Game Over in draw");

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

        //Unpause game if paused
        if(KeyHandler.pausePressed){
            KeyHandler.pausePressed= false;
        }

        // Reinitialize the minos
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    public int getScore() {
        return score;
    }
}
