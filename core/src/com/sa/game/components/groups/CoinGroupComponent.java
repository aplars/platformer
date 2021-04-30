package com.sa.game.components.groups;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;


public class CoinGroupComponent implements Component {
    public int points = 100;
    public float timeSinceTaken = 0f; //The time since sombody stole this coin. 0 means that it is not stolen yet.

    public Entity thief;
}
