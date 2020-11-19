package com.sa.game.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.States.PlayerStunProjectileState;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.*;
import com.sa.game.gfx.Sprite;

public class PlayerStunProjectile {
    public CollisionEntity collisionEntity = new CollisionEntity();

    PhysicsComponent physicsComponent;
    PositionComponent positionComponent;
    CollisionComponent collisionComponent;
    StateComponent<PlayerStunProjectileState> stateComponent;
    PlayerStunProjectileAnimationComponent animationComponent;
    AnimationComponent<PlayerStunProjectileState> animationComponent2;
    RenderComponent renderComponent;

    Entity preUpdateEntity;
    Entity updateEntity;


    public PlayerStunProjectile(Vector2 position, Vector2 velocity, final Animation<TextureRegion> onTrackAnimation, final Animation<TextureRegion> explodeAnimation, int tileSizeInPixels, CollisionDetection collisionDetection, Engine preUpdateEngine, Engine updateEngine) {
        physicsComponent = new PhysicsComponent();
        physicsComponent.acceleration.set(0f, 0f);
        physicsComponent.gravity = 0f;
        physicsComponent.velocity.set(velocity);
        physicsComponent.airResistance = 1.0f;
        physicsComponent.friction = 1.0f;

        positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setWidth(tileSizeInPixels/4);
        collisionRectangle.setHeight(tileSizeInPixels/2);
        collisionRectangle.setCenter(position);
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity = velocity;
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity; 

        stateComponent = new StateComponent<>();
        stateComponent.state = PlayerStunProjectileState.OnTrack;

        animationComponent = new PlayerStunProjectileAnimationComponent();
        animationComponent.animations.put(PlayerStunProjectileState.OnTrack, onTrackAnimation);
        animationComponent.animations.put(PlayerStunProjectileState.Explode, explodeAnimation);

        animationComponent2 = new AnimationComponent<>();
        animationComponent2.animations.put(PlayerStunProjectileState.OnTrack, onTrackAnimation);
        animationComponent2.animations.put(PlayerStunProjectileState.Explode, explodeAnimation);

        renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.position.set(collisionComponent.entity.box.x, collisionComponent.entity.box.y);
        renderComponent.sprite.size.set(collisionComponent.entity.box.width, collisionComponent.entity.box.height);

        preUpdateEntity = new Entity();
        updateEntity = new Entity();

        preUpdateEntity.add(physicsComponent);
        preUpdateEntity.add(positionComponent);
        preUpdateEntity.add(collisionComponent);

        updateEntity.add(physicsComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(stateComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(animationComponent2);
        updateEntity.add(renderComponent);

        preUpdateEngine.addEntity(preUpdateEntity);
        updateEngine.addEntity(updateEntity);
    }
}
