package org.george;

public class Main {
    public static void main(String[] args){
        Thread george = new Thread(new George());
        george.start();

    }
}
