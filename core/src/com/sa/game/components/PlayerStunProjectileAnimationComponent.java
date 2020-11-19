package com.sa.game.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sa.game.States.PlayerStunProjectileState;
import com.sa.game.gfx.PlayerStunProjectileAnimations;

public class PlayerStunProjectileAnimationComponent implements Component {
    public HashMap<PlayerStunProjectileState, Animation<TextureRegion>> animations = new HashMap<>();
    public float currentTime = 0f;

}
