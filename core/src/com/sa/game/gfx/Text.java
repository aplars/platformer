package com.sa.game.gfx;

import com.badlogic.gdx.graphics.Color;

public class Text {
    public enum Font {
        Medium,
        Big//,
        //Small
    }
    public String chars = new String();

    public Font font = Font.Medium;
    public Color color = new Color(1, 1, 1, 1);
    public float x;
    public float y;
    public float scale;
}
