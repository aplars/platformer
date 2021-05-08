package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.groups.ExitGroupComponent;

public class ExitSystem extends IteratingSystem {

	public ExitSystem() {
      super(Family.all(ExitGroupComponent.class, CollisionComponent.class).get());
      //TODO Auto-generated constructor stub
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

  }
}
