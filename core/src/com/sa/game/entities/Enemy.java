package com.sa.game.entities;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.statemachines.EnemyState;

public class Enemy {
    public enum XDirection{
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
    float size;
    float gravity = 26f*32;
    private Vector2 acceleration = new Vector2();
    private float _moveAcceleration = 30*32;
    private Vector2 moveAcceleration = new Vector2();
    boolean isOnGround = false;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Rectangle collisionRectangle;
    public CollisionEntity collisionEntity;

    private float friction = 0.1f;
    private float airResistance = 0.2f;

    public boolean isShoot = false;
    public DefaultStateMachine<Enemy, EnemyState>  stateMachine;
    public  String name;

    public Enemy(String name, Vector2 position, float size, CollisionDetection collisionDetection) {
        this.name = name;
        this.position.set(position);
        this.size = size;
        float jumpTime = 0.5f;
        gravity = -2*(32f*5f+6)/(float)Math.pow(jumpTime, 2f);

        collisionRectangle = new Rectangle(0, 0, size, size);
        collisionRectangle.setCenter(position.x, position.y);

        collisionEntity = new CollisionEntity();
        collisionEntity.box = collisionRectangle;
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        
        collisionDetection.add(collisionEntity);

        stateMachine = new DefaultStateMachine<>(this, EnemyState.RESTING);
    }

    public void moveLeft(float dt) {
        moveAcceleration.set(-_moveAcceleration, 0f);
    }

    public void moveRight(float dt) {
        moveAcceleration.set(_moveAcceleration, 0f);
    }

    public void idle(float dt) {
        moveAcceleration.set(0f, 0f);
    }

    public void preUpdate(float dt) {

        acceleration.x = moveAcceleration.x;
        acceleration.y = moveAcceleration.y + gravity;

        velocity.mulAdd(acceleration, dt);
    }
    public void update(float dt, FloorCollisionData groundCollisionData, WallCollisionData wallCollisionData, StaticEnvironment staticEnvironment) {
        isOnGround = false;


        if(groundCollisionData.didCollide)  {
            if(velocity.y < 0) {
                velocity.y = 0;//-collissionData.move;
                position.add(groundCollisionData.move);
                isOnGround = true;
            }
        }
        if(wallCollisionData.didCollide) {
            velocity.x = 0;
            position.sub(wallCollisionData.move);
        }

        position.mulAdd(velocity, dt);
        collisionRectangle.setCenter(position);
        stateData.dt = dt;
        stateData.floorCollision = groundCollisionData.didCollide;
        stateData.wallCollision = wallCollisionData.didCollide;
        stateData.staticEnvironment = staticEnvironment;
        stateMachine.update();

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
