package com.sa.game.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.States.EnemyState;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.components.*;
import com.sa.game.gfx.EnemyAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;
import com.sa.game.statemachines.ClownAIData;
import com.sa.game.statemachines.ClownAIState;

public class Enemy {
    public enum XDirection{
        Idle,
        Left,
        Right
    }

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
    //public Vector2 position = new Vector2();
    //public Vector2 velocity = new Vector2();
    float size = 0f;
    //float gravity = 26f*32;
    //private final Vector2 acceleration = new Vector2();
    boolean isOnGround = false;

    public boolean isShoot = false;
    public  String name;


    //TextureRegion currentFrame;

    PositionComponent positionComponent;
    PhysicsComponent physicsComponent;
    CollisionComponent collisionComponent;
    StateComponent<EnemyState> stateComponent;
    AnimationComponent<EnemyState> animationComponent;
    RenderComponent renderComponent;
    ClownAIComponent aiComponent;

    public Enemy(String name, Vector2 position, float size, final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, final Animation<TextureRegion> stunnedAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, Engine preUpdateEngine, Engine updateEngine) {
        this.name = name;
        //this.position.set(position);
        this.size = size;

        //_moveAcceleration = 30*staticEnvironment.tileSizeInPixels;

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size, size);
        collisionRectangle.setCenter(position.x, position.y);
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        //animations = enemyAnimations;
        //currentFrame = animations.getKeyFrame();

        positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        float jumpTime = 0.5f;
        physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);

        collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        stateComponent = new StateComponent<>();
        stateComponent.state = EnemyState.Idle;

        animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(EnemyState.Idle, idleAnimation);
        animationComponent.animations.put(EnemyState.Stunned, stunnedAnimation);
        animationComponent.animations.put(EnemyState.Walk, walkAnimation);

        renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);

        ClownAIData clownAIData = new ClownAIData(30*staticEnvironment.tileSizeInPixels*physicsComponent.mass, collisionEntity);
        aiComponent = new ClownAIComponent(clownAIData);

        Entity preUpdateEntity = new Entity();
        preUpdateEntity.add(aiComponent);
        preUpdateEntity.add(positionComponent);
        preUpdateEntity.add(physicsComponent);
        preUpdateEntity.add(collisionComponent);

        Entity updateEntity = new Entity();
        updateEntity.add(positionComponent);
        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(stateComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(renderComponent);

        preUpdateEngine.addEntity(preUpdateEntity);
        updateEngine.addEntity(updateEntity);
    }

    public void preUpdate(float dt) {

        //acceleration.x = moveAcceleration.x;
        //acceleration.y = moveAcceleration.y + physicsComponent.gravity;

        //velocity.mulAdd(acceleration, dt);
    }
    public void update(float dt, StaticEnvironment staticEnvironment) {
        this.dt = dt;
        floorCollision = collisionEntity.groundCollisionData.didCollide;
        wallCollision = collisionEntity.wallsCollisionData.didCollide;
        this.staticEnvironment = staticEnvironment;
        //stateMachine.update();

        //if(isOnGround)
            //velocity.x /= (1+physicsComponent.friction);

        if(isStunned) {
            //animations.setCurrentAnimation(EnemyState.Stunned);
        }
        else if(currentDirection == XDirection.Idle) {
            //animations.setCurrentAnimation(EnemyState.Idle);
        }
        else {
            //animations.setCurrentAnimation(EnemyState.Walk);
        }

        //animations.update(dt);
        //currentTime += dt;
    }

    //Sprite sprite = new Sprite();

}
