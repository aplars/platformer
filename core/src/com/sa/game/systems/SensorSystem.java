package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.StaticEnvironment.LayerId;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.SensorComponent;
import com.sa.game.entities.WalkDirection;

public class SensorSystem extends IteratingSystem {
    StaticEnvironment staticEnvironment;
    public SensorSystem(final StaticEnvironment staticEnvironment) {
        super(Family.all(SensorComponent.class, CollisionComponent.class, PhysicsComponent.class).get());
        this.staticEnvironment = staticEnvironment;
    }
    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final SensorComponent sensorComponent = ComponentMappers.sensor.get(entity);
        final CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);
        final PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);

        sensorComponent.isOnground = collisionComponent.entity.groundCollisionData.didCollide;
        sensorComponent.wallCollisionLeft = collisionComponent.entity.wallsCollisionData.didCollide &&
                physicsComponent.velocity.x < 0f;
        sensorComponent.wallCollisionRight = collisionComponent.entity.wallsCollisionData.didCollide &&
            physicsComponent.velocity.x > 0f;

        final Vector2 feetPos = new Vector2(collisionComponent.entity.box.x + collisionComponent.entity.box.width/2f, collisionComponent.entity.box.y);
        final int dir = 0;
        /*if(physicsComponent.walkDirection == WalkDirection.Left)
            dir = -1;
        else if (physicsComponent.walkDirection == WalkDirection.Right)
            dir = 1;*/
        GridPoint2 gridPoint2 = new GridPoint2();
        gridPoint2 = staticEnvironment.getGridPointFromWorldCoordinate(LayerId.Floor, feetPos, gridPoint2);
        final int tileId = staticEnvironment.getTileId(LayerId.Floor, gridPoint2.x+dir, gridPoint2.y-1);

        sensorComponent.groundOnNextTile = true;
        if (tileId == 0) {
            sensorComponent.groundOnNextTile = false;
        }
    }
}
