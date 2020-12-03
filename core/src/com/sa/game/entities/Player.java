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
import com.sa.game.states.PlayerState;
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


    State state = State.Alive;


    TextureRegion currentFrame;
    Sprite sprite = new Sprite();



    public Player(Vector2 pos, Vector2 vel, Vector2 size, final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, Engine updateEngine) {
        Entity updateEntity;

        Rectangle box = new Rectangle(0, 0, size.x, size.y);
        box.setCenter(pos.x, pos.y);

        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(box);
        collisionEntity.velocity = vel;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        Player1Component player1Component = new Player1Component();

        ControlComponent controlComponent = new ControlComponent();

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        physicsComponent.velocity.set(vel);

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(pos);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        StateComponent<PlayerState> stateComponent = new StateComponent<>();
        stateComponent.state = PlayerState.Idle;

        AnimationComponent<PlayerState> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(PlayerState.Idle, idleAnimation);
        animationComponent.animations.put(PlayerState.Walk, walkAnimation);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);

        updateEntity = new Entity();

        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(player1Component);
        updateEntity.add(controlComponent);

        updateEntity.add(physicsComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(stateComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(renderComponent);

        updateEngine.addEntity(updateEntity);
    }
}
