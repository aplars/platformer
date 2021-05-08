package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class DelayAIComponent implements Component{
    public float delay = 0f; //Delay in seconds 

    public DelayAIComponent(float delay) {
        this.delay = delay;
    }
}
