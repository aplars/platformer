package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.RectangleCollisionData;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.ThrownComponent;
import com.sa.game.components.groups.ToolGroupComponent;

/**
 * Drops a thrown entity. You shall not be able to pick up the entity again until it stops colliding with you.
 * We disable the collisions until the objects get separated.
 */
public class DroppedSystem extends IteratingSystem {
    public DroppedSystem() {
        super(Family.all(ToolGroupComponent.class, ThrownComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);
        final ThrownComponent thrownComponent = ComponentMappers.thrown.get(entity);

        collisionComponent.entity.isEnable = true;
        entity.remove(MoveToEntityComponent.class);

        final CollisionComponent parentCollisionComponent = ComponentMappers.collision.get(thrownComponent.parent);

        //Stop colliding dropped tool until tool and parent gets separated. 
        CollisionDetection.disableCollisionBetweenEntities(collisionComponent.entity, parentCollisionComponent.entity);

        final RectangleCollisionData colData = IntersectionTests.rectangleRectangle(collisionComponent.entity.box, new Vector2(), parentCollisionComponent.entity.box);
        if(!colData.didCollide && collisionComponent.entity.groundCollisionData.didCollide) {
            //The objects did not collide. They are separated. Enable collisions again and remove thrown component because the tool is not thrown/dropped anymore.
            entity.remove(ThrownComponent.class);
            CollisionDetection.enableCollisionBetweenEntities(collisionComponent.entity, parentCollisionComponent.entity);
        }
    }
}
