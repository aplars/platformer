package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.PlayerStunProjectileAnimationComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.StateComponent;

public class AnimationSystem<T> extends IteratingSystem {
    @SuppressWarnings("unchecked")
    private static <T> Class<T> generify(Class<?> cls) {
        return (Class<T>)cls;
    }
    private ComponentMapper<StateComponent<T>> stateMapper2 = ComponentMapper.getFor((Class<StateComponent<T>>)(Class<?>)StateComponent.class);
    private ComponentMapper<AnimationComponent<T>> animationMapper = ComponentMapper.getFor((Class<AnimationComponent<T>>)(Class<?>)AnimationComponent.class);
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);

    public AnimationSystem() {
        super(Family.all(
                (Class<StateComponent<T>>)(Class<?>)StateComponent.class,
                (Class<AnimationComponent<T>>)(Class<?>)AnimationComponent.class,
                RenderComponent.class).get());
    }
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        StateComponent<T> state = stateMapper2.get(entity);
        AnimationComponent<T> animation = animationMapper.get(entity);
        RenderComponent render = renderMapper.get(entity);
        render.sprite.textureRegion.setRegion(animation.animations.get(state.state).getKeyFrame(animation.currentTime));
        animation.currentTime += deltaTime;
  }
}
