package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ParticleEffectComponent;
import com.sa.game.components.PositionComponent;

public class RenderParticleSystem extends IteratingSystem {
    SpriteBatch batch;
    OrthographicCamera camera;

    public RenderParticleSystem(OrthographicCamera camera) {
        super(Family.all(ParticleEffectComponent.class, PositionComponent.class).get());

        batch = new SpriteBatch();
        this.camera = camera;
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = ComponentMappers.position.get(entity);
        ParticleEffectComponent particleEffectComponent = ComponentMappers.particleEffect.get(entity);

        if(particleEffectComponent.particleEffect.isComplete())
            return;

        particleEffectComponent.particleEffect.getEmitters().first().setPosition(positionComponent.position.x, positionComponent.position.y);
        //particleEffectComponent.particleEffect.start();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        particleEffectComponent.particleEffect.draw(batch, deltaTime);
        batch.end();
  }
}
