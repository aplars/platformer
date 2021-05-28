package com.sa.game.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;

/**
 * MovementSystem - Apply velocity to the position.
 */
public class MovementSystem extends IteratingSystem {
    public MovementSystem() {
        super(Family.all(PhysicsComponent.class, PositionComponent.class).get());
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
        final PositionComponent positionComponent = ComponentMappers.position.get(entity);

        positionComponent.position.mulAdd(physicsComponent.velocity, deltaTime);
    }
}
