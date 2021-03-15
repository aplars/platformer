package com.sa.game.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Renderer {
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private ArrayList<Text> texts = new ArrayList<>();
    private ArrayList<Rectangle> rects = new ArrayList<>();
    private ArrayList<Circle> circles = new ArrayList<>();

    SpriteBatch spriteBatch;
    SpriteBatch textBatch;
    ShapeRenderer shapeRenderer;

    BitmapFont font;
    BitmapFont bigFont;

    public Renderer() {
        spriteBatch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);
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

    public void add(Circle circle) {
        circles.add(circle);
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
                    (sprite.mirrorX) ? sprite.position.x + sprite.size.x + sprite.offset.x : sprite.position.x + sprite.offset.x,
                    sprite.position.y + sprite.offset.y,
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
        for (Circle circle : circles) {
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }

        shapeRenderer.end();
        rects.clear();
        circles.clear();

        float ratew = fontCamera.viewportWidth/camera.viewportWidth;  //<--- you should calculate these 2 only once.
        float rateh = fontCamera.viewportHeight/camera.viewportHeight;

        textBatch.setProjectionMatrix(fontCamera.projection);
        textBatch.setTransformMatrix(fontCamera.view);
        textBatch.begin();
        for(Text text : texts) {
            float x = fontCamera.position.x-(camera.position.x-text.x)*ratew;
            float y = fontCamera.position.y-(camera.position.y-text.y)*rateh;
            if(text.font == Text.Font.Medium)
                font.draw(textBatch, text.chars, x, y);
            if(text.font == Text.Font.Big)
                bigFont.draw(textBatch, text.chars, x, y);
        }
        texts.clear();
        textBatch.end();

    }
}
