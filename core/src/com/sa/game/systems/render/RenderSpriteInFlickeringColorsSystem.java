package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderSpriteInFlickeringColorsComponent;
import com.sa.game.components.RenderSpriteInWhiteColorComponent;
import com.sa.game.gfx.Sprite.ColorMode;

public class RenderSpriteInFlickeringColorsSystem extends IteratingSystem {
    boolean doTheSwap = false;
    final float MAX_SWAP_TIME = 0.125f;
    float doTheSapTime = MAX_SWAP_TIME;
    public RenderSpriteInFlickeringColorsSystem() {
      super(Family.all(RenderSpriteInFlickeringColorsComponent.class, RenderComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        RenderComponent renderComponent = ComponentMappers.render.get(entity);
        RenderSpriteInFlickeringColorsComponent renderSpriteInWhiteColorComponent = ComponentMappers.renderSpriteInFlickeringColors.get(entity); 
        if(renderSpriteInWhiteColorComponent.time > 0) {
            if(!doTheSwap && doTheSapTime <= 0.0f)
                renderComponent.sprite.colorMode = ColorMode.White;
            else if(doTheSapTime <= 0.0f)
                renderComponent.sprite.colorMode = ColorMode.Normal;
            doTheSwap = !doTheSwap;
            if(doTheSapTime <= 0.0f)
                doTheSapTime = MAX_SWAP_TIME;
            else
                doTheSapTime -= deltaTime;
            renderSpriteInWhiteColorComponent.time-=deltaTime;
        }
        else {
            renderComponent.sprite.colorMode = ColorMode.Normal;
            entity.remove(RenderSpriteInFlickeringColorsComponent.class);
        }
    }
}
