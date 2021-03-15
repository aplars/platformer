package com.sa.game.gfx;

public class Text {
    public enum Font {
        Medium,
        Big,
        Small
    }
    public String chars = new String();

    public Font font = Font.Medium;
    public float x;
    public float y;
    public float scale;
}
