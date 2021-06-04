package com.sa.game.systems.control;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.DelayControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.ThrownComponent;

/**
 * Adds a Thrown component to the picked up entity. We do not want anything else to be fired
 * at the same time as we throw. If a projectile gets fired at the same time as we throw then
 * it is possible that projectile directly collides with and destroys the thrown object.
 */
public class ControlThrowEntitySystem extends IteratingSystem {
    public ControlThrowEntitySystem() {
        super(Family.all(ControlComponent.class, PhysicsComponent.class, PickUpEntityComponent.class).get());
    }

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
      final ControlComponent controlComponent = ComponentMappers.control.get(entity);
      final PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
      final PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(entity);

      if (controlComponent.buttonB) {
          if (pickUpEntityComponent != null && pickUpEntityComponent.entity != null) {
              pickUpEntityComponent.entity.add(new ThrownComponent(physicsComponent.walkDirection, entity));
              pickUpEntityComponent.entity = null;
              DelayControlComponent delayControlComponent =  new DelayControlComponent(0.5f);
              delayControlComponent.mask |= DelayControlComponent.BUTTONB;
              entity.add(delayControlComponent);
              controlComponent.buttonB = false;
          }
      }
	}
}
