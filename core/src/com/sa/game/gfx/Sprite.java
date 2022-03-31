package com.sa.game.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Sprite {
    public enum ColorMode {
        Normal,
        White,
        Grayed
    }

    public TextureRegion textureRegion = new TextureRegion();
    public Vector2 position = new Vector2();
    public Vector2 size = new Vector2();
    public float rotationDeg = 0f;
    public boolean mirrorX = false;

    public Vector2 offset = new Vector2();

    public int layer = 0;

    public ColorMode colorMode = ColorMode.Normal;

    public void setCenter(float x, float y) {
        position.x = x - size.x / 2f;
        position.y = y - size.y / 2f;
    }
}
