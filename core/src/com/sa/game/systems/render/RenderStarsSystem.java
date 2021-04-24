package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderStarsComponent;
import com.sa.game.gfx.Renderer;
import com.sa.game.gfx.Sprite;

public class RenderStarsSystem extends IteratingSystem {
    public Renderer renderer;
    float time = 0;
    public RenderStarsSystem(Renderer renderer) {
        super(Family.all(RenderStarsComponent.class, PositionComponent.class, HealthComponent.class).get());
        this.renderer = renderer;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RenderStarsComponent renderStarsComponent = ComponentMappers.renderStars.get(entity);
        PositionComponent positionComponent = ComponentMappers.position.get(entity);
        HealthComponent healthComponent = ComponentMappers.health.get(entity);

        TextureRegion textureRegion = renderStarsComponent.animation.getKeyFrame(time);

        if (healthComponent.isStunned) {
            Sprite sprite = new Sprite();
            sprite.size.set(16, 16);
            sprite.textureRegion.setRegion(textureRegion);
            sprite.setCenter(positionComponent.position.x, positionComponent.position.y + 8);
            renderer.add(sprite);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        time += deltaTime;
    }
}
