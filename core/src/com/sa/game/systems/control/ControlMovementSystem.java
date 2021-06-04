package com.sa.game.systems.control;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.DelayControlComponent;
import com.sa.game.components.PhysicsComponent;

public class ControlMovementSystem extends IteratingSystem {
    private final ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);
    private final ComponentMapper<PhysicsComponent> physicsMap = ComponentMapper.getFor(PhysicsComponent.class);
    private final ComponentMapper<CollisionComponent> collisionMap = ComponentMapper.getFor(CollisionComponent.class);
    CollisionDetection collisionDetection;
    StaticEnvironment staticEnvironment;

    public ControlMovementSystem(final CollisionDetection collisionDetection, final StaticEnvironment staticEnvironment) {
        super(Family.all(ControlComponent.class, PhysicsComponent.class).exclude(DelayControlComponent.class).get());
        this.collisionDetection = collisionDetection;
        this.staticEnvironment = staticEnvironment;
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final ControlComponent control = controlMap.get(entity);
        final PhysicsComponent physics = physicsMap.get(entity);
        final CollisionComponent collision = collisionMap.get(entity);

        final float jumpTime = physics.jumpTime;;
        final float jumpImpulse = (float)Math.sqrt(2f*(staticEnvironment.tileSizeInPixels*5f+2f)*(-physics.gravity));
        final float moveForce = 30 * staticEnvironment.tileSizeInPixels * physics.mass;

        if(control.buttonA && collision.entity.groundCollisionData.didCollide) {
            physics.velocity.y += jumpImpulse;
        }
        if(control.buttonLeft) {
            physics.force.add(-moveForce, 0f);
        }
        if(control.buttonRight) {
            physics.force.add(moveForce, 0f);
        }
        if(control.buttonUp) {
            physics.force.add(0f, moveForce);
        }
        if(control.buttonDown) {
            physics.force.add(0f, -moveForce);
        }
    }
}
