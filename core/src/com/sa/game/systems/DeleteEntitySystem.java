package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.DeleteEntityComponent;

public class DeleteEntitySystem extends IteratingSystem {

	public DeleteEntitySystem() {
		super(Family.all(DeleteEntityComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      this.getEngine().removeEntity(entity);
  }
}
