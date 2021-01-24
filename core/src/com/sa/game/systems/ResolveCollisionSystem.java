package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;

public class ResolveCollisionSystem extends IteratingSystem {
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<PositionComponent> posm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
    PerformanceCounter performanceCounter;

    public ResolveCollisionSystem(PerformanceCounter performanceCounter) {
        super(Family.all(PhysicsComponent.class, PositionComponent.class, CollisionComponent.class).get());
        this.performanceCounter = performanceCounter;
    }
    int coll = 0;
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        performanceCounter.start();
        PhysicsComponent physicsComponent = pm.get(entity);
        PositionComponent positionComponent = posm.get(entity);
        CollisionComponent collisionComponent = cm.get(entity);

        if(collisionComponent.entity.groundCollisionData.didCollide)  {
            if(physicsComponent.velocity.y < 0) {
                physicsComponent.velocity.y = 0;
                positionComponent.position.add(0f, collisionComponent.entity.groundCollisionData.move.y);
            }
        }
        if(collisionComponent.entity.wallsCollisionData.didCollide) {
            physicsComponent.velocity.x = 0;
            positionComponent.position.add(collisionComponent.entity.wallsCollisionData.move.x, 0f);
            coll++;
        }
        performanceCounter.stop();
    }
}
