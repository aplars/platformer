package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.gfx.Renderer;

public class RenderSystem extends IteratingSystem {
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    public Renderer renderer;
    PerformanceCounter performanceCounter;

    public RenderSystem(PerformanceCounter performanceCounter, Renderer renderer) {
        super(Family.all(RenderComponent.class).get());
        this.renderer = renderer;
        this.performanceCounter = performanceCounter;
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      performanceCounter.start();
      RenderComponent renderComponent = renderMapper.get(entity);
      PositionComponent positionComponent = positionMapper.get(entity);

      renderComponent.sprite.setCenter(positionComponent.position.x, positionComponent.position.y);

      renderer.add(renderComponent.sprite);
      performanceCounter.stop();
	}
}
