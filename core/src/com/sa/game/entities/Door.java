package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.AIComponent;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.groups.DoorGroupComponent;
import com.sa.game.gfx.Sprite;
import com.sa.game.statemachines.*;

public class Door {
    public static Entity create(Vector2 position, float size, final Animation<TextureRegion> doorClosedAnimation, final Animation<TextureRegion> doorOpenAnimation, CollisionDetection collisionDetection) {
        Entity entity = new Entity();
        entity.flags = EntityType.Door.type;

        DoorGroupComponent doorGroupComponent = new DoorGroupComponent();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size/2f, size);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.OBJECT;
        collisionDetection.add(collisionEntity);

        ControlComponent controlComponent = new ControlComponent();
        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        AnimationComponent<DoorStates> animationComponent = new AnimationComponent<>();
        animationComponent.animations.put(DoorStates.CLOSED, doorClosedAnimation);
        animationComponent.animations.put(DoorStates.OPEN, doorOpenAnimation);

        DefaultStateMachine<Entity, DoorStates> stateMachine = new DefaultStateMachine<>(entity, DoorStates.CLOSED);
        AIComponent<DoorStates> aiComponent = new AIComponent<>(entity, stateMachine);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.sprite = new Sprite();
        renderComponent.sprite.size.set(size, size);
        renderComponent.sprite.textureRegion = doorClosedAnimation.getKeyFrame(0);

        RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();

        entity.add(doorGroupComponent);
        entity.add(controlComponent);
        entity.add(positionComponent);
        entity.add(collisionComponent);
        entity.add(animationComponent);
        entity.add(aiComponent);
        entity.add(renderComponent);
        entity.add(renderDebugInfoComponent);

        return entity;
    }
}
