package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.ThrownComponent;

/**
 * Adds a Thrown component to the picked up entity.
 */
public class ControlThrowEntitySystem extends IteratingSystem {
    public ControlThrowEntitySystem() {
        super(Family.all(ControlComponent.class, PhysicsComponent.class, PickUpEntityComponent.class).get());
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      ControlComponent controlComponent = ComponentMappers.control.get(entity);
      PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
      PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(entity);

      if (controlComponent.buttonB) {
          if (pickUpEntityComponent != null && pickUpEntityComponent.entity != null) {
              pickUpEntityComponent.entity.add(new ThrownComponent(physicsComponent.walkDirection, entity));
              pickUpEntityComponent.entity = null;
          }
      }
	}
}
