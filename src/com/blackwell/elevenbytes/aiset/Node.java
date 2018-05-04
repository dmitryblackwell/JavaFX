package com.blackwell.elevenbytes.aiset;

import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int move;
    private Map map;
    private List<Node> nodes = new ArrayList<>();

    public Node(Map m, int move){ map = new GameMap(m); this.move = move; }
    public Node(Node n) { this(n.map, n.move); }
    public void setUpNodes() {
        for (int e = 0; e < Map.FIELD_SIZE; ++e) {
            Map mapWithMove = new GameMap(map);
            AI.moveMap(mapWithMove, e);
            String mapStr = mapWithMove.toString();
            for (int i = 0; i < Map.FIELD_SIZE; ++i)
                for (int j = 0; j < Map.FIELD_SIZE; ++j) {
                    int index = i * Map.FIELD_SIZE + j;
                    if ('0' == mapStr.charAt(index)) {
                        StringBuilder sb = new StringBuilder(mapStr);
                        sb.replace(index,index+1, String.valueOf(Map.START_VALUE));
                        Map mapForNode = new GameMap(sb.toString(), map.getScore());

                        nodes.add(new Node(mapForNode, e));
                    }
                }
        }

    }

    public int getNodesLength() { return nodes.size(); }
    public boolean hasNodes() { return nodes.size() != 0; }
    public Node getNode(int i) { return nodes.get(i); }
    public int getMove() { return move; }
    public int getScore(){ return map.getScore(); }
}
