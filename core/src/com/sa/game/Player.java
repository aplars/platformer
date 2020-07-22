package com.sa.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    boolean isOnGround = false;
    boolean isJumpButtonPressed = false;
    float jumpTime;
    float maxJumpTime = 0.3f; //one second

    public Vector2 position = new Vector2();
    public Vector2 moveAcceleration = new Vector2();
    public Vector2 jumpAcceleration = new Vector2();
    Vector2 acceleration = new Vector2();

    float maxSpeed = 400;
    public Vector2 velocity = new Vector2();

    public  Vector2 size;
    Vector2 force = new Vector2();
    //float deceleration = 0.3f;
    float gravity = 18f*32;
    float _jumpVelocity = 8*32;
    float _maxJumpVelocity = 13*32;
    float _moveAcceleration = 30*32;
    float _damping = 0.4f;
    Rectangle collissionRectangle;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    Player(Vector2 pos, Vector2 vel, Vector2 siz) {
        position = pos;
        velocity = vel;
        size = siz;
        collissionRectangle = new Rectangle(0, 0, size.x*2f, size.y*2f);
        collissionRectangle.setCenter(position.x, position.y);
    }

    void handleInput(float dt, Controller controller) {
        moveAcceleration.y = 0;
        moveAcceleration.x = 0;
        jumpAcceleration.x = 0;
        jumpAcceleration.y = 0;
        acceleration.x = 0;
        acceleration.y = 0;
        boolean didPressKey = false;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveAcceleration.x = -_moveAcceleration;
            didPressKey = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveAcceleration.x = _moveAcceleration;
            didPressKey = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && isOnGround) {
            velocity.y = -_jumpVelocity;
            didPressKey = true;
        }

        if(controller.getAxis(0) < -0.5) {
            moveAcceleration.x = -_moveAcceleration;
            didPressKey = true;
        }
        if(controller.getAxis(0) > 0.5) {
            moveAcceleration.x = +_moveAcceleration;
            didPressKey = true;
        }
        if(controller.getButton(1) && isOnGround) {
            velocity.y = -_jumpVelocity;
            didPressKey = true;
            isJumpButtonPressed = true;
            jumpTime = 0f;
        }

        if(!controller.getButton(1)) {
            isJumpButtonPressed = false;
        }

        if(isJumpButtonPressed && !isOnGround && jumpTime < maxJumpTime) {
            velocity.y = -MathUtils.lerp(_jumpVelocity, _maxJumpVelocity, jumpTime/maxJumpTime);
            jumpTime+=dt;
        }

        if (!didPressKey){
            velocity.x /= (1+0.1);
            velocity.y /= (1+0.0);
        }
    }

    void preUpdate(float dt) {
        acceleration = moveAcceleration;
        acceleration.add(jumpAcceleration);
        acceleration.add(0, gravity);

        velocity.mulAdd(acceleration, dt);
    }

    void update(float dt, CollissionData groundCollissionData, CollissionData wallCollissionData) {
        isOnGround = false;
        if(groundCollissionData.didCollide)  {
            if(velocity.y > 0) {
                velocity.y = 0;//-collissionData.move;
                position.sub(groundCollissionData.move);
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
        collissionRectangle.setCenter(position);

        force.set(0f, 0f);

    }

    void render(float t, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.ellipse(position.x, position.y, size.x, size.y);
        shapeRenderer.circle(position.x,position.y, size.x);
        shapeRenderer.rect(collissionRectangle.x, collissionRectangle.y, collissionRectangle.width, collissionRectangle.height);
        shapeRenderer.end();

    }

}
