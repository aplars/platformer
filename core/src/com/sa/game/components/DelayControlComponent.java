package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class DelayControlComponent implements Component{
    public float delay = 0f; //Delay in seconds 

    public DelayControlComponent(float delay) {
        this.delay = delay;
    }
}
