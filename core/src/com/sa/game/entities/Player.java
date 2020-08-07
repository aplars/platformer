package com.sa.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import javax.lang.model.type.NullType;

import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.WallCollisionData;

public class Player {
    enum State {
        Alive,
        Dying,
        Dead
    }

    enum AliveState {
        Idle,
        WalkLeft,
        WalkRight,
        PickupWeapon
    }

    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public  Vector2 size = new Vector2();
    public Rectangle collisionRectangle;
    public CollisionEntity collisionEntity;
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

    State state = State.Alive;
    AliveState aliveState = AliveState.Idle;
    float dyingTimer = 4f;

    TextureAtlas textureAtlas;
    Animation<TextureRegion> walkAnimation;
    SpriteBatch spriteBatch;
    TextureRegion currentFrame;
    boolean flipSprite = false;
    float currentTime = 0f;

    Weapon weapon = null;

    public Player(Vector2 pos, Vector2 vel, Vector2 siz, CollisionDetection collisionDetection) {
        position.set(pos);
        velocity.set(vel);
        size.set(siz);

        collisionRectangle = new Rectangle(0, 0, size.x, size.y);
        collisionRectangle.setCenter(position.x, position.y);

        collisionEntity = new CollisionEntity();
        collisionEntity.box = collisionRectangle;
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        float jumpTime = 0.5f;
        gravity = -2*(32f*5f+6)/(float)Math.pow(jumpTime, 2f);
        _jumpVelocity = 2f*(32f*5f+6)/jumpTime;
        _maxJumpVelocity = _jumpVelocity;

        textureAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        walkAnimation = new Animation<TextureRegion>(1 / 60f * 6f, textureAtlas.findRegions("player"), PlayMode.LOOP);
        spriteBatch = new SpriteBatch();
        currentFrame = walkAnimation.getKeyFrame(currentTime, true);
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
        aliveState = AliveState.Idle;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveAcceleration.x = -_moveAcceleration;
            aliveState = AliveState.WalkLeft;
            didPressKey = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveAcceleration.x = _moveAcceleration;
            aliveState = AliveState.WalkRight;
            didPressKey = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && weapon != null) {
            weapon.fire();
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

        if(weapon != null)
            weapon.preUpdate(dt);
    }

    public void update(float dt, CollisionDetection collisionDetection, FloorCollisionData groundCollissionData, WallCollisionData wallCollissionData, StaticEnvironment staticEnvironment, PlayerProjectiles projectiles, Enemies enemies) {
        isOnGround = false;

        for(CollisionEntity collidee : collisionEntity.collidees) {
            if(collidee.userData instanceof Enemy) {
                Enemy enemy = (Enemy) collidee.userData;
                if(enemy.stateData.isStunned) {
                    //We shall pick up the enemy.

                    //Remove it from the enemy list and add it as player weapon.
                    enemies.remove(enemy);
                    collisionDetection.remove(enemy.collisionEntity);
                    weapon = new Weapon(new Vector2(position), new Vector2(velocity), enemy.size, collisionDetection);
                    //Set the weapons velocity to zero
                    weapon.velocity.setZero();
                    //Run the add weapon sequence.
                    aliveState = AliveState.PickupWeapon;

                }
                else {
                    state = State.Dying;
                }
            }
        }

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

        if(Math.abs(moveAcceleration.x) > 1f)
            currentFrame = walkAnimation.getKeyFrame(currentTime, true);
        if(aliveState == AliveState.WalkLeft)
            flipSprite = true;
        else if(aliveState == AliveState.WalkRight)
            flipSprite = false;

        if(fire && weapon == null) {
            projectiles.add(new PlayerProjectile(new Vector2(position), new Vector2(0, dt * 600f), collisionDetection));
        }
        else  if(fire && weapon != null) {
            weapon.velocity.set(600f, gravity/8f);
            weapon.update(dt);
        }
        else if(fire == false && weapon != null) {
            weapon.setPosition(position.x, position.y + 40);
            weapon.update(dt);
        }
        currentTime += dt;
    }

    public void render(float t, OrthographicCamera camera) {
        /*shapeRenderer.setProjectionMatrix(camera.projection);
        Matrix4 translate = new Matrix4();
        Matrix4 matrix = new Matrix4();

        matrix.set(camera.view);
        translate.setToTranslation(collisionRectangle.x, collisionRectangle.y, 0f);

        matrix.mulLeft(translate);

        shapeRenderer.setTransformMatrix(matrix );
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
        shapeRenderer.rect(0, 0, collisionRectangle.width, collisionRectangle.height);
        shapeRenderer.end();
        */
        if(aliveState == AliveState.PickupWeapon) {
            
        }

        if(weapon != null) {
            weapon.render(t, camera);
        }

        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame,
                flipSprite ? collisionRectangle.x + collisionRectangle.width : collisionRectangle.x, collisionRectangle.y,
                flipSprite ? -collisionRectangle.width : collisionRectangle.width, collisionRectangle.height); // Draw current frame at (50, 50)
        spriteBatch.end();

    }

}
