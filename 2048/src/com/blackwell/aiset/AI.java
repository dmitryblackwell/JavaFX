package com.blackwell.aiset;


import com.blackwell.GameMap;
import com.blackwell.Map;

import java.util.*;


public class AI {
    static int logCount = 0;
    public static void log() { System.out.println(String.valueOf(logCount++)); }
    public static void move(Map map){
        log();
        Node root = new Node( map,"");
        root.setUpNodes();
        log();

        log();
        Iterator it = root.iterator();
        while (it.hasNext())
            ((Node) it.next()).setUpNodes();
        log();

        fillMovementsSet(root);
        Movement movement = new Movement();
        for(Movement m : set){
            if (m.score > movement.score)
                movement = m;
        }
        moveMap(map, movement);
    }

    static class Movement{
        String movements = "";
        float score = 0;
        float count = 0;
        public void addSide(String s) { movements += s; }
        public void addScore(float score) { this.score += score; count++; }
        public float getAverage() { return score/count; }

        public Movement(String movements, float score) {
            this.movements = movements;
            this.score = score;
            count++;
        }

        public Movement() { }
        public String getSides() { return movements; }
        @Override
        public boolean equals(Object obj) {
            if( !(obj instanceof Movement) ) return false;
            return movements.equals(obj);
        }

        @Override
        public int hashCode() {
            int hashCode = 0;
            for(int i=0; i<movements.length(); ++i)
                hashCode += ( ((int) movements.charAt(i)) * 31);
            return hashCode;
        }
    }

    static class MovementsSet extends HashSet<Movement>{
        @Override
        public boolean add(Movement movement) {
            for (Movement m : this){
                if (m.equals(movement)) {
                    m.addScore(movement.score);
                    return true;
                }
            }
            super.add(movement);

            return true;
        }
    }

    private static MovementsSet set = new MovementsSet();
    private static void fillMovementsSet(Node node){
        Iterator it = node.iterator();
        while (it.hasNext()){
            Node n = (Node) it.next();
            if (n.isNodesEmpty())
                set.add(new Movement(n.getSides(), n.getScore()));
            else
                fillMovementsSet(n);
        }
    }



    static void moveMap(Map map, Movement m){
        String sides = m.getSides();
        for (int i=0; i<sides.length(); ++i)
            moveMap(map, (int) sides.charAt(i));
    }

    static void moveMap(Map map, int move){
        switch (move){
            case 0: map.moveLeft(); break;
            case 1: map.moveRight(); break;
            case 2: map.moveUp(); break;
            case 3: map.moveDown(); break;
        }
    }


    private static void dummyMove(Map map){
        int maxScore = 0;
        int m1 = 0, m2 = 0;
        for(int i=0; i<3; ++i){
            Map move1 = new GameMap(map);
            moveMap(move1, i);
            for(int j=0; j<3; ++j){
                Map move2= new GameMap(move1);
                moveMap(move2, j);
                if(move2.getScore() > maxScore){
                    m1 = i;
                    m2 = j;
                    maxScore = move2.getScore();
                }
            }
        }
        String before = map.toString();
        moveMap(map,m1);
        moveMap(map,m2);

        if (map.toString().equals(before)){
            map.moveUp();
            map.moveDown();
        }
    }


}
