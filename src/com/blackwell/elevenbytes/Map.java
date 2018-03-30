package com.blackwell.elevenbytes;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Map {
    public static final int FIELD_SIZE = 4; // field size, in this case 4x4

    private static final int START_VALUE = 2; // value of generated cell
    private int[][] map = new int[FIELD_SIZE][FIELD_SIZE];
    private int score=0;
    public Map(){
        for (int[] row : map)
            Arrays.fill(row,0);

        addCell();
    }
    public int getCellValue(int x,int y){
        int value = map[y][x]; // Make a copy
        return value;
    }

    public int getScore() {
        return score;
    }

    // This code is fucking shitty
    // TODO refactor this code
    // TODO create interface
    // TODO change logic(order) of merge
    public void moveLeft(){
        shiftLeft();
        mergeLeft();
        mergeLeft();
        addCell();
    }

    public void moveRight(){
        shiftRight();
        mergeRight();
        mergeRight();
        addCell();
    }

    public void moveUp(){
        shifUp();
        mergeUp();
        mergeUp();
        addCell();
    }
    public void moveDown(){
        shiftDown();
        mergeDown();
        mergeDown();
        addCell();
    }
    private void mergeRight(){
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=0; j<FIELD_SIZE-1; ++j)
                if (checkForMovement(j,i,+1,0))
                    break;

    }
    private void mergeLeft(){
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=FIELD_SIZE-1; j>0; --j)
                if (checkForMovement(j,i,-1,0))
                    break;

    }
    private void mergeUp(){
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=1; j<FIELD_SIZE; ++j)
                if (checkForMovement(i,j,0,-1))
                    break;
    }
    private void mergeDown(){
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=0; j<FIELD_SIZE-1; ++j)
                if (checkForMovement(i,j,0,1))
                    break;
    }
    private void shiftDown(){
        for(int i=0; i<FIELD_SIZE; ++i){
            int[] tmp = new int[FIELD_SIZE];
            Arrays.fill(tmp,0);
            int carriage=FIELD_SIZE-1;
            for(int j=FIELD_SIZE-1;j>=0;--j) {
                if (map[j][i] != 0){
                    tmp[carriage] = map[j][i];
                    carriage--;
                }
            }
            for(int j=0;j<FIELD_SIZE; ++j)
                map[j][i]=tmp[j];
        }
    }

    private void shifUp(){
        for(int i=0; i<FIELD_SIZE; ++i){
            int[] tmp = new int[FIELD_SIZE];
            Arrays.fill(tmp,0);
            int carriage=0;
            for(int j=0;j<FIELD_SIZE;++j) {
                if (map[j][i] != 0){
                    tmp[carriage] = map[j][i];
                    carriage++;
                }
            }
            for(int j=0;j<FIELD_SIZE; ++j)
                map[j][i]=tmp[j];
        }
    }

    private void shiftLeft(){
        for(int i=0; i<FIELD_SIZE; ++i){
            int[] tmp = new int[FIELD_SIZE];
            Arrays.fill(tmp,0);
            int carriage=0;
            for(int j=0;j<FIELD_SIZE;++j) {
                if (map[i][j] != 0){
                    tmp[carriage] = map[i][j];
                    carriage++;
                }
            }
            System.arraycopy(tmp,0,map[i],0,FIELD_SIZE);
        }
    }
    private void shiftRight(){
        for(int i=0; i<FIELD_SIZE; ++i){
            int[] tmp = new int[FIELD_SIZE];
            Arrays.fill(tmp,0);
            int carriage=FIELD_SIZE-1;
            for(int j=FIELD_SIZE-1;j>=0;--j) {
                if (map[i][j] != 0){
                    tmp[carriage] = map[i][j];
                    carriage--;
                }
            }
            System.arraycopy(tmp,0,map[i],0,FIELD_SIZE);
        }
    }


    /**
     *  This method check if it is possible to move cell on x,y to the vector of vX,vY
     *  If it is possible, than it moves it and return true
     *  and if it is not, that it do nothing and return false
     *
     * @param x x of point where you need to check if movement is possible
     * @param y same for y
     * @param vX velocityX. Vector on the x line where to go. Can be 1,-1 or 0
     * @param vY velocityY. Same for vX
     * @return true if movement is done
     */
    private boolean checkForMovement(int x,int y,int vX, int vY){
        // if cell is not zero AND if next cell is same
        if(map[y][x] != 0 && map[y+vY][x+vX] == map[y][x]){
            map[y+vY][x+vX] *= 2;
            map[y][x] = 0;
            score+=map[y+vY][x+vX]; // TODO change score system
            return true;
        }
        return false;
    }

    /**
     * Get random free point on map and after
     * fill it with START_VALUE, that equals 2 for this game
     */
    private void addCell(){
        Point p = getRandomPoint();
        map[p.y][p.x] = START_VALUE;
        score += START_VALUE;
    }

    /**
     * @return random free point on map
     */
    private Point getRandomPoint(){
        Random R = new Random();
        int randX, randY;
        do{
            randX = R.nextInt(FIELD_SIZE);
            randY = R.nextInt(FIELD_SIZE);
        }while (map[randY][randX] != 0);
        return new Point(randX,randY);
    }
}
