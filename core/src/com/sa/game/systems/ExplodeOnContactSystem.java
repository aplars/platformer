package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ExplodeOnContactComponent;

public class ExplodeOnContactSystem extends IteratingSystem {
    CollisionDetection collisionDetection;
    public ExplodeOnContactSystem(CollisionDetection collisionDetection) {
        super(Family.all(ExplodeOnContactComponent.class, CollisionComponent.class).get());
        this.collisionDetection = collisionDetection;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        boolean isColliding = ComponentMappers.collision.get(entity).entity.collidees.size() > 0;
        isColliding |= ComponentMappers.collision.get(entity).entity.groundCollisionData.didCollide;
        isColliding |= ComponentMappers.collision.get(entity).entity.wallsCollisionData.didCollide;
        if (isColliding) {
            this.collisionDetection.remove(ComponentMappers.collision.get(entity).entity);
            this.getEngine().removeEntity(entity);
        }
    }
}
