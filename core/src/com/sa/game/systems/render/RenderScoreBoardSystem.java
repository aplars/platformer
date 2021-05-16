package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sa.game.StaticEnvironment;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.gfx.Renderer;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Text;

public class RenderScoreBoardSystem extends IteratingSystem {
    private Renderer renderer;
    private StaticEnvironment staticEnvironment;
    private TextureRegion heartImage;
    private Sprite sprites[] = new Sprite[6];
    AssetManager assetManager;

    public RenderScoreBoardSystem(AssetManager assetManager, Renderer renderer, OrthographicCamera camera, StaticEnvironment staticEnvironment) {
        super(Family.all(Player1Component.class, HealthComponent.class).get());
        this.renderer = renderer;
        this.staticEnvironment = staticEnvironment;
        this.assetManager =assetManager;
        for (int i = 0; i < 6; i++) {
            sprites[i] = new Sprite();
            sprites[i].size.x = 8;
            sprites[i].size.y = 8;
            sprites[i].textureRegion = heartImage;
        }
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      Player1Component player1Component = ComponentMappers.player1.get(entity);
      HealthComponent healthComponent = ComponentMappers.health.get(entity);
      Text text = new Text();
      text.chars = Integer.toString(player1Component.score);
      //text.height = 100;
      //text.width = 100;
      text.font = Text.Font.Big;
      text.x = staticEnvironment.tileSizeInPixels*2;
      text.y = staticEnvironment.getWorldBoundY()-10;
      renderer.add(text);

      TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);


      for (int i = 0; i < healthComponent.lives; i++) {
          sprites[i].textureRegion = atlas.findRegion("heart");
          sprites[i].position.x = (staticEnvironment.tileSizeInPixels*2)+(i * sprites[i].size.x);
          sprites[i].position.y = staticEnvironment.getWorldBoundY()-10;
          renderer.add(sprites[i], 0);
      }
  }
}
