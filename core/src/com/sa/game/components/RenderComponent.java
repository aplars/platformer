package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.sa.game.gfx.Sprite;

public class RenderComponent implements Component{
    public Sprite sprite;
    public boolean mirror = false;
}

