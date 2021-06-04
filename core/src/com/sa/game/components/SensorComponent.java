package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.sa.game.StaticEnvironment;

public class SensorComponent implements Component {
    public StaticEnvironment staticEnvironment = null;

    public boolean isOnground = false;
    public boolean wallCollisionLeft = false;
    public boolean wallCollisionRight = false;

    public boolean groundOnNextTile = true;
    public boolean groundOnLeft = true;
    public boolean groundOnRight = true;

    public boolean isOnTop = false;
    public boolean isOnBottom = false;
}
