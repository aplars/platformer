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
import com.sa.game.statemachines.ChiChiStates;

public class ChiChi {
    static Entity create(final float startDelay, final Vector2 position, final float size, boolean isFlipped,
                         final Animation<TextureRegion> flapingWingsAnimation,
                         final Animation<TextureRegion> starAnimation,
                         final StaticEnvironment staticEnvironment,
                         final CollisionDetection collisionDetection) {

        Entity entity = new Entity();
        entity.flags = EntityType.Enemy.type;

        final EnemyGroupComponent enemyGroupComponent = new EnemyGroupComponent();

        final WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();
        worldConstantsComponent.width = staticEnvironment.getWorldBoundX();

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
        physicsComponent.gravity = 0;//-(staticEnvironment.tileSizeInPixels*5f+2)/(2f*jumpTime*jumpTime);
        physicsComponent.jumpTime = jumpTime;
        physicsComponent.airResistance.set(0.85f, 0.85f);
        physicsComponent.walkDirection = WalkDirection.Right;
        if(isFlipped)
            physicsComponent.walkDirection = WalkDirection.Left;

        final CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        final SensorComponent sensorComponent = new SensorComponent();
        sensorComponent.staticEnvironment = staticEnvironment;

        final RenderStarsComponent renderStarsComponent = new RenderStarsComponent();
        renderStarsComponent.animation = starAnimation;

        final AnimationComponent<ChiChiStates> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(ChiChiStates.START_LEFT, flapingWingsAnimation);
        animationComponent.animations.put(ChiChiStates.START_RIGHT, flapingWingsAnimation);
        animationComponent.animations.put(ChiChiStates.FLYING, flapingWingsAnimation);

        final RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.layer = 3;
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        renderComponent.sprite.mirrorX = false;
        if(isFlipped)
            renderComponent.sprite.mirrorX = true;

        final DelayControlComponent delayAIComponent = new DelayControlComponent(startDelay);
        ChiChiStates startState = ChiChiStates.START_RIGHT;
        if(isFlipped)
            startState = ChiChiStates.START_LEFT;
        final DefaultStateMachine<Entity, ChiChiStates> stateMachine = new DefaultStateMachine<>(entity, startState);
        final AIComponent<ChiChiStates> aiComponent = new AIComponent<>(entity, stateMachine);

        final HealthComponent healthComponent = new HealthComponent();
        healthComponent.stun = 3;
        healthComponent.stun = 2;

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
        entity.add(renderStarsComponent);
        entity.add(animationComponent);
        entity.add(renderComponent);
        entity.add(renderDebugInfoComponent);

        return entity;
    }
}
