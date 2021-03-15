package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.SensorComponent;

public class SensorSystem extends IteratingSystem {
    public SensorSystem() {
        super(Family.all(SensorComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SensorComponent sensorComponent = ComponentMappers.sensor.get(entity);
        CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);
        PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);

        sensorComponent.isOnground = collisionComponent.entity.groundCollisionData.didCollide;
        sensorComponent.wallCollisionLeft = collisionComponent.entity.wallsCollisionData.didCollide &&
                physicsComponent.velocity.x < 0f;
        sensorComponent.wallCollisionRight = collisionComponent.entity.wallsCollisionData.didCollide &&
            physicsComponent.velocity.x > 0f;

        sensorComponent.staticEnvironment.getTileIdFromWorldCoordinate(null, null);
    }
}
