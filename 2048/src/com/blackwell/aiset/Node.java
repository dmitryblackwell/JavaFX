package com.blackwell.aiset;

import com.blackwell.GameMap;
import com.blackwell.Map;
import com.blackwell.Side;

import java.util.*;

public class Node {
    private Map map;
    private String sides;
    private Set<Node> nodes = new HashSet<>();
    public Node(Map map, String sides) {
        this.map = map;
        this.sides = sides;
    }
    public int getScore() { return map.getScore(); }
    public char getLastSide() { return sides.charAt(sides.length()-1); }
    public boolean isNodesEmpty() { return nodes.size() == 0;}
    public String getSides() { return sides; }

    @Override
    public int hashCode() {
        String mapStr = map.toString();
        int hashCode = 0;
        for (int i=0; i<sides.length(); ++i)
            hashCode += ( ((int) sides.charAt(i)) * 31);
        for(int i=0; i< mapStr.length(); ++i)
            hashCode += ((int) mapStr.charAt(i)) * 31;
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node){
            Node n = (Node) obj;
            if (n.map.toString().equals(map.toString()) &&
                    sides.equals(n.sides)) return true;
        }
        return false;
    }

    public Iterator<Node> iterator() { return nodes.iterator(); }
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
                    Node nodeTmp = new Node(mapTmp, sides+i);
                    nodes.add(nodeTmp);
                }
            }
        }
    }

}
