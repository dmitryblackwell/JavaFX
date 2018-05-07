package com.blackwell.elevenbytes.aiset;


import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;
import com.blackwell.elevenbytes.Side;

import java.util.*;


public class AI {
    public static void move(Map map){
        Node root = new Node(null, map, null);
        root.setUpNodes();

        Iterator it = root.iterator();
        while (it.hasNext())
            ((Node) it.next()).setUpNodes();

        fillMovementsSet(root);
        Movement movement = new Movement();
        for(Movement m : set){
            if (m.score > movement.score)
                movement = m;
        }
        moveMap(map, movement);
    }

    static class Movement{
        List<Side> movements = new ArrayList<>();
        float score = 0;
        float count = 0;
        public void addSide(Side s) { movements.add(s); }
        public void addScore(float score) { this.score += score; count++; }
        public float getAverage() { return score/count; }

        public Movement(List<Side> movements, float score) {
            this.movements = movements;
            this.score = score;
            count++;
        }
        public Movement() { }
        public Object[] getSides() { return movements.toArray(); }
        @Override
        public boolean equals(Object obj) {
            if( !(obj instanceof Movement) ) return false;
            return movements.equals(obj);
        }

        @Override
        public int hashCode() {
            int hashCode = 0;
            for(Side s : movements)
                if (s != null) hashCode += (s.ordinal() * 31);
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
        for (Object obj : m.getSides())
            moveMap(map, ((Side) obj).ordinal());
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
