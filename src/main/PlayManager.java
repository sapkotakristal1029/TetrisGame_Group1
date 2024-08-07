package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    // MAin play area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //MINO
    Mino currentMino;

    final int MINO_START_X;
    final int MINO_START_Y;

    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;

    public static ArrayList<Block> staticBlocks = new ArrayList<>();


    //Mino drop in every 60 frames
    public static int dropInterval = 60;
    boolean gameOver;


    //Effect of deleting line
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    //Score
    int level = 1;
    int lines;
    int score;



    public PlayManager(){

        //Main Play Area Frame
        left_x = (GamePanel.WIDTH/2)-(WIDTH/2);
        right_x = left_x+WIDTH;
        top_y= 50;
        bottom_y = top_y+HEIGHT;

        MINO_START_X = left_x + (WIDTH/2 - Block.SIZE);
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x+175;
        NEXTMINO_Y = top_y+ 500;


        //Set the starting Mino
        currentMino = pickMino();
//        currentMino = new Mino_Z1();
        currentMino.setXY(MINO_START_X,MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);


    }


    private  Mino pickMino(){
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i){
            case 0-> mino = new Mino_L1();
            case 1-> mino = new Mino_l2();
            case 2-> mino = new Mino_Bar();
            case 3-> mino = new Mino_Square();
            case 4-> mino = new Mino_T();
            case 5-> mino = new Mino_Z1();
            case 6-> mino = new Mino_Z2();

        }
        return mino;



    }

    public void update(){
        //TO check if correct mino is active
        if(!currentMino.active){
            //if not active put it in the static blocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //Check if the game is over
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                //This mean there is not any place to fall the mino. So it is game over
                gameOver = true;

            }
            currentMino.deactivating = false;


            //replace the current mino with next mino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X,MINO_START_Y);

            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            //Check if the line id full and delete that line
            checkDelete();


        }

        currentMino.update();

    }
    private void checkDelete(){

        int x = left_x;
        int y = top_y;
        int blockCount = 0;

        int lineCount = 0;


        while (x < right_x && y < bottom_y){
            for (Block staticBlock : staticBlocks) {
                if (staticBlock.x == x && staticBlock.y == y) {
                    //increase the count of  static block
                    blockCount++;

                }
            }
            x += Block.SIZE;

            if ( x == right_x){

                //if it is full we can delete the line
                if(blockCount == 12){
                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size()-1; i > -1; i--){
                        if (staticBlocks.get(i). y == y){
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount+=1;
                    lines ++;

                    //DROP SPEED
                    //If the line score hot a certain number, increase the drop speed
                    //1 is fastest;
                    if (lines % 10 == 0 && dropInterval > 1){
                        level ++;
                        if (dropInterval>10){
                            dropInterval -=10;
                        }else{
                            dropInterval =10;
                        }
                    }


                    //a line has been deleted so need to slide the old block down
                    for (Block staticBlock : staticBlocks) {
                        //if a block is above the current y, move it down by the block size
                        if (staticBlock.y < y) {
                            staticBlock.y += Block.SIZE;
                        }
                    }

                }
                blockCount = 0;
                x = left_x;
                y+=Block.SIZE;

            }
        }
        if(lineCount > 0){
            int singleLineScore = 10;
            score += singleLineScore*lineCount;

        }
    }
    public void draw(Graphics2D g2){

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4,top_y-4,WIDTH+8,HEIGHT+8);

        //Draw a mino Frame

        int x = right_x+100;
        int y = bottom_y-200;
        g2.drawRect(x,y,200,200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);


        //Draw the currentMino

        if (currentMino != null){
            currentMino.draw(g2);

        }

        //Draw next Mino
        nextMino.draw(g2);

        //Draw Static Blocks;
        for (Block staticBlock : staticBlocks) {
            staticBlock.draw(g2);
        }

        //Draw Score frame
        g2.drawRect(x, top_y,250, 300);
        x+=40;
        y = top_y +90;
        g2.drawString("LEVEL: "+level, x, y);y+=70;
        g2.drawString("LINES: "+lines, x, y);y+=70;

        g2.drawString("SCORE: "+ score, x, y);



        if(effectCounterOn){
            effectCounter++;

            g2.setColor(Color.red);
            for (Integer integer : effectY) {
                g2.fillRect(left_x, integer, WIDTH, Block.SIZE);
            }

            if(effectCounter == 10){
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();

            }
        }


        //Game Over
        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver){
            x = left_x+25;
            y = top_y+320;
            g2.drawString("Game Over", x, y);

        }

        //Draw Pause
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));

        if(KeyHandler.pausePressed){
            x = left_x+90;
            y = top_y+320;
            g2.drawString("Paused", x, y);
        }
    }
}
