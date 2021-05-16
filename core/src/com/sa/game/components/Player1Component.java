package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class Player1Component implements Component
{
    public int score = 0;
    public Vector2 initialPosition = new Vector2();
}
