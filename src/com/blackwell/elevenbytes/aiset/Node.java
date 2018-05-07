package com.blackwell.elevenbytes.aiset;

import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;
import com.blackwell.elevenbytes.Side;

import java.util.*;

public class Node {
    private Node parent;
    private Map map;
    private List<Side> sides = new ArrayList<>();
    private Set<Node> nodes = new HashSet<>();
    private float TotalAverage = 0f;
    private float[] NodesAverage = new float[Map.FIELD_SIZE];
    public Node(Node parent, Map map, Side side) {
        this.parent = parent;
        this.map = map;
        if (parent != null)
            sides.addAll(parent.sides);
        if (side != null)
            sides.add(side);
    }

    public float getMaxScoreRotation(){
        float max = NodesAverage[0];
        int maxMove = 0;
        for (int i=1; i<Map.FIELD_SIZE; ++i)
            if (max < NodesAverage[i]) { max = NodesAverage[i]; maxMove = i; }

        return maxMove;
    }
    public int getScore() { return map.getScore(); }
    public Side getLastSide() { return sides.get(sides.size()-1); }
    public boolean isNodesEmpty() { return nodes.size() == 0;}
    public List<Side> getSides() { return sides; }

    @Override
    public int hashCode() {
        String mapStr = map.toString();
        String parentMapStr = parent.map.toString();
        int hashCode = 0;
        for (Side side : sides)
            if (side != null) hashCode += (side.ordinal() * 31);
        for(int i=0; i< mapStr.length(); ++i)
            hashCode += ((int) mapStr.charAt(i)) * 31;
        for(int i=0; i< parentMapStr.length(); ++i)
            hashCode += ((int) parentMapStr.charAt(i)) * 31;
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node){
            Node n = (Node) obj;
            if (parent == n.parent &&
                    n.map.toString().equals(map.toString()) &&
                    sides.equals(n.sides)) return true;
        }
        return false;
    }

    public Iterator<Node> iterator() { return nodes.iterator(); }

    private static final int RANDOM_TRIES = 20;
    private StringBuilder mapToStringBuilder(Map m){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<Map.FIELD_SIZE; ++i)
            for(int j=0; j<Map.FIELD_SIZE; ++j)
                sb.append(m.getCellValue(j,i));
        return sb;
    }
    public void setUpNodes(){
        for(int i=0; i<Map.FIELD_SIZE; ++i){
            Map mapPattern = new GameMap(map);
            StringBuilder sb = mapToStringBuilder(mapPattern);
            for(int j=0; j<sb.length(); ++j){
                if (sb.charAt(j) == '0'){
                    sb.replace(j, j+1, "2");
                    Map mapTmp = new GameMap(sb.toString(), mapPattern.getScore());
                    TotalAverage += mapTmp.getScore();
                    Node nodeTmp = new Node(this, mapTmp, Side.values()[i]);
                    nodes.add(nodeTmp);
                }
            }
        }
        TotalAverage /= (float) nodes.size();


        for(int i=0; i<Map.FIELD_SIZE; ++i){
            float sum = 0;
            float count = 0;
            for(Node n : nodes){
                if (n.getLastSide().ordinal() == i){
                   sum += n.map.getScore();
                   count++;
                }
            }
            NodesAverage[i] = sum/count;
        }
    }

}
