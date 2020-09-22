package com.sa.game.gfx;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimations {
    public enum AnimationType {
        Idle,
        Walk,
    }

    HashMap<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public PlayerAnimations(final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation) {
        animations.put(AnimationType.Idle, idleAnimation);
        animations.put(AnimationType.Walk, walkAnimation);
        currentAnimation = idleAnimation;
    }

    public void setCurrentAnimation(final AnimationType animation) {
        currentAnimation = animations.get(animation);
    }

    public TextureRegion getKeyFrame() {
        return currentAnimation.getKeyFrame(currentTime);
    }

    public void update(final float dt) {
        currentTime += dt;
    }
}
