package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class ExplodeOnContactComponent implements Component {
    public Entity theGuiltyEntity; //The entity that is guilty of this ones death.

    public ExplodeOnContactComponent(Entity theGuiltyEntity) {
        this.theGuiltyEntity = theGuiltyEntity;
    }
}
