package com.sa.game.gfx;

import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerWeaponAnimations {
    public enum AnimationType {
        Stunned
    }

    HashMap<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public PlayerWeaponAnimations(final Animation<TextureRegion> stunnedAnimation) {
        animations.put(AnimationType.Stunned, stunnedAnimation);
        currentAnimation = stunnedAnimation;
    }

    public void setCurrentAnimation(final AnimationType animation) {
        currentAnimation = animations.get(animation);
    }

    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }

    public TextureRegion getKeyFrame() {
        return currentAnimation.getKeyFrame(currentTime);
    }

    public void update(final float dt) {
        currentTime += dt;
    }
}
