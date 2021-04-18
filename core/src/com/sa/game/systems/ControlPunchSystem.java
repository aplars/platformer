package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.PunchComponent;
import com.sa.game.components.ThrownComponent;
import com.sa.game.entities.CreateEnteties;

public class ControlPunchSystem extends IteratingSystem {
    AssetManager assetManager;
    CollisionDetection collisionDetection;
    StaticEnvironment staticEnvironment;
    float currentTime = 0f;

    public ControlPunchSystem(AssetManager assetManager, CollisionDetection collisionDetection, StaticEnvironment staticEnvironment) {
        super(Family.all(PunchComponent.class,
                         ControlComponent.class,
                         PositionComponent.class,
                         PhysicsComponent.class,
                         PickUpEntityComponent.class).get());

        this.assetManager = assetManager;
        this.collisionDetection = collisionDetection;
        this.staticEnvironment = staticEnvironment;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PunchComponent punchComponent = ComponentMappers.punch.get(entity);
        ControlComponent controlComponent = ComponentMappers.control.get(entity);
        PositionComponent positionComponent = ComponentMappers.position.get(entity);
        PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
        PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(entity);

        if (controlComponent.buttonB && pickUpEntityComponent.entity == null) {
            Vector2 vel = new Vector2(300f * (float) physicsComponent.GetWalkDirectionScalar(), 0f);
            Entity boxingGlove = CreateEnteties.boxingGlove(assetManager, positionComponent.position, vel,
                                                            staticEnvironment.tileSizeInPixels * 3, entity, staticEnvironment, collisionDetection);
            this.getEngine().addEntity(boxingGlove);

        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentTime += deltaTime;
    }

}
