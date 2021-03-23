package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.entities.PlayerStunProjectile;
import com.sa.game.entities.WalkDirection;

public class ControlSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> positionMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMap = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<CollisionComponent> collisionMap = ComponentMapper.getFor(CollisionComponent.class);
    AssetManager assetManager;
    CollisionDetection collisionDetection;
    float timeToNextProjectile = 0f;
    StaticEnvironment staticEnvironment;

    public ControlSystem(AssetManager assetManager, CollisionDetection collisionDetection, StaticEnvironment staticEnvironment) {
        super(Family.all(ControlComponent.class, PhysicsComponent.class).get());
        this.assetManager = assetManager;
        this.collisionDetection = collisionDetection;
        this.staticEnvironment = staticEnvironment;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMap.get(entity);
        ControlComponent control = controlMap.get(entity);
        PhysicsComponent physics = physicsMap.get(entity);
        CollisionComponent collision = collisionMap.get(entity);
        PickUpEntityComponent pickUpEntity = ComponentMappers.pickUp.get(entity);
        MoveToEntityComponent moveToEntityComponent = ComponentMappers.moveToEntity.get(entity);

        float jumpTime = 0.5f;
        float jumpImpulse = 2f*(staticEnvironment.tileSizeInPixels*5f+2)/jumpTime;
        float moveForce = 30 * staticEnvironment.tileSizeInPixels * physics.mass;

        if(control.buttonLeft) {
            physics.force.add(-moveForce, 0f);
        }
        if(control.buttonRight) {
            physics.force.add(moveForce, 0f);
        }
        if(control.buttonA && collision.entity.groundCollisionData.didCollide) {
            physics.velocity.y += jumpImpulse;
        }
        if(control.buttonB) {
            if(pickUpEntity != null && pickUpEntity.entity!=null) {
                PhysicsComponent pickedUpEntPhysics = ComponentMappers.physics.get(pickUpEntity.entity);
                CollisionComponent collisionComponent = ComponentMappers.collision.get(pickUpEntity.entity);
                collisionComponent.entity.isEnable = true;
                //pickedUpEntPhysics.force.x = 1800f;
                if(pickedUpEntPhysics.walkDirection == WalkDirection.Right)
                    pickedUpEntPhysics.velocity.x = 300;
                else
                    pickedUpEntPhysics.velocity.x = -300;

                pickedUpEntPhysics.friction = 1f;
                pickedUpEntPhysics.airResistance = 1f;
                if(moveToEntityComponent != null)
                    moveToEntityComponent.isEnable = false;
                pickUpEntity.entity.remove(MoveToEntityComponent.class);
                entity.remove(PickUpEntityComponent.class);
            }
            else if(timeToNextProjectile <= 0f){
                Vector2 vel = new Vector2(300f * (float) physics.GetWalkDirectionScalar(), 0f);
                //Entity projectile = CreateEnteties.playerStunProjectile(assetManager, position.position, projectileVelocity, tileSizeInPixels, collisionDetection);
                Entity boxingGlove = CreateEnteties.boxingGlove(assetManager, position.position, vel,
                        staticEnvironment.tileSizeInPixels*3, entity, staticEnvironment, collisionDetection);
                this.getEngine().addEntity(boxingGlove);
                timeToNextProjectile = 1.0f;
            }
        }
        timeToNextProjectile -= deltaTime;

    }
}
