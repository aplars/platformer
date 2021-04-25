package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.PhysicsComponent;

public class DampingSystem extends IteratingSystem {
    private final ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private final ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

    public DampingSystem() {
        super(Family.all(PhysicsComponent.class).get());
    }

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
      final PhysicsComponent physicsComponent = pm.get(entity);
      final CollisionComponent collisionComponent = cm.get(entity);

      if(physicsComponent != null && collisionComponent != null) {
          if(collisionComponent.entity.groundCollisionData.didCollide)
              physicsComponent.velocity.x *= physicsComponent.friction;
          else
              physicsComponent.velocity.x *= physicsComponent.airResistance;
      }
  }
}
