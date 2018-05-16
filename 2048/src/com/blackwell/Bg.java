package com.blackwell;

/**
 * Enum with colors for cells backgrounds, cells text fill and window bg.
 */
public enum Bg {
    SLIGHTLY("FFFFFF"),
    STANDART("FF7E04"),
    RED_FULL("CC1500"),
    VIOLET_LIGHT("C704FF"),
    BLUE_LIGHT("5C76CC"),

    /* More great colors:
    BLUE_DEEP("1A12FF"),
    YELLOW_LIGHT("FFEE04"),
    RED_DEEP("7F0D00"),
    VELVET("6B251C"),
    VIOLET_DEEP("8F16B2"),
    */

    BLACK("000000"),;

    private final String color;

    Bg(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}