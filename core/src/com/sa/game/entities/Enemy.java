package com.sa.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.statemachines.EnemyState;
import com.sa.game.statemachines.GameStateMachine;

public class Enemy {
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    float size;
    float gravity = 26f*32;
    private Vector2 acceleration = new Vector2();
    private float _moveAcceleration = 30*32;
    private Vector2 moveAcceleration = new Vector2();
    boolean isOnGround = false;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Rectangle collisionRectangle;
    private float friction = 0.1f;
    private float airResistance = 0.2f;

    public GameStateMachine stateMachine;

    public Enemy(Vector2 position, float size) {
        this.position.set(position);
        this.size = size;
        float jumpTime = 0.5f;
        gravity = -2*(32f*5f+6)/(float)Math.pow(jumpTime, 2f);

        collisionRectangle = new Rectangle(0, 0, size, size);
        collisionRectangle.setCenter(position.x, position.y);
        stateMachine = new GameStateMachine(this, EnemyState.RESTING);
    }

    public void moveLeft(float dt) {
        moveAcceleration.set(-_moveAcceleration, 0f);
    }

    public void moveRight(float dt) {
        moveAcceleration.set(_moveAcceleration, 0f);
    }

    public void preUpdate(float dt) {
        acceleration.x = moveAcceleration.x;
        acceleration.y = moveAcceleration.y + gravity;

        velocity.mulAdd(acceleration, dt);
    }
    public void update(float dt, FloorCollisionData groundCollissionData, WallCollisionData wallCollissionData, StaticEnvironment staticEnvironment) {
        isOnGround = false;
        if(groundCollissionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;//-collissionData.move;
                position.add(groundCollissionData.move);
                isOnGround = true;
            }
        }
        if(wallCollissionData.didCollide) {
            velocity.x = 0;
            position.sub(wallCollissionData.move);
        }


        position.mulAdd(velocity, dt);
        collisionRectangle.setCenter(position);

        stateMachine.update(dt, groundCollissionData.didCollide, wallCollissionData.didCollide, staticEnvironment);

        if(isOnGround)
            velocity.x /= (1+friction);

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
