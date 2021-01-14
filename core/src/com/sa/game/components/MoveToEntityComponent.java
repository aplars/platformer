package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class MoveToEntityComponent implements Component {
    public Entity entity;
    public Vector2 offset = new Vector2();
    public float maxSpeed = 0f;
    public boolean isEnable = true;

    public MoveToEntityComponent(Entity ent, Vector2 offset, float maxSpeed) {
        if(!ComponentMappers.position.has(ent)) {
            throw new IllegalArgumentException("Entity must have a position");
        }
        this.entity = ent;
        this.offset.set(offset);
        this.maxSpeed = maxSpeed;
    }
}

