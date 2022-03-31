package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class RenderSpriteInFlickeringColorsComponent implements Component {
    public float time = 0f;

    public RenderSpriteInFlickeringColorsComponent(float time) {
        this.time = time;
    }
}
