package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderSpriteInWhiteColorComponent;
import com.sa.game.gfx.Sprite.ColorMode;

public class RenderSpriteInWhiteColorSystem extends IteratingSystem {

	public RenderSpriteInWhiteColorSystem() {
      super(Family.all(RenderSpriteInWhiteColorComponent.class, RenderComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        RenderComponent renderComponent = ComponentMappers.render.get(entity);
        RenderSpriteInWhiteColorComponent renderSpriteInWhiteColorComponent = ComponentMappers.renderSpriteInWhiteColor.get(entity); 
        if(renderSpriteInWhiteColorComponent.time > 0) {
            renderComponent.sprite.colorMode = ColorMode.White;
            renderSpriteInWhiteColorComponent.time-=deltaTime;
        }
        else {
            renderComponent.sprite.colorMode = ColorMode.Normal;
            entity.remove(RenderSpriteInWhiteColorComponent.class);
        }
    }
}
