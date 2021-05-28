package com.sa.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Family.Builder;
import com.badlogic.ashley.utils.ImmutableArray;
import com.sa.game.components.CollisionComponent;

public class CollidersIteratingSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> collidingEntities;
    private Builder family;

    public CollidersIteratingSystem(Builder family) {
        this.family = family;
    }

    public void addedToEngine (Engine engine) {
        entities = engine.getEntitiesFor(family.all(CollisionComponent.class).get());
    }

}
