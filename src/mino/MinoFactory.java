package mino;

import java.util.Random;

public class MinoFactory {

    public static AbstractMino createMino() {
        int randomNumber = new Random().nextInt(7);
        return createMinoByType(randomNumber);
    }

    public static AbstractMino createMinoByType(int type) {
        switch (type) {
            case 0: return new AbstractMino_L1();
            case 1: return new AbstractMino_l2();
            case 2: return new AbstractMino_Bar();
            case 3: return new AbstractMino_Square();
            case 4: return new AbstractMino_T();
            case 5: return new AbstractMino_Z1();
            case 6: return new AbstractMino_Z2();
            default: throw new IllegalArgumentException("Unknown Mino type: " + type);
        }
    }
}
