package com.sa.game.gfx;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class EnemyAnimations {
    public enum AnimationType {
        Idle,
        Walk,
        Stunned
    }

    HashMap<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public EnemyAnimations(final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, final Animation<TextureRegion> stunnedAnimation) {
        animations.put(AnimationType.Idle, idleAnimation);
        animations.put(AnimationType.Walk, walkAnimation);
        animations.put(AnimationType.Stunned, stunnedAnimation);
        currentAnimation = idleAnimation;
    }

    public void setCurrentAnimation(final AnimationType animation) {
        currentAnimation = animations.get(animation);
    }

    public Animation<TextureRegion> getCurrentAnimation() { return currentAnimation; }
    public TextureRegion getKeyFrame() {
        return currentAnimation.getKeyFrame(currentTime);
    }

    public void update(final float dt) {
        currentTime += dt;
    }
}
