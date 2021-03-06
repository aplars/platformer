package com.sa.game.collision;

import com.badlogic.gdx.math.Vector2;

public class WallCollisionData {
    public boolean didCollide = false;
    public Vector2 move = new Vector2(0.0f, 0.0f);

    public WallCollisionData() { }

    public WallCollisionData(boolean didCol, Vector2 move) {
        didCollide = didCol;
        this.move.set(move);
    }

    public void set(boolean didCol, Vector2 move) {
        didCollide = didCol;
        this.move.set(move);
    }


}
