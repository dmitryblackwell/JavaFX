package com.blackwell.elevenbytes.aiset;


import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;

public class AI {
    public static void move(Map map){
        int move1 = 0;
        int move2 = 0;
        int maxScore = 0;
        for(int i=0; i<Map.FIELD_SIZE; ++i){
            Map map1 = new GameMap(map);
            moveMap(map1, i);
            for (int j=0; j<Map.FIELD_SIZE; ++j){
                Map map2 = new GameMap(map1);
                moveMap(map2, j);
                if(maxScore < map2.getScore()){
                    move1 = i;
                    move2 = j;
                    maxScore = map2.getScore();
                }
            }
        }

        moveMap(map,move1);
        moveMap(map,move2);

    }

    private static void moveMap(Map map, int move){
        switch (move){
            case 0: map.moveLeft(); break;
            case 1: map.moveRight(); break;
            case 2: map.moveUp(); break;
            case 3: map.moveDown(); break;
        }
    }
}
