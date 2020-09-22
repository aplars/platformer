package com.sa.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.gfx.PlayerAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;
import com.sa.game.gfx.PlayerAnimations.AnimationType;

public class Player {
    enum State {
        Alive,
        Dying,
        Dead
    }

    enum AliveState {
        Idle,
        PickupWeapon
    }

    public final Vector2 position = new Vector2();
    public final Vector2 velocity = new Vector2();
    public  final Vector2 size = new Vector2();
    public final CollisionEntity collisionEntity;
    public boolean fire = false;

    private final float timeToWaitUntilNextFire = 0.2f;
    private float fireTimer = 0f;

    private boolean isOnGround = false;
    private boolean isJumpButtonPressed = false;
    private float jumpTime;
    private final float maxJumpTime = 0.3f; //one second
    private final float friction = 0.1f;
    private final float airResistance = 0.2f;
    private final Vector2 moveAcceleration = new Vector2();
    private final Vector2 jumpAcceleration = new Vector2();
    private final Vector2 acceleration = new Vector2();

    private final float maxSpeed = 400;

    private float gravity = 0;
    private float _jumpVelocity = 0;
    private float _moveAcceleration = 0;

    State state = State.Alive;
    AliveState aliveState = AliveState.Idle;

    //TextureAtlas textureAtlas;
    //Animation<TextureRegion> walkAnimation;

    PlayerAnimations animations;

    SpriteBatch spriteBatch;
    TextureRegion currentFrame;

    PlayerWeapon weapon = null;

    WalkDirection walkDirection = WalkDirection.Right;

    public Player(Vector2 pos, Vector2 vel, Vector2 siz, PlayerAnimations playerAnimations, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        position.set(pos);
        velocity.set(vel);
        size.set(siz);

        Rectangle box = new Rectangle(0, 0, size.x, size.y);
        box.setCenter(position.x, position.y);

        animations = playerAnimations;

        collisionEntity = new CollisionEntity();
        collisionEntity.box.set(box);
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        float jumpTime = 0.5f;
        gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        _jumpVelocity = 2f*(staticEnvironment.tileSizeInPixels*5f+2)/jumpTime;
        _moveAcceleration = 30*staticEnvironment.tileSizeInPixels;

        //textureAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        //walkAnimation = new Animation<TextureRegion>(1 / 60f * 6f, textureAtlas.findRegions("walk"), PlayMode.LOOP);
        spriteBatch = new SpriteBatch();
        //currentFrame = walkAnimation.getKeyFrame(currentTime, true);

        animations.setCurrentAnimation(AnimationType.Walk);
        currentFrame = animations.getKeyFrame();
    }

    public void warpToTop(StaticEnvironment staticEnvironment) {
        position.y = staticEnvironment.getWorldBoundY();
        if(weapon != null) {
            weapon.dstPosition.y = position.y - 40;
            weapon.position.y = weapon.dstPosition.y;
        }
    }

    public void handleInput(float dt, Controller controller) {
        moveAcceleration.y = 0;
        moveAcceleration.x = 0;
        jumpAcceleration.x = 0;
        jumpAcceleration.y = 0;
        acceleration.x = 0;
        acceleration.y = 0;
        fire = false;

        aliveState = AliveState.Idle;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveAcceleration.x = -_moveAcceleration;
            walkDirection = WalkDirection.Left;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveAcceleration.x = _moveAcceleration;
            walkDirection = WalkDirection.Right;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && weapon != null) {
            weapon.fire(walkDirection);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && fireTimer == 0) {
            fire = true;
            fireTimer = timeToWaitUntilNextFire;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && isOnGround) {
            velocity.y = _jumpVelocity;
            isJumpButtonPressed = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P) && isOnGround) {

        }

        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            isJumpButtonPressed = false;
        }

        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()>0) {
            moveAcceleration.x = _moveAcceleration;
            walkDirection = WalkDirection.Right;
        }

        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()<0) {
            moveAcceleration.x = -_moveAcceleration;
            walkDirection = WalkDirection.Left;
        }

        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()>0) {
            moveAcceleration.x = _moveAcceleration;
            walkDirection = WalkDirection.Right;
        }


        if(controller != null) {
            if (controller.getAxis(0) < -0.5) {
                moveAcceleration.x = -_moveAcceleration;
            }
            if (controller.getAxis(0) > 0.5) {
                moveAcceleration.x = +_moveAcceleration;
            }
            if (controller.getButton(1) && isOnGround) {
                velocity.y = _jumpVelocity;
                isJumpButtonPressed = true;
            }

            if (!controller.getButton(1)) {
                isJumpButtonPressed = false;
            }

            if(controller.getButton(2) && fireTimer <= 0) {
                fire = true;
                fireTimer = timeToWaitUntilNextFire;
            }

        }

        fireTimer-=dt;
        fireTimer = Math.max(0, fireTimer);
    }

    public void preUpdate(float dt) {
        acceleration.set(moveAcceleration);
        acceleration.add(jumpAcceleration);
        acceleration.add(0, gravity);

        velocity.mulAdd(acceleration, dt);

        if(weapon != null)
            weapon.preUpdate(dt);
    }

    public void update(float dt, CollisionDetection collisionDetection, StaticEnvironment staticEnvironment, PlayerProjectiles projectiles, PlayerWeapons weapons, Enemies enemies) {
        isOnGround = false;

        FloorCollisionData groundCollisionData = IntersectionTests.rectangleGround(dt, collisionEntity.box, velocity, staticEnvironment);
        WallCollisionData wallsCollisionData = IntersectionTests.rectangleWalls(dt, collisionEntity.box, velocity, staticEnvironment);

        for(CollisionEntity collidee : collisionEntity.collidees) {
            if(collidee.userData instanceof Enemy) {
                Enemy enemy = (Enemy) collidee.userData;
                if(enemy.stateData.isStunned) {
                    //We shall pick up the enemy.

                    //Remove it from the enemy list and add it as player weapon.
                    enemies.remove(enemy);
                    collisionDetection.remove(enemy.collisionEntity);
                    weapon = new PlayerWeapon(new Vector2(position), new Vector2(0f, 0f), enemy.size, collisionDetection);
                    //Run the add weapon sequence.
                    aliveState = AliveState.PickupWeapon;

                }
                else {
                    state = State.Dying;
                }
            }
        }

        if(groundCollisionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;
                position.add(groundCollisionData.move);
            }
            isOnGround = true;
        }
        if(wallsCollisionData.didCollide) {
            velocity.x = 0;
            position.sub(wallsCollisionData.move);
        }

        if(Math.abs(velocity.x) > maxSpeed) {
            if(velocity.x > 0)
                velocity.x = maxSpeed;
            else
                velocity.x = -maxSpeed;
        }

        position.mulAdd(velocity, dt);
        collisionEntity.box.setCenter(position);

        if(isOnGround)
            velocity.x /= (1+friction);
        else
            velocity.x /= (1+airResistance);
        velocity.y /= (1+0.0);

        if(Math.abs(moveAcceleration.x) > 1f) {
            currentFrame = animations.getKeyFrame();
        }
        if(fire && weapon == null) {
            float projDir = (walkDirection == WalkDirection.Left) ? -300f : 300f;
            projectiles.add(new PlayerProjectile(new Vector2(position), new Vector2(projDir, 0f), staticEnvironment, collisionDetection));
        }
        else  if(fire && weapon != null) {
            weapons.add(weapon);
            weapon = null;
        }
        else if(weapon != null) {
            weapon.setPosition(position.x, position.y +  this.collisionEntity.box.height*0.8f);
            weapon.update(dt, staticEnvironment);
        }
        animations.update(dt);
    }
    Sprite sprite = new Sprite();

    public void render(float t, Sprites sprites) {
        if(weapon != null) {
            weapon.render(t, sprites);
        }

        sprite.textureRegion.setRegion(currentFrame);
        sprite.position.set(collisionEntity.box.x, collisionEntity.box.y);
        sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        sprite.mirrorX = walkDirection == WalkDirection.Left;
        sprites.add(sprite);
    }

}
