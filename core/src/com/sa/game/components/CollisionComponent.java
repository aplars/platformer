package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionEntity;

public class CollisionComponent implements Component{
    public CollisionEntity entity = new CollisionEntity();
    public Vector2 offset = new Vector2();
    
    public void setIsEnable(boolean isEnable) {
        entity.isEnable = isEnable;
    }
}
