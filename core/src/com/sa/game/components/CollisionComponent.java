package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.sa.game.collision.CollisionEntity;

public class CollisionComponent implements Component{
    public CollisionEntity entity = new CollisionEntity();

    public void setIsEnable(boolean isEnable) {
        entity.isEnable = isEnable;
    }
}
