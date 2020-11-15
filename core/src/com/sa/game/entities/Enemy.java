package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
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
import com.sa.game.gfx.EnemyAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;

public class Enemy {
    public enum XDirection{
        Idle,
        Left,
        Right;
    };

    Entity entity;
    //   public class EnemyStateData {
    public float dt;
    public boolean wallCollision = false;
    public boolean floorCollision = false;
    public StaticEnvironment staticEnvironment;
    public float restTime = MathUtils.random(3f, 5f);
    public XDirection currentDirection = XDirection.Left;
    public float stunnedTime = 5f;
    public boolean isStunned = false;
    private float _moveAcceleration = 0f;
    private Vector2 moveAcceleration = new Vector2();
    public WalkDirection walkDirection = WalkDirection.Right;
    
    public CollisionEntity collisionEntity = new CollisionEntity();
    public void moveLeft() {
        moveAcceleration.set(-_moveAcceleration, 0f);
        walkDirection = WalkDirection.Left;
    }
    
    public void moveRight() {
        moveAcceleration.set(_moveAcceleration, 0f);
        walkDirection = WalkDirection.Right;
    }
    
    public void idle() {
        moveAcceleration.set(0f, 0f);
    }
    //        public DefaultStateMachine<EnemyStateData, State<EnemyStateData>>  stateMachine;
    public DefaultStateMachine<Enemy, State<Enemy>>  stateMachine;
    //}
    //public EnemyStateData stateData = new EnemyStateData();
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    float size = 0f;
    float gravity = 26f*32;
    private final Vector2 acceleration = new Vector2();
    boolean isOnGround = false;

    private float friction = 0.1f;
    //private float airResistance = 0.2f;

    public boolean isShoot = false;
    public  String name;

    public  EnemyAnimations animations;

    TextureRegion currentFrame;

    public Enemy(String name, Vector2 position, float size, EnemyAnimations enemyAnimations, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        this.name = name;
        this.position.set(position);
        this.size = size;
        float jumpTime = 0.5f;
        gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        _moveAcceleration = 30*staticEnvironment.tileSizeInPixels;

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size, size);
        collisionRectangle.setCenter(position.x, position.y);
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        this.stateMachine = stateMachine; 
        animations = enemyAnimations;
        currentFrame = animations.getKeyFrame();
    }

    public void preUpdate(float dt) {

        acceleration.x = moveAcceleration.x;
        acceleration.y = moveAcceleration.y + gravity;

        velocity.mulAdd(acceleration, dt);
    }
    public void update(float dt, StaticEnvironment staticEnvironment) {
        isOnGround = false;

        if(collisionEntity.groundCollisionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;
                position.add(collisionEntity.groundCollisionData.move);
                isOnGround = true;
            }
        }
        if(collisionEntity.wallsCollisionData.didCollide) {
            velocity.x = 0;
            position.sub(collisionEntity.wallsCollisionData.move);
        }

        //Handle collision vs weapons
        for(CollisionEntity collidee : collisionEntity.collidees) {
            if(collidee.userData instanceof PickedUpEntity) {
                isShoot = true;
            }
        }

        position.mulAdd(velocity, dt);
        collisionEntity.box.setCenter(position);
        collisionEntity.box.setSize(size, size);

        this.dt = dt;
        floorCollision = collisionEntity.groundCollisionData.didCollide;
        wallCollision = collisionEntity.wallsCollisionData.didCollide;
        this.staticEnvironment = staticEnvironment;
        stateMachine.update();

        if(isOnGround)
            velocity.x /= (1+friction);

        if(isStunned) {
            animations.setCurrentAnimation(EnemyAnimations.AnimationType.Stunned);
        }
        else if(currentDirection == XDirection.Idle) {
            animations.setCurrentAnimation(EnemyAnimations.AnimationType.Idle);
        }
        else {
            animations.setCurrentAnimation(EnemyAnimations.AnimationType.Walk);
        }

        animations.update(dt);
        //currentTime += dt;
    }

    Sprite sprite = new Sprite();

    public void render(float t, Sprites sprites) {
        sprite.textureRegion.setRegion(animations.getKeyFrame());
        sprite.position.set(collisionEntity.box.x, collisionEntity.box.y);
        sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        sprite.mirrorX = (walkDirection == WalkDirection.Left);
        sprites.add(sprite);
    }
}
