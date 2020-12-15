package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.entities.PlayerStunProjectile;

public class ControlSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> positionMap = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMap = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<CollisionComponent> collisionMap = ComponentMapper.getFor(CollisionComponent.class);
    AssetManager assetManager;
    CollisionDetection collisionDetection;
    int tileSizeInPixels;

    public ControlSystem(AssetManager assetManager, CollisionDetection collisionDetection, int tileSizeInPixels) {
        super(Family.all(ControlComponent.class, PhysicsComponent.class).get());
        this.assetManager = assetManager;
        this.collisionDetection = collisionDetection;
        this.tileSizeInPixels = tileSizeInPixels;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMap.get(entity);
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
        if(control.buttonB) {
            Vector2 projectileVelocity = new Vector2();
            projectileVelocity.y = 0;
            projectileVelocity.x = 300f * (float) physics.GetWalkDirectionScalar();

            Entity projectile = CreateEnteties.playerStunProjectile(assetManager, position.position, projectileVelocity, tileSizeInPixels, collisionDetection);
            this.getEngine().addEntity(projectile);
        }
    }
}
