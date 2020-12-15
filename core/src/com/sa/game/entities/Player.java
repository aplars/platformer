package com.sa.game.entities;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.StateComponent;
import com.sa.game.gfx.Sprite;
import com.sa.game.states.PlayerState;

public class Player {
    /*enum State {
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
    */


    public static Entity create(Vector2 pos, Vector2 vel, Vector2 size, final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        Entity updateEntity = new Entity();

        Rectangle box = new Rectangle(0, 0, size.x, size.y);
        box.setCenter(pos.x, pos.y);

        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(box);
        collisionEntity.velocity = vel;
        collisionEntity.userData = updateEntity;
        collisionEntity.filter.category = CollisionFilter.PLAYER;
        collisionEntity.filter.mask = CollisionFilter.ENEMY;
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

        PickUpEntityComponent pickUpEntityComponent = new PickUpEntityComponent();

        StateComponent<PlayerState> stateComponent = new StateComponent<>();
        stateComponent.state = PlayerState.Idle;

        AnimationComponent<PlayerState> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(PlayerState.Idle, idleAnimation);
        animationComponent.animations.put(PlayerState.Walk, walkAnimation);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);

        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(player1Component);
        updateEntity.add(controlComponent);

        updateEntity.add(physicsComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(pickUpEntityComponent);
        updateEntity.add(stateComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(renderComponent);

        return updateEntity;
    }
}
