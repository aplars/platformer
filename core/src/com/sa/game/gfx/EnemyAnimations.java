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

    HashMap<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public EnemyAnimations(final String atlasFile) {
        final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasFile));
        Animation<TextureRegion> idleAnimation = new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("idle"), PlayMode.LOOP);
        Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), PlayMode.LOOP);
        Animation<TextureRegion> stunnedAnimation = new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("stunned"),
                PlayMode.NORMAL);

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
