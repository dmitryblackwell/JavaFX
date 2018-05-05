package com.blackwell.elevenbytes.aiset;


import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AI {
    public static void move(Map map){
        // TODO AI move here
    }

    static void moveMap(Map map, int move){
        switch (move){
            case 0: map.moveLeft(); break;
            case 1: map.moveRight(); break;
            case 2: map.moveUp(); break;
            case 3: map.moveDown(); break;
        }
    }
}
