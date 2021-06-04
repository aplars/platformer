package com.sa.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Used to make an entity make damage to another entity.
 * Damage updates the other entitys health component.
 *
 */
public class DamageComponent implements Component{
    public int stun = 0;
    public float stunTime = 0;
    public int damage = 0;
}

