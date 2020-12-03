package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.StateComponent;

public class AnimationSystem<T> extends IteratingSystem {
    private ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);
    private ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

    public AnimationSystem() {
        super(Family.all(
                         StateComponent.class,
                         AnimationComponent.class,
                         RenderComponent.class,
                         PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateComponent<T> state = stateMapper.get(entity);
        AnimationComponent<T> animation = animationMapper.get(entity);
        RenderComponent render = renderMapper.get(entity);
        PhysicsComponent physics = physicsMapper.get(entity);

        render.sprite.textureRegion.setRegion(animation.animations.get(state.state).getKeyFrame(animation.currentTime));
        if(physics.velocity.x < 0.0f) {
            render.sprite.mirrorX = true;
        }
        else if(physics.velocity.x > 0) {
            render.sprite.mirrorX = false;
        }
        animation.currentTime += deltaTime;
    }
}
