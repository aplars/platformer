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
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.PositionComponent;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.entities.WalkDirection;

public class PlayerInputSystem extends IteratingSystem{
    private ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);

    public PlayerInputSystem() {
        super(Family.all(ControlComponent.class, Player1Component.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ControlComponent controlComponent = controlMap.get(entity);
        controlComponent.clear();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //physicsComponent.force.x -= entityControlComponent.moveForce;
            controlComponent.buttonLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //physicsComponent.force.x += entityControlComponent.moveForce;
            controlComponent.buttonRight = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            controlComponent.buttonUp = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            controlComponent.buttonDown = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            //physicsComponent.velocity.y = entityControlComponent.jumpImpulse;
            controlComponent.buttonA = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)) {
            //float projDir = (physicsComponent.walkDirection == WalkDirection.Left) ? -300f : 300f;
            //CreateEnteties.playerStunProjectile(assetManager, positionComponent.position, new Vector2(projDir, 0f), tileSizeInPixels, collisionDetection, preUpdateEngine, updateEngine);
            //entityControlComponent.timeUntilNextBulletSpawnPossible = EntityControlComponent.SPAWN_TIME_INTERVAL;
            controlComponent.buttonB = true;
        }
        //entityControlComponent.timeUntilNextBulletSpawnPossible -= deltaTime;
            //entityControlComponent.timeUntilNextBulletSpawnPossible = Float.max(0f, entityControlComponent.timeUntilNextBulletSpawnPossible);
    }
}
