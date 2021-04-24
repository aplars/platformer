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
import com.sa.game.components.HealthComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.PunchComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.WorldConstantsComponent;
import com.sa.game.gfx.Sprite;
import com.sa.game.statemachines.PlayerAIState;

public class Player {
    public static Entity create(Vector2 pos, Vector2 vel, Vector2 size,
                                final Animation<TextureRegion> idleAnimation,
                                final Animation<TextureRegion> walkAnimation,
                                final Animation<TextureRegion> jumpAnimation,
                                final Animation<TextureRegion> deadAnimation,
                                StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        Entity entity = new Entity();

        WorldConstantsComponent worldConstantsComponent = new WorldConstantsComponent();
        worldConstantsComponent.height = staticEnvironment.getWorldBoundY();

        Rectangle colbBox = new Rectangle(0, 0, size.x/2, size.y/2);
        colbBox.setCenter(pos.x, pos.y);

        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(colbBox);
        collisionEntity.velocity = vel;
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.PLAYER;
        collisionEntity.filter.mask = (short)(CollisionFilter.ENEMY|CollisionFilter.OBJECT);
        collisionDetection.add(collisionEntity);

        Player1Component player1Component = new Player1Component();

        ControlComponent controlComponent = new ControlComponent();

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        physicsComponent.velocity.set(vel);
        physicsComponent.jumpTime = jumpTime;

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(pos.x, pos.y);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;
        //collisionComponent.offset.y = -size.y / 4;

        HealthComponent healthComponent = new HealthComponent();
        healthComponent.isStunned = false;
        healthComponent.health = 1;

        PickUpEntityComponent pickUpEntityComponent = new PickUpEntityComponent();

        PunchComponent punchComponent = new PunchComponent();

        AnimationComponent<PlayerAIState> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(PlayerAIState.IDLE, idleAnimation);
        animationComponent.animations.put(PlayerAIState.WALK, walkAnimation);
        animationComponent.animations.put(PlayerAIState.JUMP, jumpAnimation);
        animationComponent.animations.put(PlayerAIState.DEAD, deadAnimation);

        DefaultStateMachine<Entity, PlayerAIState> stateMachine = new DefaultStateMachine<>(entity, PlayerAIState.IDLE);
        AIComponent<PlayerAIState> aiComponent = new AIComponent<>(entity, stateMachine);


        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(size.x, size.y);
        renderComponent.sprite.offset.y = size.y / 4;

        RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();
        // WeaponComponent weaponComponent = new WeaponComponent();
        //Vector2 projectileVelocity = new Vector2(300f * (float) physics.GetWalkDirectionScalar(), 0f);
        //Entity projectile = CreateEnteties.playerStunProjectile(assetManager, position.position, projectileVelocity, tileSizeInPixels, collisionDetection);
        //weaponComponent.entity =;

        entity.add(worldConstantsComponent);
        entity.add(physicsComponent);
        entity.add(collisionComponent);
        entity.add(positionComponent);
        entity.add(player1Component);
        entity.add(controlComponent);
        entity.add(physicsComponent);
        entity.add(positionComponent);
        entity.add(collisionComponent);
        entity.add(healthComponent);
        entity.add(pickUpEntityComponent);
        entity.add(punchComponent);
        entity.add(animationComponent);
        entity.add(aiComponent);
        entity.add(renderComponent);
        entity.add(renderDebugInfoComponent);

        return entity;
    }
}
