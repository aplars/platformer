package com.sa.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.gfx.Sprite;

public class PlayerProjectile {
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public CollisionEntity collisionEntity = new CollisionEntity();
    private final ShapeRenderer shapeRenderer;
    private Sprite sprite = new Sprite();

    public PlayerProjectile(Vector2 position, Vector2 velocity, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {

        this.position.set(position);
        this.velocity.set(velocity);

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setWidth(staticEnvironment.tileSizeInPixels/4);
        collisionRectangle.setHeight(staticEnvironment.tileSizeInPixels/2);
        collisionRectangle.setCenter(position);
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        collisionEntity.box.setCenter(position);
    }

    public void render(float t, OrthographicCamera camera) {

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(collisionEntity.box.x, collisionEntity.box.y, collisionEntity.box.width, collisionEntity.box.height);
        shapeRenderer.end();
    }

}
