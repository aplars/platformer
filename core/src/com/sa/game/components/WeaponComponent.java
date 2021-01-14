package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class WeaponComponent implements Component
{
    float speed = 0;
    Entity projectile;
    int num = -1;
}
