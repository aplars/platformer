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
import com.sa.game.components.HealthComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.WorldConstantsComponent;
import com.sa.game.gfx.Sprite;
import com.sa.game.statemachines.ClownAIState;

public class Enemy {
    public static Entity create(String name, Vector2 position, float size, final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, final Animation<TextureRegion> stunnedAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        Entity updateEntity = new Entity();
        updateEntity.flags = EntityType.Enemy.type;

        WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size, size);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = updateEntity;
        collisionEntity.filter.category = CollisionFilter.ENEMY;
        collisionDetection.add(collisionEntity);

        ControlComponent controlComponent = new ControlComponent();

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        AnimationComponent<ClownAIState> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(ClownAIState.IDLE, idleAnimation);
        animationComponent.animations.put(ClownAIState.STUNNED, stunnedAnimation);
        animationComponent.animations.put(ClownAIState.WALK, walkAnimation);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);
        renderComponent.mirror = true;

        DefaultStateMachine<Entity, ClownAIState> stateMachine = new DefaultStateMachine<>(updateEntity, ClownAIState.START);
        AIComponent<ClownAIState> aiComponent = new AIComponent<>(updateEntity, stateMachine);

        HealthComponent healthComponent = new HealthComponent();

        DamageComponent damageComponent = new DamageComponent();
        damageComponent.stun = false;
        damageComponent.damage = 1;

        final RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();

        updateEntity.add(worldConstantsComponent);
        updateEntity.add(controlComponent);
        updateEntity.add(healthComponent);
        updateEntity.add(damageComponent);
        updateEntity.add(aiComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(renderComponent);
        updateEntity.add(renderDebugInfoComponent);
        return updateEntity;
    }
}
