package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.AIComponent;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.DamageComponent;
import com.sa.game.components.DelayControlComponent;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.RenderStarsComponent;
import com.sa.game.components.WorldConstantsComponent;
import com.sa.game.components.groups.EnemyGroupComponent;
import com.sa.game.gfx.Sprite;
import com.sa.game.statemachines.DevoDevilStates;

public class Enemy {
    public static Entity create(String name, float startDelay, Vector2 position, float size,
                                final Animation<TextureRegion> idleAnimation,
                                final Animation<TextureRegion> walkAnimation,
                                final Animation<TextureRegion> stunnedAnimation,
                                final Animation<TextureRegion> starAnimation,
                                StaticEnvironment staticEnvironment,
                                CollisionDetection collisionDetection) {
        Entity entity = new Entity();
        entity.flags = EntityType.Enemy.type;

        EnemyGroupComponent enemyGroupComponent = new EnemyGroupComponent();

        WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size*0.9f, size*0.8f);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.ENEMY;
        collisionDetection.add(collisionEntity);

        ControlComponent controlComponent = new ControlComponent();

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2 * (staticEnvironment.tileSizeInPixels * 5f + 2) / (float) Math.pow(jumpTime, 2f);


        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        AnimationComponent<DevoDevilStates> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(DevoDevilStates.START, idleAnimation);
        animationComponent.animations.put(DevoDevilStates.IDLE, idleAnimation);
        animationComponent.animations.put(DevoDevilStates.STUNNED, stunnedAnimation);
        animationComponent.animations.put(DevoDevilStates.WALK, walkAnimation);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.layer = 3;
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        renderComponent.mirror = true;

        RenderStarsComponent renderStarsComponent = new RenderStarsComponent();
        renderStarsComponent.animation = starAnimation;

        DelayControlComponent delayAIComponent = new DelayControlComponent(startDelay);
        DefaultStateMachine<Entity, DevoDevilStates> stateMachine = new DefaultStateMachine<>(entity, DevoDevilStates.START);
        AIComponent<DevoDevilStates> aiComponent = new AIComponent<>(entity, stateMachine);

        HealthComponent healthComponent = new HealthComponent();

        DamageComponent damageComponent = new DamageComponent();
        damageComponent.stun = false;
        damageComponent.damage = 1;

        final RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();

        entity.add(enemyGroupComponent);
        entity.add(worldConstantsComponent);
        entity.add(controlComponent);
        entity.add(healthComponent);
        entity.add(damageComponent);
        entity.add(delayAIComponent);
        entity.add(aiComponent);
        entity.add(positionComponent);
        entity.add(physicsComponent);
        entity.add(collisionComponent);
        entity.add(positionComponent);
        entity.add(physicsComponent);
        entity.add(collisionComponent);
        entity.add(animationComponent);
        entity.add(renderComponent);
        entity.add(renderStarsComponent);
        entity.add(renderDebugInfoComponent);
        return entity;
    }
}
