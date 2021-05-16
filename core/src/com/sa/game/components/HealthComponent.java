package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component{
    public int lives = 1;
    public boolean isStunned = false;
    public float stunTime = 0;
    public int health = 0;
}
