package com.sa.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.gfx.PlayerWeaponAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;

public class PlayerWeapon {
    ShapeRenderer shapeRenderer;
    CollisionEntity collisionEntity;
    public Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
    public Vector2 dstPosition = new Vector2();
    float gravity = 0f;

    private Vector2 acceleration = new Vector2();
    boolean fire = false;

    SpriteBatch spriteBatch;
    float currentTime = 0f;
    TextureRegion currentFrame;
    WalkDirection walkDirection = WalkDirection.Left;
    int numCollisionsBeforeBreakage = 2;
    int numCollisions = 0;
    public boolean isDead = false;
    PlayerWeaponAnimations playerWeaponAnimations;

    public PlayerWeapon(Vector2 position, Vector2 velocity, float size, PlayerWeaponAnimations playerWeaponAnimations, CollisionDetection collisionDetection) {
        shapeRenderer = new ShapeRenderer();

        this.position.set(position);
        this.dstPosition.set(position);
        this.velocity.set(velocity);

        float jumpTime = 0.5f;
        gravity = -2*(32f*5f+6)/(float)Math.pow(jumpTime, 2f);

        Rectangle collisionRectangle = new Rectangle(0, 0, size, size);
        collisionRectangle.setCenter(position.x, position.y);

        collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity = this.velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        spriteBatch = new SpriteBatch();
        playerWeaponAnimations.setCurrentAnimation(PlayerWeaponAnimations.AnimationType.Stunned);
        currentFrame = playerWeaponAnimations.getKeyFrame();;

    }


    void fire(WalkDirection direction) {
        fire = true;
        if(direction == WalkDirection.Left)
            velocity.x = -800;
        else
            velocity.x = 800;
    }

    void setPosition(float x, float y) {
        dstPosition.set(x, y);
    }

    public void preUpdate(float dt) {
        if(fire) {
            acceleration.y = gravity;
            velocity.mulAdd(acceleration, dt);
        }
    }

    public void update(float dt, StaticEnvironment staticEnvironment) {
        if(!fire) {
            Vector2 dirToTarget = new Vector2(dstPosition);
            dirToTarget.sub(position);
            float len = dirToTarget.len();
            dirToTarget.setLength(len / 4f);
            position.set(position.x + dirToTarget.x, position.y + dirToTarget.y);
        }
        else {
            FloorCollisionData groundCollisionData = IntersectionTests.rectangleGround(dt, collisionEntity.box, velocity, staticEnvironment);
            WallCollisionData wallsCollisionData = IntersectionTests.rectangleWalls(dt, collisionEntity.box, velocity, staticEnvironment);

            if (groundCollisionData !=null && groundCollisionData.didCollide) {
                if (velocity.y < 0) {
                    velocity.y = 0;//-collissionData.move;
                    position.add(groundCollisionData.move);
                }
            }
            if (wallsCollisionData != null && wallsCollisionData.didCollide) {
                velocity.x = -velocity.x;
                //velocity.x = 0;
                position.sub(wallsCollisionData.move);
                numCollisions++;

                if(numCollisions >= numCollisionsBeforeBreakage) {
                    isDead = true;
                }
            }
        }

        position.mulAdd(velocity, dt);
        collisionEntity.box.setCenter(position.x, position.y);
    }
    Sprite sprite = new Sprite();

    public void render(float t, Sprites sprites) {

        sprite.textureRegion.setRegion(currentFrame);
        sprite.position.set(collisionEntity.box.x, collisionEntity.box.y);
        sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        sprite.mirrorX = (walkDirection == WalkDirection.Left);
        sprites.add(sprite);
    }

}
