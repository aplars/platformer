package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.DelayControlComponent;

public class DelayControlSystem extends IteratingSystem{

	public DelayControlSystem() {
		super(Family.all(DelayControlComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        DelayControlComponent delayAIComponent = ComponentMappers.delayAi.get(entity);
        if(delayAIComponent.delay <= 0f)
            entity.remove(DelayControlComponent.class);
        delayAIComponent.delay -= deltaTime;
	}
}
