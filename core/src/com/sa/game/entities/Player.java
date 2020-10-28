package com.sa.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.gfx.PlayerAnimations;
import com.sa.game.gfx.PlayerAnimations.AnimationType;
import com.sa.game.gfx.PlayerWeaponAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;

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
    public final CollisionEntity collisionEntity;
    public boolean fire = false;

    private final float timeToWaitUntilNextFire = 0.2f;
    private float fireTimer = 0f;

    private boolean isOnGround = false;
    private final float friction = 0.1f;
    private final float airResistance = 0.2f;
    private final Vector2 moveAcceleration = new Vector2();
    private final Vector2 jumpAcceleration = new Vector2();
    private final Vector2 acceleration = new Vector2();

    private final float maxSpeed = 400;

    private float gravity = 0;
    private float initialJumpVelocity = 0;
    private float initialMoveAcceleration = 0;

    State state = State.Alive;

    PlayerAnimations animations;

    TextureRegion currentFrame;

    PickedUpEntity pickedUpEntity = null;

    WalkDirection walkDirection = WalkDirection.Right;

    public Player(Vector2 pos, Vector2 vel, Vector2 size, PlayerAnimations playerAnimations, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        position.set(pos);
        velocity.set(vel);

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
        initialJumpVelocity = 2f*(staticEnvironment.tileSizeInPixels*5f+2)/jumpTime;
        initialMoveAcceleration = 30*staticEnvironment.tileSizeInPixels;

        animations.setCurrentAnimation(AnimationType.Walk);
        currentFrame = animations.getKeyFrame();
    }

    public void warpToTop(float worldBoundY) {
        position.y = worldBoundY;
        if(pickedUpEntity != null) {
            pickedUpEntity.dstPosition.y = position.y - 40;
            pickedUpEntity.position.y = pickedUpEntity.dstPosition.y;
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

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveAcceleration.x = -initialMoveAcceleration;
            walkDirection = WalkDirection.Left;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveAcceleration.x = initialMoveAcceleration;
            walkDirection = WalkDirection.Right;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && pickedUpEntity != null) {
            pickedUpEntity.fire(walkDirection);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && fireTimer == 0) {
            fire = true;
            fireTimer = timeToWaitUntilNextFire;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && isOnGround) {
            velocity.y = initialJumpVelocity;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P) && isOnGround) {

        }

        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        }

        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()>0) {
            moveAcceleration.x = initialMoveAcceleration;
            walkDirection = WalkDirection.Right;
        }

        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()<0) {
            moveAcceleration.x = -initialMoveAcceleration;
            walkDirection = WalkDirection.Left;
        }

        if(Gdx.input.isTouched()&&Gdx.input.getDeltaX()>0) {
            moveAcceleration.x = initialMoveAcceleration;
            walkDirection = WalkDirection.Right;
        }


        if(controller != null) {
            if (controller.getAxis(0) < -0.5) {
                moveAcceleration.x = -initialMoveAcceleration;
            }
            if (controller.getAxis(0) > 0.5) {
                moveAcceleration.x = +initialMoveAcceleration;
            }
            if (controller.getButton(1) && isOnGround) {
                velocity.y = initialJumpVelocity;
            }

            if (!controller.getButton(1)) {
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

        if(pickedUpEntity != null)
            pickedUpEntity.preUpdate(dt);
    }

    public void update(float dt, AssetManager assetManager, CollisionDetection collisionDetection, int tileSizeInPixels, PlayerStunProjectiles playerStunProjectiles, PickedUpEntities weapons, Enemies enemies) {
        isOnGround = false;

        for(CollisionEntity collidee : collisionEntity.collidees) {
            if(collidee.userData instanceof Enemy) {
                Enemy enemy = (Enemy) collidee.userData;
                if(enemy.stateData.isStunned) {
                    //We shall pick up the enemy.

                    //Remove it from the enemy list and add it as player weapon.
                    enemy.isShoot = true;
                    if(pickedUpEntity == null) {
                        PlayerWeaponAnimations playerWeaponAnimations = new PlayerWeaponAnimations(enemy.animations.getCurrentAnimation());
                        pickedUpEntity = CreateEnteties.playerWeapon(playerWeaponAnimations, position, velocity, enemy.size, collisionDetection);
                    }

                }
                else {
                    state = State.Dying;
                }
            }
        }

        if(collisionEntity.groundCollisionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;
                position.add(0f, collisionEntity.groundCollisionData.move.y);
            }
            isOnGround = true;
        }
        if(collisionEntity.wallsCollisionData.didCollide) {
            velocity.x = 0;
            position.sub(collisionEntity.wallsCollisionData.move);
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
        if(fire && pickedUpEntity == null) {
            float projDir = (walkDirection == WalkDirection.Left) ? -300f : 300f;
            playerStunProjectiles.add(new PlayerStunProjectile(new Vector2(position), new Vector2(projDir, 0f), tileSizeInPixels, collisionDetection));
        }
        else  if(fire && pickedUpEntity != null) {
            weapons.add(pickedUpEntity);
            pickedUpEntity = null;
        }
        else if(pickedUpEntity != null) {
            pickedUpEntity.setPosition(position.x, position.y +  this.collisionEntity.box.height*0.8f);
            pickedUpEntity.update(dt);
        }
        animations.update(dt);
    }
    Sprite sprite = new Sprite();

    public void render(float t, Sprites sprites) {
        if(pickedUpEntity != null) {
            pickedUpEntity.render(t, sprites);
        }

        sprite.textureRegion.setRegion(currentFrame);
        sprite.position.set(collisionEntity.box.x, collisionEntity.box.y);
        sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        sprite.mirrorX = walkDirection == WalkDirection.Left;
        sprites.add(sprite);
    }

}
