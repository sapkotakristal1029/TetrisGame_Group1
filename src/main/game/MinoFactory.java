package main.game;

import mino.*;

import java.util.Random;

public class MinoFactory {

    public static Mino createMino() {
        int randomNumber = new Random().nextInt(7);
        return createMinoByType(randomNumber);
    }

    public static Mino createMinoByType(int type) {
        switch (type) {
            case 0: return new Mino_L1();
            case 1: return new Mino_l2();
            case 2: return new Mino_Bar();
            case 3: return new Mino_Square();
            case 4: return new Mino_T();
            case 5: return new Mino_Z1();
            case 6: return new Mino_Z2();
            default: throw new IllegalArgumentException("Unknown Mino type: " + type);
        }
    }
}
