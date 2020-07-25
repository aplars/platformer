package com.sa.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
    float size;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    Enemy(Vector2 position, float size) {
        this.position = position;
        this.size = size;
    }

    void update(float dt) {

    }

    void render(float t, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(position.x,position.y, size);
        shapeRenderer.end();

    }

}
