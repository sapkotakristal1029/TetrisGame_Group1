package mino;

import java.awt.*;

public interface Mino {
    void create(Color c);
    void updateXY(int direction);
    void draw(Graphics2D g2);
    void checkMovementCollision();
    void checkRotationCollision();
    void checkStaticBlocksCollision();
    void update();
    void deactivating();
}
