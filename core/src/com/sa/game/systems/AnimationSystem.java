package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sa.game.components.AIComponent;
import com.sa.game.components.AnimationComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.RenderComponent;

public class AnimationSystem<T> extends IteratingSystem {
    public AnimationSystem() {
        super(Family.all(
                         AIComponent.class,
                         AnimationComponent.class,
                         RenderComponent.class//,
                         /*PhysicsComponent.class*/).get());
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final AIComponent aiComponent = ComponentMappers.ai.get(entity);
        final AnimationComponent animationComponent = ComponentMappers.animation.get(entity);
        final RenderComponent renderComponent = ComponentMappers.render.get(entity);
        final PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);

        final Animation<TextureRegion> animationFrames = (Animation<TextureRegion>)animationComponent.animations.get(aiComponent.stateMachine.getCurrentState());
        if(animationFrames != null)
            renderComponent.sprite.textureRegion.setRegion(animationFrames.getKeyFrame(animationComponent.currentTime));

        if (physicsComponent != null) {
            if (physicsComponent.velocity.x < 0.0f) {
                renderComponent.sprite.mirrorX = true;
            } else if (physicsComponent.velocity.x > 0) {
                renderComponent.sprite.mirrorX = false;
            }
        }
        animationComponent.currentTime += deltaTime;
    }
}
