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
import com.sa.game.components.DelayControlComponent;
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
import com.sa.game.statemachines.PlayerStates;

public class Player {
    public static Entity create(float startDelay,
                                int initialScore,
                                int lives,
                                Vector2 pos, Vector2 vel, Vector2 size, boolean isFlipped,
                                final Animation<TextureRegion> idleAnimation,
                                final Animation<TextureRegion> walkAnimation,
                                final Animation<TextureRegion> jumpAnimation,
                                final Animation<TextureRegion> deadAnimation,
                                StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        Entity entity = new Entity();
        entity.flags = EntityType.Player.type;

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
        player1Component.score = initialScore;
        player1Component.initialPosition.set(pos);

        DelayControlComponent delayControlComponent = new DelayControlComponent(startDelay);
        ControlComponent controlComponent = new ControlComponent();

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);
        physicsComponent.velocity.set(vel);
        physicsComponent.jumpTime = jumpTime;
        //physicsComponent.airResistance.set(0.85f, 0.95f);

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(pos.x, pos.y);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;
        //collisionComponent.offset.y = -size.y / 4;

        HealthComponent healthComponent = new HealthComponent();
        healthComponent.isStunned = false;
        healthComponent.health = 1;
        healthComponent.lives = lives;

        PickUpEntityComponent pickUpEntityComponent = new PickUpEntityComponent();

        PunchComponent punchComponent = new PunchComponent();

        AnimationComponent<PlayerStates> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(PlayerStates.IDLE, idleAnimation);
        animationComponent.animations.put(PlayerStates.WALK, walkAnimation);
        animationComponent.animations.put(PlayerStates.JUMP, jumpAnimation);
        animationComponent.animations.put(PlayerStates.DEAD, deadAnimation);
        animationComponent.animations.put(PlayerStates.RESPAWN, deadAnimation);
        animationComponent.animations.put(PlayerStates.REMOVEFROMGAME, deadAnimation);

        DefaultStateMachine<Entity, PlayerStates> stateMachine = new DefaultStateMachine<>(entity, PlayerStates.IDLE);
        AIComponent<PlayerStates> aiComponent = new AIComponent<>(entity, stateMachine);


        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.layer = 3;
        renderComponent.sprite.size.set(size.x, size.y);
        renderComponent.sprite.offset.y = size.y / 4;
        if(isFlipped)
            renderComponent.sprite.mirrorX = true;

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
        entity.add(delayControlComponent);
        entity.add(controlComponent);
        entity.add(physicsComponent);
        //entity.add(positionComponent);
        //entity.add(collisionComponent);
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
