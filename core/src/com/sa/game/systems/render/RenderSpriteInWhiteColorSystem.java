package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderSpriteInWhiteColorComponent;

public class RenderSpriteInWhiteColorSystem extends IteratingSystem {

	public RenderSpriteInWhiteColorSystem() {
      super(Family.all(RenderSpriteInWhiteColorComponent.class, RenderComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        RenderComponent renderComponent = ComponentMappers.render.get(entity);
        RenderSpriteInWhiteColorComponent renderSpriteInWhiteColorComponent = ComponentMappers.renderSpriteInWhiteColor.get(entity); 
        if(renderSpriteInWhiteColorComponent.time > 0) {
            renderComponent.sprite.white = true;
            renderSpriteInWhiteColorComponent.time-=deltaTime;
        }
        else {
            renderComponent.sprite.white = false;
            entity.remove(RenderSpriteInWhiteColorComponent.class);
        }
    }
}
