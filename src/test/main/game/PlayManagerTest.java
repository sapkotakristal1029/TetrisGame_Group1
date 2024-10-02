package main.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayManagerTest {

    PlayManager playManager;

    @BeforeEach
    void setUp() {
        playManager = PlayManager.getInstance();
        playManager.resetGame(); // Ensure a clean state before each test
    }

    @AfterEach
    void tearDown() {
        playManager = null;
    }

    @Test
    void testGetInstance() {
        PlayManager instance1 = PlayManager.getInstance();
        PlayManager instance2 = PlayManager.getInstance();
        assertSame(instance1, instance2, "PlayManager should be a singleton instance");
    }

    @Test
    void testToggleMusic() {
        boolean initialMusicState = playManager.musicPlaying;
        playManager.toggleMusic();
        assertNotEquals(initialMusicState, playManager.musicPlaying, "Music state should toggle when toggleMusic() is called");
    }

    @Test
    void testToggleSoundEffects() {
        boolean initialSoundEffectsState = playManager.soundEffectsOn;
        playManager.toggleSoundEffects();
        assertNotEquals(initialSoundEffectsState, playManager.soundEffectsOn, "Sound effects state should toggle when toggleSoundEffects() is called");
    }

    @Test
    void testPlayScoreSound() {
        playManager.soundEffectsOn = true;
        playManager.playScoreSound();

    }

    @Test
    void testUpdateGameState() {
        playManager.update();
        assertFalse(playManager.gameOver, "Game should not be over initially");
    }

    @Test
    void testResetGame() {
        playManager.score = 100;
        playManager.lines = 5;
        playManager.gameOver = true;

        playManager.resetGame();

        assertEquals(0, playManager.score, "Score should be reset to 0");
        assertEquals(0, playManager.lines, "Lines should be reset to 0");
        assertFalse(playManager.gameOver, "Game over state should be false");
    }
}
