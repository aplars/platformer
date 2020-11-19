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
    public float airResistance = 0.8f;
    public float gravity = 0f; //force of gravity
    public WalkDirection walkDirection = WalkDirection.Right;

}
