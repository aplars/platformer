package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;

public class CollisionSystem extends IteratingSystem {
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<PositionComponent> posm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

    public CollisionSystem() {
        super(Family.all(PhysicsComponent.class, PositionComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physicsComponent = pm.get(entity);
        PositionComponent positionComponent = posm.get(entity);
        CollisionComponent collisionComponent = cm.get(entity);

        collisionComponent.entity.box.setCenter(positionComponent.position);
        collisionComponent.entity.velocity.set(physicsComponent.velocity);
    }
}