package com.sa.game.entities;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.components.*;
import com.sa.game.gfx.PlayerAnimations;
import com.sa.game.gfx.PlayerWeaponAnimations;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;
import com.sa.game.gfx.PlayerAnimations.PlayerState;

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

    public boolean fire = false;

    private final float timeToWaitUntilNextFire = 0.2f;
    private float fireTimer = 0f;

    State state = State.Alive;


    TextureRegion currentFrame;
    Sprite sprite = new Sprite();

    PickedUpEntity pickedUpEntity = null;

    Entity preUpdateEntity;
    Entity updateEntity;

    Player1Component player1Component;
    PositionComponent positionComponent;
    PhysicsComponent physicsComponent;
    EntityControlComponent entityControlComponent;
    CollisionComponent collisionComponent;
    StateComponent<PlayerState> stateComponent;
    AnimationComponent<PlayerState> animationComponent;
    RenderComponent renderComponent;

    Engine preUpdateEngine;
    Engine updateEngine;

    public Player(Vector2 pos, Vector2 vel, Vector2 size, final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, Engine preUpdatyeEngine, Engine updateEngine) {
        Rectangle box = new Rectangle(0, 0, size.x, size.y);
        box.setCenter(pos.x, pos.y);

        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(box);
        collisionEntity.velocity = vel;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);


        this.preUpdateEngine = preUpdatyeEngine;
        this.updateEngine = updateEngine;

        player1Component = new Player1Component();

        float jumpTime = 0.5f;
        physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        physicsComponent.velocity.set(vel);

        entityControlComponent = new EntityControlComponent();
        entityControlComponent.jumpImpulse = 2f*(staticEnvironment.tileSizeInPixels*5f+2)/jumpTime;
        entityControlComponent.moveForce = 30*staticEnvironment.tileSizeInPixels*physicsComponent.mass;

        positionComponent = new PositionComponent();
        positionComponent.position.set(pos);

        collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        stateComponent = new StateComponent<>();
        stateComponent.state = PlayerState.Idle;

        animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(PlayerState.Idle, idleAnimation);
        animationComponent.animations.put(PlayerState.Walk, walkAnimation);

        renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);

        preUpdateEntity = new Entity();
        preUpdateEntity.add(physicsComponent);
        preUpdateEntity.add(entityControlComponent);
        preUpdateEntity.add(collisionComponent);
        preUpdateEntity.add(positionComponent);
        preUpdateEntity.add(player1Component);

        updateEntity = new Entity();
        updateEntity.add(physicsComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(stateComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(renderComponent);






        this.preUpdateEngine.addEntity(preUpdateEntity);
        this.updateEngine.addEntity(updateEntity);
    }

    public void warpToTop(float worldBoundY) {
        positionComponent.position.y = worldBoundY;
        if(pickedUpEntity != null) {
            pickedUpEntity.dstPosition.y = positionComponent.position.y - 40;
            pickedUpEntity.position.y = pickedUpEntity.dstPosition.y;
        }
    }

    public void handleInput(float dt, Controller controller) {
        fire = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W) && pickedUpEntity != null) {
            pickedUpEntity.fire(physicsComponent.walkDirection);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && fireTimer == 0) {
            fire = true;
            fireTimer = timeToWaitUntilNextFire;
        }

        fireTimer-=dt;
        fireTimer = Math.max(0, fireTimer);
    }

    public void preUpdate(float dt) {
        if(pickedUpEntity != null)
            pickedUpEntity.preUpdate(dt);
    }

    public void update(float dt, AssetManager assetManager, CollisionDetection collisionDetection, int tileSizeInPixels, PlayerStunProjectiles playerStunProjectiles, PickedUpEntities weapons, Enemies enemies) {

        for(CollisionEntity collidee : collisionComponent.entity.collidees) {
            if(collidee.userData instanceof Enemy) {
                Enemy enemy = (Enemy) collidee.userData;
                if(enemy.isStunned) {
                    //We shall pick up the enemy.

                    //Remove it from the enemy list and add it as player weapon.
                    enemy.isShoot = true;
                    if(pickedUpEntity == null) {
                        //PlayerWeaponAnimations playerWeaponAnimations = new PlayerWeaponAnimations(enemy.animations.getCurrentAnimation());
                        //pickedUpEntity = CreateEnteties.playerWeapon(playerWeaponAnimations, positionComponent.position, physicsComponent.velocity, enemy.size, collisionDetection);
                    }

                }
                else {
                    state = State.Dying;
                }
            }
        }

        if(Math.abs(physicsComponent.velocity.x) > 1f) {
        }
        //if(fire && pickedUpEntity == null) {
        //    float projDir = (physicsComponent.walkDirection == WalkDirection.Left) ? -300f : 300f;
        //    playerStunProjectiles.add(CreateEnteties.playerStunProjectile(assetManager, new Vector2(positionComponent.position), new Vector2(projDir, 0f), tileSizeInPixels, collisionDetection, preUpdateEngine, updateEngine));
        //}
        //else
        if(fire && pickedUpEntity != null) {
            weapons.add(pickedUpEntity);
            pickedUpEntity = null;
        }
        else if(pickedUpEntity != null) {
            pickedUpEntity.setPosition(positionComponent.position.x, positionComponent.position.y +  this.collisionComponent.entity.box.height*0.8f);
            pickedUpEntity.update(dt);
        }
    }

    public void render(float t, Sprites sprites) {
        if(pickedUpEntity != null) {
            pickedUpEntity.render(t, sprites);
        }
        /*
        sprite.textureRegion.setRegion(currentFrame);
        sprite.position.set(collisionComponent.entity.box.x, collisionComponent.entity.box.y);
        sprite.size.set(collisionComponent.entity.box.width, collisionComponent.entity.box.height);
        sprite.mirrorX = physicsComponent.walkDirection == WalkDirection.Left;
        sprites.add(sprite);*/
    }

}
