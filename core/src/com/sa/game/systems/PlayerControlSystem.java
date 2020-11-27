package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.EntityControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.PositionComponent;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.entities.WalkDirection;

public class PlayerControlSystem extends IteratingSystem{
    private ComponentMapper<Player1Component> pl1m = ComponentMapper.getFor(Player1Component.class);
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<EntityControlComponent> cm = ComponentMapper.getFor(EntityControlComponent.class);
    private ComponentMapper<CollisionComponent> colm = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<PositionComponent> posm = ComponentMapper.getFor(PositionComponent.class);

    private AssetManager assetManager;
    private int tileSizeInPixels;
    private CollisionDetection collisionDetection;
    private Engine preUpdateEngine;
    private Engine updateEngine;

    public PlayerControlSystem(AssetManager assetManager, int tileSizeInPixels, CollisionDetection collisionDetection, Engine preUpdateEngine, Engine updateEngine) {
        super(Family.all(Player1Component.class, PhysicsComponent.class, EntityControlComponent.class, CollisionComponent.class, PositionComponent.class).get());

        this.assetManager = assetManager;
        this.tileSizeInPixels = tileSizeInPixels;
        this.collisionDetection = collisionDetection;
        this.preUpdateEngine = preUpdateEngine;
        this.updateEngine = updateEngine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physicsComponent = pm.get(entity);
        EntityControlComponent entityControlComponent = cm.get(entity); 
        CollisionComponent collisionComponent = colm.get(entity);
        PositionComponent positionComponent = posm.get(entity);

        if(physicsComponent != null && entityControlComponent != null && collisionComponent != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                physicsComponent.force.x -= entityControlComponent.moveForce;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                physicsComponent.force.x += entityControlComponent.moveForce;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && collisionComponent.entity.groundCollisionData.didCollide) {
              physicsComponent.velocity.y = entityControlComponent.jumpImpulse;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W) && entityControlComponent.timeUntilNextBulletSpawnPossible <= 0) {
                float projDir = (physicsComponent.walkDirection == WalkDirection.Left) ? -300f : 300f;
                CreateEnteties.playerStunProjectile(assetManager, positionComponent.position, new Vector2(projDir, 0f), tileSizeInPixels, collisionDetection, preUpdateEngine, updateEngine);
                entityControlComponent.timeUntilNextBulletSpawnPossible = EntityControlComponent.SPAWN_TIME_INTERVAL;
            }
            entityControlComponent.timeUntilNextBulletSpawnPossible -= deltaTime;
            entityControlComponent.timeUntilNextBulletSpawnPossible = Float.max(0f, entityControlComponent.timeUntilNextBulletSpawnPossible);
        }
    }
}
