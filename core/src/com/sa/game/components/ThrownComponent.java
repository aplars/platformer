package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.sa.game.entities.WalkDirection;

public class ThrownComponent implements Component{
    public WalkDirection direction;
    /**
     * The entity that did throw. 
     */
    public Entity parent;

    public ThrownComponent(WalkDirection direction, Entity parent) {
        this.direction = direction;
        this.parent = parent;
    }
}
