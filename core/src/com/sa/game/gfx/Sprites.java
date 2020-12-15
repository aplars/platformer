package com.sa.game.gfx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Sprites {
    private ArrayList<com.sa.game.gfx.Sprite> sprites = new ArrayList<>();
    SpriteBatch spriteBatch;

    public Sprites() {
        spriteBatch = new SpriteBatch();
    }

    public void add(com.sa.game.gfx.Sprite sprite) {
        sprites.add(sprite);
    }

    public int numberOfSprites() {
        return sprites.size();
    }

    public void render(OrthographicCamera camera) {
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.begin();
        for(Sprite sprite : sprites) {
            spriteBatch.draw(
                    sprite.textureRegion,
                    (sprite.mirrorX) ? sprite.position.x + sprite.size.x : sprite.position.x,
                    sprite.position.y,
                    (sprite.mirrorX) ? -sprite.size.x : sprite.size.x,
                    sprite.size.y); // Draw current frame at (50, 50)
        }
        spriteBatch.end();

        sprites.clear();
    }
}
