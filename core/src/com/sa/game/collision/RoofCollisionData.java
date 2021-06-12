package com.sa.game.collision;

import com.badlogic.gdx.math.Vector2;

public class RoofCollisionData {
    public boolean didCollide = false;
    public Vector2 move = new Vector2(0.0f, 0.0f);

    public RoofCollisionData() {
    }

    public RoofCollisionData(boolean didCol, Vector2 move) {
        didCollide = didCol;
        this.move.set(move);
    }

    public void set(boolean didCol, Vector2 move) {
        didCollide = didCol;
        this.move.set(move);
    }

    public void set(boolean didCol, float movex, float movey) {
        didCollide = didCol;
        this.move.set(movex, movey);
    }
}
