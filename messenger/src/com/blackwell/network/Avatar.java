package com.blackwell.network;

public enum Avatar {
    GAME_GIRL("gamegirl.jpg"), JOKER("joker.jpg"), RICK("rick.png"),
    SUPREME("supreme.png"), YUSUKE("yusuke.jpg");

    public static final String IMG_PATH = "messenger/img/";
    private String value;
    Avatar(String value) { this.value = IMG_PATH + value; }

    public String get() { return value; }
}
