package com.sa.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;

public class PlayerProjectile {
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public Rectangle collisionRectangle = new Rectangle();
    public CollisionEntity collisionEntity = new CollisionEntity();
    private final ShapeRenderer shapeRenderer;

    public PlayerProjectile(Vector2 position, Vector2 velocity, CollisionDetection collisionDetection) {

        this.position.set(position);
        this.velocity.set(velocity);
        this.collisionRectangle.setWidth(10);
        collisionRectangle.setHeight(10);
        collisionRectangle.setCenter(position);
        collisionEntity.box = collisionRectangle;
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float dt) {
        position.add(velocity);
        collisionRectangle.setCenter(position);
    }

    public void render(float t, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
        shapeRenderer.end();
    }

}
