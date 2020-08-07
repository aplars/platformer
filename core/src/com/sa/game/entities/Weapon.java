package com.sa.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;

public class Weapon {
    ShapeRenderer shapeRenderer;
    Rectangle collisionRectangle;
    CollisionEntity collisionEntity;
    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 dstPosition = new Vector2();

    boolean fire = false;

    public Weapon(Vector2 position, Vector2 velocity, float size, CollisionDetection collisionDetection) {
        shapeRenderer = new ShapeRenderer();

        this.position.set(position);
        this.dstPosition.set(position);
        this.velocity.set(velocity);

        collisionRectangle = new Rectangle(0, 0, size, size);
        collisionRectangle.setCenter(position.x, position.y);

        collisionEntity = new CollisionEntity();
        collisionEntity.box = collisionRectangle;
        collisionEntity.velocity = this.velocity;
        collisionEntity.userData = this;
    }

    void fire() {
        fire = true;
    }

    void setPosition(float x, float y) {
        dstPosition.set(x, y);
    }

    public void preUpdate(float dt) {

    }

    public void update(float dt) {
        if(!fire) {
            Vector2 dirToTarget = new Vector2(dstPosition);
            dirToTarget.sub(position);
            float len = dirToTarget.len();
            dirToTarget.setLength(len / 4f);
            position.set(position.x + dirToTarget.x, position.y + dirToTarget.y);
        }

        position.mulAdd(velocity, dt);
        collisionRectangle.setCenter(position.x, position.y);
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
