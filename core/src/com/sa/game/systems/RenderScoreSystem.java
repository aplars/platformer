package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sa.game.StaticEnvironment;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.Player1Component;
import com.sa.game.gfx.Renderer;
import com.sa.game.gfx.Text;

public class RenderScoreSystem extends IteratingSystem {
    private Renderer renderer;
    private StaticEnvironment staticEnvironment;

    public RenderScoreSystem(Renderer renderer, OrthographicCamera camera, OrthographicCamera fontCamera, StaticEnvironment staticEnvironment) {
        super(Family.all(Player1Component.class).get());
        this.renderer = renderer;
        this.staticEnvironment = staticEnvironment;
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      Player1Component player1Component = ComponentMappers.player1.get(entity);
      Text text = new Text();
      text.chars = Integer.toString(player1Component.score);
      //text.height = 100;
      //text.width = 100;
      text.font = Text.Font.Big;
      text.x = staticEnvironment.tileSizeInPixels*2;
      text.y = staticEnvironment.getWorldBoundY();
      renderer.add(text);
  }
}
