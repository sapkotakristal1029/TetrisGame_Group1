package mino;

import java.awt.*;

public class AbstractMino_Square extends AbstractMino {
    public AbstractMino_Square(){
        create(Color.yellow);

    }
    public void setXY(int x, int y){
        //0 0
        //0 0

        b[0].x = x;
        b[0].y = y;
        b[1].x = x;
        b[1].y = y + Block.SIZE;
        b[2].x = x + Block.SIZE;
        b[2].y = y;
        b[3].x = x + Block.SIZE;
        b[3].y = y +Block.SIZE;

    }
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}


}
