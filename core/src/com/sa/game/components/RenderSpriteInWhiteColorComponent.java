package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class RenderSpriteInWhiteColorComponent implements Component {
    public float time = 0f;

    public RenderSpriteInWhiteColorComponent(float time) {
        this.time = time;
    }
}
