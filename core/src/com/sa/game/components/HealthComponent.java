package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component{
    public int lives = 1;
    public int stun = 0;
    public float stunTime = 0;
    public int health = 0;
    public float immortal = 0; //Time to be immortal
    public boolean isStunned() { return stun <= 0; } 
}
