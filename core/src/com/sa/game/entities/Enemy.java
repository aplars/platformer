package com.sa.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.statemachines.EnemyState;
import com.sa.game.gfx.EnemyAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;

public class Enemy {
    public enum XDirection{
        Idle,
        Left,
        Right;
        };

    public class EnemyStateData {
        public float dt;
        public boolean wallCollision = false;
        public boolean floorCollision = false;
        public StaticEnvironment staticEnvironment;
        public float restTime = MathUtils.random(3f, 5f);
        public XDirection currentDirection = XDirection.Left;
        public float stunnedTime = 5f;
        public boolean isStunned = false;
    }
    public EnemyStateData stateData = new EnemyStateData();
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    float size = 0f;
    float gravity = 26f*32;
    private final Vector2 acceleration = new Vector2();
    private float _moveAcceleration = 0f;
    private Vector2 moveAcceleration = new Vector2();
    boolean isOnGround = false;
    public CollisionEntity collisionEntity = new CollisionEntity();

    private float friction = 0.1f;
    //private float airResistance = 0.2f;

    public boolean isShoot = false;
    public DefaultStateMachine<Enemy, EnemyState>  stateMachine;
    public  String name;

    EnemyAnimations animation;

    SpriteBatch spriteBatch;
    TextureRegion currentFrame;
    WalkDirection walkDirection = WalkDirection.Right;

    public Enemy(String name, Vector2 position, float size, EnemyAnimations enemyAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        this.name = name;
        this.position.set(position);
        this.size = size;
        float jumpTime = 0.5f;
        gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        _moveAcceleration = 30*staticEnvironment.tileSizeInPixels;

        Rectangle collisionRectangle = new Rectangle(0, 0, size, size);
        collisionRectangle.setCenter(position.x, position.y);

        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);
        stateMachine = new DefaultStateMachine<>(this, EnemyState.RESTING);

        animation = enemyAnimation;
        currentFrame = animation.getKeyFrame();
        spriteBatch = new SpriteBatch();
    }

    public void moveLeft(float dt) {
        moveAcceleration.set(-_moveAcceleration, 0f);
        walkDirection = WalkDirection.Left;
    }

    public void moveRight(float dt) {
        moveAcceleration.set(_moveAcceleration, 0f);
        walkDirection = WalkDirection.Right;
    }

    public void idle(float dt) {
        moveAcceleration.set(0f, 0f);
    }

    public void preUpdate(float dt) {

        acceleration.x = moveAcceleration.x;
        acceleration.y = moveAcceleration.y + gravity;

        velocity.mulAdd(acceleration, dt);
    }
    public void update(float dt, StaticEnvironment staticEnvironment) {
        isOnGround = false;


        FloorCollisionData groundCollisionData = IntersectionTests.rectangleGround(dt, collisionEntity.box, velocity, staticEnvironment);
        WallCollisionData wallsCollisionData = IntersectionTests.rectangleWalls(dt, collisionEntity.box, velocity, staticEnvironment);


        if(groundCollisionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;
                position.add(groundCollisionData.move);
                isOnGround = true;
            }
        }
        if(wallsCollisionData.didCollide) {
            velocity.x = 0;
            position.sub(wallsCollisionData.move);
        }

        //Handle collision vs weapons
        for(CollisionEntity collidee : collisionEntity.collidees) {
            if(collidee.userData instanceof PlayerWeapon) {
                isShoot = true;
            }
        }

        position.mulAdd(velocity, dt);
        collisionEntity.box.setCenter(position);
        stateData.dt = dt;
        stateData.floorCollision = groundCollisionData.didCollide;
        stateData.wallCollision = wallsCollisionData.didCollide;
        stateData.staticEnvironment = staticEnvironment;
        stateMachine.update();

        if(isOnGround)
            velocity.x /= (1+friction);

        if(stateData.isStunned) {
            animation.setCurrentAnimation(EnemyAnimations.AnimationType.Stunned);
        }
        else if(stateData.currentDirection == XDirection.Idle) {
            animation.setCurrentAnimation(EnemyAnimations.AnimationType.Idle);
        }
        else {
            animation.setCurrentAnimation(EnemyAnimations.AnimationType.Walk);
        }

        animation.update(dt);
        //currentTime += dt;
    }

    Sprite sprite = new Sprite();

    public void render(float t, Sprites sprites) {
        sprite.textureRegion.setRegion(animation.getKeyFrame());
        sprite.position.set(collisionEntity.box.x, collisionEntity.box.y);
        sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        sprite.mirrorX = (walkDirection == WalkDirection.Left);
        sprites.add(sprite);
    }
}
