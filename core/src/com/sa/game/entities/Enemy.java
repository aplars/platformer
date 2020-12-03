package com.sa.game.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;
import com.sa.game.components.*;
import com.sa.game.gfx.Sprite;
import com.sa.game.gfx.Sprites;
import com.sa.game.statemachines.ClownAIState;
import com.sa.game.states.EnemyState;

public class Enemy {
    public  String name;

    public Enemy(String name, Vector2 position, float size, final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, final Animation<TextureRegion> stunnedAnimation, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, Engine updateEngine) {
        this.name = name;

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size, size);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = this;
        collisionDetection.add(collisionEntity);

        //Player1Component player1Component = new Player1Component();
        ControlComponent controlComponent = new ControlComponent();

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        float jumpTime = 0.5f;
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.gravity = -2*(staticEnvironment.tileSizeInPixels*5f+2)/(float)Math.pow(jumpTime, 2f);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        StateComponent<ClownAIState> stateComponent = new StateComponent<>();
        stateComponent.state = ClownAIState.IDLE;

        AnimationComponent<ClownAIState> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(ClownAIState.IDLE, idleAnimation);
        //animationComponent.animations.put(ClownAIState.STUNNED, stunnedAnimation);
        animationComponent.animations.put(ClownAIState.WALK, walkAnimation);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(collisionEntity.box.width, collisionEntity.box.height);

        Entity updateEntity = new Entity();

        DefaultStateMachine<Entity, ClownAIState> stateMachine = new DefaultStateMachine<>(updateEntity, ClownAIState.START);
        AIComponent<ClownAIState> aiComponent = new AIComponent<>(updateEntity, stateMachine);


        //updateEntity.add(player1Component);
        updateEntity.add(controlComponent);

        updateEntity.add(aiComponent);
        updateEntity.add(positionComponent);
        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);

        updateEntity.add(positionComponent);
        updateEntity.add(physicsComponent);
        updateEntity.add(collisionComponent);
        updateEntity.add(stateComponent);
        updateEntity.add(animationComponent);
        updateEntity.add(renderComponent);

        updateEngine.addEntity(updateEntity);
    }

    public void preUpdate(float dt) {

    }
    public void update(float dt, StaticEnvironment staticEnvironment) {
    }


}
