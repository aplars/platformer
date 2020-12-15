package com.sa.game.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.states.PlayerStunProjectileState;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.*;
import com.sa.game.gfx.Sprite;

public class PlayerStunProjectile {


    public static Entity create(Vector2 position, Vector2 velocity, final Animation<TextureRegion> onTrackAnimation, final Animation<TextureRegion> explodeAnimation, int tileSizeInPixels, CollisionDetection collisionDetection) {
        Entity entity = new Entity();
        CollisionEntity collisionEntity = new CollisionEntity();
        PhysicsComponent physicsComponent;
        PositionComponent positionComponent;
        CollisionComponent collisionComponent;
        StateComponent<PlayerStunProjectileState> stateComponent;
        AnimationComponent<PlayerStunProjectileState> animationComponent;
        RenderComponent renderComponent;

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
        collisionEntity.velocity.set(velocity);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.PLAYER_PROJECTILE;
        collisionEntity.filter.mask = CollisionFilter.ENEMY;
        collisionDetection.add(collisionEntity);

        collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        ExplodeOnContactComponent explodeOnContactComponent = new ExplodeOnContactComponent();

        stateComponent = new StateComponent<>();
        stateComponent.state = PlayerStunProjectileState.OnTrack;

        animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(PlayerStunProjectileState.OnTrack, onTrackAnimation);
        animationComponent.animations.put(PlayerStunProjectileState.Explode, explodeAnimation);

        renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.position.set(collisionComponent.entity.box.x, collisionComponent.entity.box.y);
        renderComponent.sprite.size.set(collisionComponent.entity.box.width, collisionComponent.entity.box.height);

        DamageComponent damageComponent = new DamageComponent();
        damageComponent.stun = true;

        entity.add(damageComponent);
        entity.add(physicsComponent);
        entity.add(positionComponent);
        entity.add(collisionComponent);

        entity.add(physicsComponent);
        entity.add(positionComponent);
        entity.add(collisionComponent);
        entity.add(explodeOnContactComponent);
        entity.add(stateComponent);
        entity.add(animationComponent);
        entity.add(renderComponent);

        return entity;
    }
}
