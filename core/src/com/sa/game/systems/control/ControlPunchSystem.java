package com.sa.game.systems.control;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.PunchComponent;
import com.sa.game.components.groups.BoxingGloveGroupComponent;
import com.sa.game.entities.CreateEnteties;

public class ControlPunchSystem extends IteratingSystem {
    AssetManager assetManager;
    CollisionDetection collisionDetection;
    StaticEnvironment staticEnvironment;
    //float currentTime = 0f;

    public ControlPunchSystem(final AssetManager assetManager, final CollisionDetection collisionDetection, final StaticEnvironment staticEnvironment) {
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
    protected void processEntity(final Entity entity, final float deltaTime) {
        final PunchComponent punchComponent = ComponentMappers.punch.get(entity);
        final ControlComponent controlComponent = ComponentMappers.control.get(entity);
        final PositionComponent positionComponent = ComponentMappers.position.get(entity);
        final PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
        final PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(entity);

        ImmutableArray<Entity> ents = this.getEngine().getEntitiesFor(Family.all(BoxingGloveGroupComponent.class).get());
        if (ents.size() > 0)
            return;

        if (controlComponent.buttonB && pickUpEntityComponent.entity == null) {
            final Vector2 vel = new Vector2(200f * (float) physicsComponent.GetWalkDirectionScalar(), 0f);
            final Entity boxingGlove = CreateEnteties.boxingGlove(assetManager, positionComponent.position, vel,
                                                                  staticEnvironment.tileSizeInPixels * 3, entity, staticEnvironment, collisionDetection);

            this.getEngine().addEntity(boxingGlove);

        }
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
        //currentTime += deltaTime;
    }

}
