package com.sa.game.gfx;
import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sa.game.States.EnemyState;

public class EnemyAnimations {

    HashMap<EnemyState, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public EnemyAnimations(final Animation<TextureRegion> idleAnimation, final Animation<TextureRegion> walkAnimation, final Animation<TextureRegion> stunnedAnimation) {
        animations.put(EnemyState.Idle, idleAnimation);
        animations.put(EnemyState.Walk, walkAnimation);
        animations.put(EnemyState.Stunned, stunnedAnimation);
        currentAnimation = idleAnimation;
    }

    public void setCurrentAnimation(final EnemyState animation) {
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
