package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationComponent<T> implements Component {
    public HashMap<T, Animation<TextureRegion>> animations = new HashMap<>();
    public float currentTime = 0f;
}
