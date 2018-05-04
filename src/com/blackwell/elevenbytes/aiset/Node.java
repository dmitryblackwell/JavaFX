package com.blackwell.elevenbytes.aiset;

import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;

import java.util.ArrayList;
import java.util.List;

public class Node {
    Map map;
    List<Map> nodes = new ArrayList<>();

    public Node(Map m){ map = new GameMap(m); }
    public void setUpNodes(){
        String mapStr = map.toString();
        for(int i=0; i<Map.FIELD_SIZE; ++i)
            for(int j=0; j<Map.FIELD_SIZE; ++j) {
                int index = i*Map.FIELD_SIZE + j;
                if ("0".equals(mapStr.charAt(index)))
                    nodes.add(new GameMap(mapStr.substring(0, index-1)
                            + Map.START_VALUE
                            + mapStr.substring(index+1,mapStr.length()), map.getScore()));
            }
    }
    public int getScore(){ return map.getScore(); }
}
