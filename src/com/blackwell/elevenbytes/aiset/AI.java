package com.blackwell.elevenbytes.aiset;


import com.blackwell.elevenbytes.GameMap;
import com.blackwell.elevenbytes.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AI {
    static class Combination{
        private String combination;
        private int score;

        public Combination(String combination, int score) {
            this.combination = combination;
            this.score = score;
        }

        public void add(int score) { this.score += score; }

        @Override
        public String toString() {
            return combination + ": " + score;
        }

        public String getCombination() { return combination;}
        public int getScore() { return score; }

        @Override
        public int hashCode() {
            return "".equals(combination) ? 0 : 31 * Integer.valueOf(combination);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Combination)
                return ((Combination) obj).combination.equals(combination);

            return false;
        }
    }

    static class MultipleCombinations extends Combination{
        private int count;

        public int getCount() { return count; }

        public MultipleCombinations(String comb, int score){
            super(comb, score);
            count = 0;
        }
        public MultipleCombinations(Combination comb) { this(comb.combination, comb.score); }

        public void increment() { count++; }
    }

    static class CombinationsSet extends HashSet<MultipleCombinations>{
        @Override
        public boolean add(MultipleCombinations multipleCombinations) {
            for (MultipleCombinations mc : this){
                if (mc.equals(multipleCombinations)){
                    mc.add(multipleCombinations.getScore());
                    mc.increment();
                    return false;
                }
            }

            super.add(multipleCombinations);
            return true;
        }
    }

    private static void addCombination(Set<Combination> comb, Node n, String trace){
        for (int i=0; i<n.getNodesLength(); ++i){
            addCombination(comb, n.getNode(i), trace + String.valueOf(n.getMove()));

//            Node son = root.getNode(i);
//            for(int j=0; j<son.getNodesLength(); ++j){
//                Node son2 = son.getNode(i);
//                combinations.add(
//                        new Combination(son.getMove() +""+son2.getMove(),
//                                son2.getScore()));
//            }
        }
        comb.add(new Combination(trace, n.getScore()));
    }

    public static void move(Map map){
        Node root = new Node(map, -1);
        root.setUpNodes();

        for(int i=0; i<root.getNodesLength(); ++i)
            root.getNode(i).setUpNodes();

        Set<Combination> combinations = new HashSet<>();
        addCombination(combinations,root, "");
        System.out.println(combinations);
        Set<MultipleCombinations> average= new CombinationsSet();
        for(Combination comb : combinations)
            average.add(new MultipleCombinations(comb));

        Combination comb = new Combination("", 0);
        for(MultipleCombinations mc : average) {
            float score = mc.getScore();
            float count = mc.getCount();
            if ( (score/count) > comb.getScore() )
                comb = mc;
        }

//        moveMap(map, (int) comb.combination.charAt(0) );
//        moveMap(map, (int) comb.combination.charAt(1) );
        System.out.println(comb.combination);
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
