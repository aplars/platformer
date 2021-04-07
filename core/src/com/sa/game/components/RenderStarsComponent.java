package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderStarsComponent implements Component {
    public Animation<TextureRegion> animation;
}
