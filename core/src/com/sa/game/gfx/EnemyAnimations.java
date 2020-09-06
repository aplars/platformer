package com.sa.game.gfx;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class EnemyAnimations {
    public enum AnimationType {
        Idle,
        Walk,
        Stunned
    }

    public static class Data {
        public float duration;
        public PlayMode playMode;

        public Data(float duration, PlayMode playMode) {
            this.duration = duration;
            this.playMode = playMode;
        }
    }

    HashMap<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public EnemyAnimations(TextureAtlas atlas, Animation<TextureRegion> idleAnimation, Animation<TextureRegion> walkAnimation, Animation<TextureRegion> stunnedAnimation) {
        animations.put(AnimationType.Idle, idleAnimation);
        animations.put(AnimationType.Walk, walkAnimation);
        animations.put(AnimationType.Stunned, stunnedAnimation);
        currentAnimation = idleAnimation;
    }

    public void setCurrentAnimation(final AnimationType animation) {
        currentAnimation = animations.get(animation);
    }

    public TextureRegion getKeyFrame() {
        return currentAnimation.getKeyFrame(currentTime);
    }

    public void update(float dt) {
        currentTime += dt;
    }
}
