package com.blackwell.elevenbytes;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class GameMap implements Map {

    /** map that present a two dimensional array of ints
     *  if cell equals zero it is empty
     */
    private int[][] map = new int[FIELD_SIZE][FIELD_SIZE];

    private enum Side { LEFT, RIGHT, UP, DOWN}
    private int score=0;

    /*_____________________________INTERFACE_START_____________________________*/
    GameMap(){
        for (int[] row : map)
            Arrays.fill(row,0);

        addCell();
    }

    public GameMap(Map map){
        this();
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=0; j<FIELD_SIZE; ++j)
                this.map[i][j] = map.getCellValue(j,i);
        score = map.getScore();
    }
    public GameMap(String map, int score){
        this();
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=0; j<FIELD_SIZE; ++j)
                this.map[i][j] = (int) map.charAt(i*FIELD_SIZE + j);
        this.score = score;
    }

    @Override
    public int getCellValue(int x,int y){
        return map[y][x];
    }

    @Override
    public int getScore() {
        return score;
    }


    @Override
    public void moveLeft(){ move(Side.LEFT);}
    @Override
    public void moveRight(){ move(Side.RIGHT); }
    @Override
    public void moveUp(){ move(Side.UP); }
    @Override
    public void moveDown(){ move(Side.DOWN); }


    @Override
    public void saveGame(String bestScore){
        try {
            FileWriter file = new FileWriter(SAVES_FILE);
            PrintWriter writer = new PrintWriter(file);
            // elements that need to save: best score, current score, 16 cells.
            StringBuilder out = new StringBuilder();
            out.append(bestScore).append(" ").append(score);
            for(int i = 0; i< Map.FIELD_SIZE; ++i)
                for (int j = 0; j < Map.FIELD_SIZE; ++j)
                    out.append(" ").append(map[j][i]);
            //byte[] bytesOut = out.toString().getBytes();
            writer.print(out.toString());
            writer.close();
            file.close();
        } catch (FileNotFoundException e) {
            System.err.println("File "+ SAVES_FILE + " not found.");
        } catch (IOException e) {
            System.err.println("Unable write file ");
        }
    }

    @Override
    public String loadGame(){
        try {
            FileReader file = new FileReader(SAVES_FILE);
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            String[] data = line.split(" ");
            score = Integer.parseInt(data[1]);
            for(int i=0;i<FIELD_SIZE;++i)
                for (int j=0; j<FIELD_SIZE; ++j)
                    map[j][i] = Integer.parseInt(data[i*FIELD_SIZE + j + 2]);
            return data[0];
        } catch (FileNotFoundException e) {
            System.err.println("File "+ SAVES_FILE + " not found.");
        } catch (IOException e) {
            System.err.println("Unable write file.");
        }
        return "0";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<FIELD_SIZE; ++i)
            for(int j=0;j<FIELD_SIZE; ++j)
                sb.append(map[i][j]);
        return sb.toString();
    }

    /*_____________________________INTERFACE_END_______________________________*/

    /**
     * Make a move to special side
     * First it is shifting all elements together,
     * Also this method is creating new cell
     * only if something change in the map,
     * like elements were moved or merge
     *
     * Example of moving to Side s == LEFT
     * so if our row looks this: 0 2 0 2
     * it is becoming this: 0 2 2 0
     * after it is merging two elements: 0 4 0 0
     * next it is shifting them again: 4 0 0 0
     *
     * @param s side where we moving to
     */
    private void move(Side s){
        String mapBefore = Arrays.deepToString(map);

        shift(s);
        merge(s);
        shift(s);

        String mapAfter = Arrays.deepToString(map);

        if (!mapBefore.equals(mapAfter))
            addCell();
    }

    /**
     * This method merge first pair of same cells.
     * It is comes from Side s to opposite side and
     * checking for pair of same values.
     * After pair is found it is making a merge and break from the loop
     *
     * @param s Side to witch we have to merge all cells.
     */
    private void merge(Side s){
        for (int i = 0; i < FIELD_SIZE; ++i) {
            switch (s) {
                case LEFT:
                    for (int j = 0; j < FIELD_SIZE - 1; ++j)
                        if (makeMerge(j, i, +1, 0)) break;
                    break;
                case RIGHT:
                    for (int j = FIELD_SIZE - 1; j > 0; --j)
                        if (makeMerge(j, i, -1, 0)) break;
                    break;
                case UP:
                    for (int j = 0; j < FIELD_SIZE - 1; ++j)
                        if (makeMerge(i, j, 0, +1)) break;
                    break;
                case DOWN:
                    for (int j = FIELD_SIZE - 1; j > 0; --j)
                        if (makeMerge(i, j, 0, -1)) break;
                    break;
            }
        }
    }

    /**
     * Swap two nearby valuables on the map, but only if first one zero.
     * For example we got first row setting up like this 1 0 0 3
     * First of all it is changing two zeros and row looks still the same
     * Second one it is chane 0 and 3, so row looks like this 1 0 3 0
     *
     * @param i y of the point on the map
     * @param j x of the point
     * @param vX direction by x, can be -1:Left, 0:Still, +1:Right
     * @param vY dirextion by y, can be -1:Up, 0:Still, +1:Down
     */
    private void swapTwoPoints(int i, int j, int vX, int vY){
        int tmp = map[i][j];
        map[i][j] = map[i+vY][j+vX];
        map[i+vY][j+vX] = tmp;
    }

    /**
     * This method moving all elements to one side
     * It is running FIELD_SIZE times (in this case 4) so all elements
     * even from other side moves to one.
     * It depend where to move, but technically it comes from Side s to opposite
     * and change to values if first one is zerro
     *
     * For example:
     * If we need move all elements to the left side and we have this row: 0 2 0 8
     * First we change 0 and 2, so row became 2 0 0 8
     * After it is changing two zeros, so row looks same for us
     * Next it is changing 0 and 8: 2 0 8 0
     * After it first iteration is done and we come to next one
     * On second phase it is changing 0 and 8: 2 8 0 8
     * Yeeah, baby! We done here.
     * Actually not.
     * We run this FIELD_SIZE time in case our row looks like this: 0 0 0 4
     *
     * @param s where to shift all elements
     */
    private void shift(Side s){
        for(int times=0; times<FIELD_SIZE; ++times) {
            for (int i = 0; i < FIELD_SIZE; ++i) {
                switch (s) {
                    case LEFT:
                        for (int j = 0; j < FIELD_SIZE - 1; ++j)
                            if (map[i][j] == 0)
                                swapTwoPoints(i, j, +1, 0);
                        break;
                    case RIGHT:
                        for (int j = FIELD_SIZE - 1; j > 0; --j)
                            if (map[i][j] == 0)
                                swapTwoPoints(i, j, -1, 0);
                        break;
                    case UP:
                        for (int j = 0; j < FIELD_SIZE - 1; ++j)
                            if (map[j][i] == 0)
                                swapTwoPoints(j, i, 0, +1);
                        break;
                    case DOWN:
                        for (int j = FIELD_SIZE - 1; j > 0; --j)
                            if (map[j][i] == 0)
                                swapTwoPoints(j, i, 0, -1);
                        break;
                }
            }
        }
    }

    /**
     *  This method check if it is possible to merge cell on x,y to the vector of vX,vY
     *  If it is possible, than it merges it and return true
     *  and if it is not, that it do nothing and return false
     *
     * @param x x of point where you need to check if merge is possible
     * @param y same for y
     * @param vX velocityX. Vector on the x line where to go. Can be 1,-1 or 0
     * @param vY velocityY. Same for vX
     * @return true if movement is done
     */
    private boolean makeMerge(int x, int y, int vX, int vY){
        // if cell is not zero AND if next cell is same
        if(map[y][x] != 0 && map[y+vY][x+vX] == map[y][x]){
            map[y+vY][x+vX] *= 2;
            map[y][x] = 0;
            score+=map[y+vY][x+vX];
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
