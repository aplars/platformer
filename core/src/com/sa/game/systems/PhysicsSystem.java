package com.sa.game.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.entities.WalkDirection;

public class PhysicsSystem extends IteratingSystem {
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);

    public PhysicsSystem() {
        super(Family.all(PhysicsComponent.class).get());
    }

	@Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physicsComponent = pm.get(entity);
        if (physicsComponent != null) {
            physicsComponent.force.add(0, physicsComponent.gravity);
            physicsComponent.acceleration.set(0f, 0f);
            physicsComponent.acceleration.mulAdd(physicsComponent.force, 1f/physicsComponent.mass);
            physicsComponent.velocity.mulAdd(physicsComponent.acceleration, deltaTime);
            physicsComponent.force.set(0f, 0f);
            if (physicsComponent.velocity.x > 0f) {
                physicsComponent.walkDirection = WalkDirection.Right;
            }
            if (physicsComponent.velocity.x < 0f) {
                physicsComponent.walkDirection = WalkDirection.Left;
            }
        }
    }
}
