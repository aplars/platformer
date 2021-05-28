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
import com.sa.game.components.SensorComponent;
import com.sa.game.components.WorldConstantsComponent;
import com.sa.game.components.groups.EnemyGroupComponent;
import com.sa.game.gfx.Sprite;
import com.sa.game.statemachines.DevoDevilStates;

public class Enemy {
    public static Entity create(final String name, final float startDelay, final Vector2 position, final float size, final boolean isFlipped,
                                final Animation<TextureRegion> idleAnimation,
                                final Animation<TextureRegion> walkAnimation,
                                final Animation<TextureRegion> stunnedAnimation,
                                final Animation<TextureRegion> jumpAnimation,
                                final Animation<TextureRegion> starAnimation,
                                final StaticEnvironment staticEnvironment,
                                final CollisionDetection collisionDetection) {
        final Entity entity = new Entity();
        entity.flags = EntityType.Enemy.type;

        final EnemyGroupComponent enemyGroupComponent = new EnemyGroupComponent();

        final WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();

        final Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size*0.9f, size*0.8f);
        collisionRectangle.setCenter(position.x, position.y);
        final CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);

        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.ENEMY;
        collisionEntity.filter.mask &= ~CollisionFilter.ENEMY;
        collisionDetection.add(collisionEntity);

        final ControlComponent controlComponent = new ControlComponent();

        final PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        final float jumpTime = 0.25f;
        final PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -(staticEnvironment.tileSizeInPixels*5f+2)/(2f*jumpTime*jumpTime);
        physicsComponent.jumpTime = jumpTime;
        physicsComponent.airResistance.set(0.85f, 1f);
        physicsComponent.walkDirection = WalkDirection.Right;
        if(isFlipped)
            physicsComponent.walkDirection = WalkDirection.Left;
        //physicsComponent.gravity = -2 * (staticEnvironment.tileSizeInPixels * 5f + 2) / (float) Math.pow(jumpTime, 2f);


        final CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        final SensorComponent sensorComponent = new SensorComponent();

        final AnimationComponent<DevoDevilStates> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(DevoDevilStates.START_LEFT, idleAnimation);
        animationComponent.animations.put(DevoDevilStates.START_RIGHT, idleAnimation);
        animationComponent.animations.put(DevoDevilStates.IDLE, idleAnimation);
        animationComponent.animations.put(DevoDevilStates.STUNNED, stunnedAnimation);
        animationComponent.animations.put(DevoDevilStates.WALK, walkAnimation);
        animationComponent.animations.put(DevoDevilStates.JUMP, jumpAnimation);

        final RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.layer = 3;
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        renderComponent.sprite.mirrorX = false;
        if(isFlipped)
            renderComponent.sprite.mirrorX = true;

        final RenderStarsComponent renderStarsComponent = new RenderStarsComponent();
        renderStarsComponent.animation = starAnimation;

        final DelayControlComponent delayAIComponent = new DelayControlComponent(startDelay);
        DevoDevilStates startState = DevoDevilStates.START_RIGHT;
        if(isFlipped)
            startState = DevoDevilStates.START_LEFT;
        final DefaultStateMachine<Entity, DevoDevilStates> stateMachine = new DefaultStateMachine<>(entity, startState);
        final AIComponent<DevoDevilStates> aiComponent = new AIComponent<>(entity, stateMachine);

        final HealthComponent healthComponent = new HealthComponent();
        healthComponent.stun = 3;

        final DamageComponent damageComponent = new DamageComponent();
        damageComponent.stun = 1;
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
        entity.add(sensorComponent);
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
