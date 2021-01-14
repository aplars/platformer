package com.sa.game.gfx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Renderer {
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private ArrayList<Text> texts = new ArrayList<>();
    private ArrayList<Rectangle> rects = new ArrayList<>();

    SpriteBatch spriteBatch;
    SpriteBatch textBatch;
    ShapeRenderer shapeRenderer;

    BitmapFont font;

    public Renderer() {
        spriteBatch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
    }

    public void add(Sprite sprite) {
        sprites.add(sprite);
    }

    public void add(Text text) {
        texts.add(text);
    }

    public void add(Rectangle rect) {
        rects.add(rect);
    }

    public int numberOfSprites() {
        return sprites.size();
    }

    public void render(OrthographicCamera camera, OrthographicCamera fontCamera) {
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

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeType.Line);
        for (Rectangle rect : rects) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        shapeRenderer.end();
        rects.clear();

        float ratew = fontCamera.viewportWidth/camera.viewportWidth;  //<--- you should calculate these 2 only once.
        float rateh = fontCamera.viewportHeight/camera.viewportHeight;

        textBatch.setProjectionMatrix(fontCamera.projection);
        textBatch.setTransformMatrix(fontCamera.view);
        textBatch.begin();
        for(Text text : texts) {
            float x = fontCamera.position.x-(camera.position.x-text.x)*ratew;
            float y = fontCamera.position.y-(camera.position.y-text.y)*rateh;
            font.draw(textBatch, text.chars, x, y);
        }
        texts.clear();
        textBatch.end();

    }
}
