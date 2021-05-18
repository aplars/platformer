package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.DamageComponent;
import com.sa.game.components.ExplodeOnContactComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.WorldConstantsComponent;
import com.sa.game.components.groups.BoxingGloveGroupComponent;
import com.sa.game.gfx.Sprite;

public class BoxingGlove {
    public static Entity create(String name, Vector2 position, Vector2 vel, float size, final Animation<TextureRegion> idleAnimation, Entity parentEntity, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        Entity entity = new Entity();
        entity.flags = EntityType.BoxingGlove.type;

        BoxingGloveGroupComponent boxingGloveGroupComponent = new BoxingGloveGroupComponent();

        WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size/2.0f, size/2.0f);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.PLAYER_PROJECTILE;
        collisionEntity.filter.mask = CollisionFilter.ENEMY;

        collisionDetection.add(collisionEntity);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = 0;
        physicsComponent.airResistance.set(1, 1);
        physicsComponent.friction = 1;
        physicsComponent.velocity.set(vel);

        ExplodeOnContactComponent explodeOnContactComponent = new ExplodeOnContactComponent(null);

        DamageComponent damageComponent = new DamageComponent();
        damageComponent.stun = true;
        damageComponent.stunTime = 5f;

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.layer = 3;
        renderComponent.sprite.size.set(size, size);
        renderComponent.sprite.textureRegion = idleAnimation.getKeyFrame(0);

        RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();

        entity.add(boxingGloveGroupComponent);
        entity.add(worldConstantsComponent);
        entity.add(collisionComponent);
        entity.add(positionComponent);
        entity.add(physicsComponent);
        entity.add(explodeOnContactComponent);
        entity.add(damageComponent);
        entity.add(renderComponent);
        entity.add(renderDebugInfoComponent);
        return entity;
    }
}
