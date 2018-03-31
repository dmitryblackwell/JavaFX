package com.blackwell.elevenbytes;

public interface Map {
    /** field size, in this case 4x4 */
    int FIELD_SIZE = 4;

    /**
     * @param x position in row of the cell
     * @param y row of the cell
     * @return value of this cell
     */
    int getCellValue(int x, int y);

    /**
     * @return score of the player
     */
    int getScore();

    /** Moving all elements to left one side */
    void moveLeft();
    void moveRight();
    void moveUp();
    void moveDown();
}
