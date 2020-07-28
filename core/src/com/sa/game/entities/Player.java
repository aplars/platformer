package com.sa.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.WallCollisionData;

public class Player {
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public  Vector2 size = new Vector2();
    public Rectangle collisionRectangle;
    public boolean fire = false;

    private final float timeToWaitUntilNextFire = 0.2f;
    private float fireTimer = 0f;

    private boolean isOnGround = false;
    private boolean isJumpButtonPressed = false;
    private float jumpTime;
    private float maxJumpTime = 0.3f; //one second
    private float friction = 0.1f;
    private float airResistance = 0.2f;
    private Vector2 moveAcceleration = new Vector2();
    private Vector2 jumpAcceleration = new Vector2();
    private Vector2 acceleration = new Vector2();

    private float maxSpeed = 400;

    private float gravity = 26f*32;
    private float _jumpVelocity = 8*32;
    private float _maxJumpVelocity = 13*32;
    private float _moveAcceleration = 30*32;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    ArrayList<Vector2> jumpPoints = new ArrayList<>();

    public Player(Vector2 pos, Vector2 vel, Vector2 siz) {
        position.set(pos);
        velocity.set(vel);
        size.set(siz);
        collisionRectangle = new Rectangle(0, 0, size.x, size.y);
        collisionRectangle.setCenter(position.x, position.y);

        float jumpTime = 0.5f;
        gravity = -2*(32f*5f+6)/(float)Math.pow(jumpTime, 2f);
        _jumpVelocity = 2f*(32f*5f+6)/jumpTime;
        _maxJumpVelocity = _jumpVelocity;
    }

    public void handleInput(float dt, Controller controller) {
        moveAcceleration.y = 0;
        moveAcceleration.x = 0;
        jumpAcceleration.x = 0;
        jumpAcceleration.y = 0;
        acceleration.x = 0;
        acceleration.y = 0;
        fire = false;
        boolean didPressKey = false;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveAcceleration.x = -_moveAcceleration;
            didPressKey = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveAcceleration.x = _moveAcceleration;
            didPressKey = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) && fireTimer == 0) {
            fire = true;
            fireTimer = timeToWaitUntilNextFire;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && isOnGround) {
            velocity.y = _jumpVelocity;
            didPressKey = true;
            isJumpButtonPressed = true;
            jumpTime = 0f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P) && isOnGround) {

        }

        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            isJumpButtonPressed = false;
        }
        if(controller != null) {
            if (controller.getAxis(0) < -0.5) {
                moveAcceleration.x = -_moveAcceleration;
                didPressKey = true;
            }
            if (controller.getAxis(0) > 0.5) {
                moveAcceleration.x = +_moveAcceleration;
                didPressKey = true;
            }
            if (controller.getButton(1) && isOnGround) {
                velocity.y = _jumpVelocity;
                didPressKey = true;
                isJumpButtonPressed = true;
                jumpTime = 0f;
            }

            if (!controller.getButton(1)) {
                isJumpButtonPressed = false;
            }

            if(controller.getButton(2) && fireTimer <= 0) {
                fire = true;
                fireTimer = timeToWaitUntilNextFire;
            }

        }
        if(isJumpButtonPressed && !isOnGround && jumpTime < maxJumpTime) {
            //velocity.y = -MathUtils.lerp(_jumpVelocity, _maxJumpVelocity, jumpTime/maxJumpTime);
            jumpTime+=dt;
        }

        fireTimer-=dt;
        fireTimer = Math.max(0, fireTimer);
    }

    public void preUpdate(float dt) {
        acceleration = moveAcceleration;
        acceleration.add(jumpAcceleration);
        acceleration.add(0, gravity);

        velocity.mulAdd(acceleration, dt);
    }

    public void update(float dt, FloorCollisionData groundCollissionData, WallCollisionData wallCollissionData) {
        isOnGround = false;
        if(groundCollissionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;//-collissionData.move;
                position.add(groundCollissionData.move);
            }
            isOnGround = true;
        }
        if(wallCollissionData.didCollide) {
            velocity.x = 0;
            position.sub(wallCollissionData.move);
        }

        if(Math.abs(velocity.x) > maxSpeed) {
            if(velocity.x > 0)
                velocity.x = maxSpeed;
            else
                velocity.x = -maxSpeed;
        }

        position.mulAdd(velocity, dt);
        collisionRectangle.setCenter(position);

        if(isOnGround)
            velocity.x /= (1+friction);
        else
            velocity.x /= (1+airResistance);
        velocity.y /= (1+0.0);

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
