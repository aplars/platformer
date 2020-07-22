package com.sa.game;

import com.badlogic.gdx.math.Vector2;

public class CollissionData {
    public boolean didCollide = false;
    Vector2 move = new Vector2(0.0f, 0.0f);

    CollissionData(boolean didCol, Vector2 move) {
        didCollide = didCol;
        this.move= move;
    }
}
