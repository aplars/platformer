package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.entities.WalkDirection;

public class PhysicsComponent implements Component {
    public Vector2 velocity = new Vector2();
    public Vector2 acceleration = new Vector2();
    public Vector2 force = new Vector2();
    public float mass = 1f;
    public float friction = 0.9f;
    public Vector2 airResistance = new Vector2(.85f, .98f);
    public float gravity = 0f; //force of gravity
    public float jumpTime = 0f;
    public WalkDirection walkDirection = WalkDirection.Right;

    public int GetWalkDirectionScalar() {
        if(walkDirection == WalkDirection.Right)
            return 1;
        else
            return -1;
    }
}
