package mino;

import java.awt.*;

public class AbstractMino_Z2 extends AbstractMino {

    public AbstractMino_Z2(){
        create(Color.red);
    }

    public void setXY(int x, int y){
        //0
        //0 0
        //  0


        b[0].x = x;
        b[0].y = y;
        b[1].x = x;
        b[1].y = y-Block.SIZE;
        b[2].x = x+Block.SIZE;
        b[2].y = y;
        b[3].x = x + Block.SIZE;
        b[3].y = y +Block.SIZE;

    }
    public void getDirection1(){
        //0
        //0 0
        //  0
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y-Block.SIZE;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y +Block.SIZE;

        updateXY(1);

    }
    public void getDirection2(){
        //  0 0
        //0 0
        //
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x -Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;


        updateXY(2);
    }
    public void getDirection3(){
        getDirection1();

    }
    public void getDirection4(){
        getDirection2();
    }
}
