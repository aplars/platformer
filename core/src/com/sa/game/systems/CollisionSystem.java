package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;

public class CollisionSystem extends IteratingSystem {
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<PositionComponent> posm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

    PerformanceCounter performanceCounter;
    CollisionDetection collisionDetection;
    StaticEnvironment staticEnvironment;

    public CollisionSystem(PerformanceCounter performanceCounter, CollisionDetection collisionDetection, StaticEnvironment staticEnvironment) {
        super(Family.all(PhysicsComponent.class, PositionComponent.class, CollisionComponent.class).get());
        this.performanceCounter = performanceCounter;
        this.collisionDetection = collisionDetection;
        this.staticEnvironment = staticEnvironment;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physicsComponent = pm.get(entity);
        PositionComponent positionComponent = posm.get(entity);
        CollisionComponent collisionComponent = cm.get(entity);

        Vector2 pos = new Vector2(positionComponent.position);
        collisionComponent.entity.box.setCenter(pos.add(collisionComponent.offset));
        collisionComponent.entity.velocity.set(physicsComponent.velocity);
    }

    @Override public void update(float deltaTime) {
        super.update(deltaTime);
        performanceCounter.start();
        this.collisionDetection.update(deltaTime, this.staticEnvironment);
        performanceCounter.stop();
    }
}
