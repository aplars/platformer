package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;

public class ControlSystem extends IteratingSystem {
    private ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMap = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<CollisionComponent> collisionMap = ComponentMapper.getFor(CollisionComponent.class);
    int tileSizeInPixels;

    public ControlSystem(int tileSizeInPixels) {
        super(Family.all(ControlComponent.class, PhysicsComponent.class).get());

        this.tileSizeInPixels = tileSizeInPixels;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ControlComponent control = controlMap.get(entity);
        PhysicsComponent physics = physicsMap.get(entity);
        CollisionComponent collision = collisionMap.get(entity);

        float jumpTime = 0.5f;
        float jumpImpulse = 2f*(tileSizeInPixels*5f+2)/jumpTime;
        float moveForce = 30 * tileSizeInPixels * physics.mass;

        if(control.buttonLeft) {
            physics.force.add(-moveForce, 0f);
        }
        if(control.buttonRight) {
            physics.force.add(moveForce, 0f);
        }
        if(control.buttonA && collision.entity.groundCollisionData.didCollide) {
            physics.velocity.y += jumpImpulse;
        }
    }
}
