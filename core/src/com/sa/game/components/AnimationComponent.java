package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationComponent<S extends State<Entity>> implements Component {
    public HashMap<S, Animation<TextureRegion>> animations = new HashMap<>();
    public float currentTime = 0f;
}
