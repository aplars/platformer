package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AIComponent;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.RenderComponent;

public class AnimationSystem<T> extends IteratingSystem {
    private ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem() {
        super(Family.all(
                         AIComponent.class,
                         AnimationComponent.class,
                         RenderComponent.class,
                         PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AIComponent ai = ComponentMappers.ai.get(entity);
        AnimationComponent<T> animation = animationMapper.get(entity);
        RenderComponent render = ComponentMappers.render.get(entity);
        PhysicsComponent physics = ComponentMappers.physics.get(entity);

        render.sprite.textureRegion.setRegion(animation.animations.get(ai.stateMachine.getCurrentState()).getKeyFrame(animation.currentTime));
        if(physics.velocity.x < 0.0f) {
            render.sprite.mirrorX = true;
        }
        else if(physics.velocity.x > 0) {
            render.sprite.mirrorX = false;
        }
        animation.currentTime += deltaTime;
    }
}
