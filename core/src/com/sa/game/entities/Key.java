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
import com.sa.game.components.HealthComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.WorldConstantsComponent;
import com.sa.game.components.groups.KeyGroupComponent;
import com.sa.game.components.groups.ToolGroupComponent;
import com.sa.game.gfx.Sprite;

public class Key {
    public static Entity create(String name, Vector2 position, float size, final Animation<TextureRegion> idleAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        Entity entity = new Entity();
        entity.flags = EntityType.Key.type;

        ToolGroupComponent toolGroupComponent = new ToolGroupComponent();
        KeyGroupComponent keyGroupComponent = new KeyGroupComponent();

        WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();
        worldConstantsComponent.width = staticEnvironment.getWorldBoundX();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size, size);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.OBJECT;
        collisionDetection.add(collisionEntity);

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        HealthComponent healthComponent = new HealthComponent();
        healthComponent.stun = 0; //0 because we always want to pick up the key. No need to hit it first.

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.layer = 3;
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        renderComponent.sprite.textureRegion = idleAnimation.getKeyFrame(0);

        RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();

        entity.add(keyGroupComponent);
        entity.add(toolGroupComponent);
        entity.add(worldConstantsComponent);
        entity.add(positionComponent);
        entity.add(physicsComponent);
        entity.add(collisionComponent);
        entity.add(healthComponent);
        entity.add(renderComponent);
        entity.add(renderDebugInfoComponent);

        return entity;
    }
}
