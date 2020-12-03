package com.sa.game.gfx;

import java.util.HashMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sa.game.states.PlayerStunProjectileState;

public class PlayerStunProjectileAnimations {
    HashMap<PlayerStunProjectileState, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;
    float currentTime = 0f;

    public PlayerStunProjectileAnimations(final Animation<TextureRegion> onTrackAnimation, final Animation<TextureRegion> explodeAnimation) {
        animations.put(PlayerStunProjectileState.OnTrack, onTrackAnimation);
        animations.put(PlayerStunProjectileState.Explode, explodeAnimation);
        currentAnimation = onTrackAnimation;
    }

    public void setCurrentAnimation(final PlayerStunProjectileState animation) {
        currentAnimation = animations.get(animation);
    }

    public TextureRegion getKeyFrame() {
        return currentAnimation.getKeyFrame(currentTime);
    }

    public void update(final float dt) {
        currentTime += dt;
    }
}
