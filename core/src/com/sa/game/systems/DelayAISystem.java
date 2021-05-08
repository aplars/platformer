package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.DelayAIComponent;

public class DelayAISystem extends IteratingSystem{

	public DelayAISystem() {
		super(Family.all(DelayAIComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        DelayAIComponent delayAIComponent = ComponentMappers.delayAi.get(entity);
        if(delayAIComponent.delay <= 0f)
            entity.remove(DelayAIComponent.class);
        delayAIComponent.delay -= deltaTime;
	}
}
