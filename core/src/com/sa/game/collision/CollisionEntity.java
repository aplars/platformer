package com.sa.game.collision;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class CollisionEntity {
    public final Rectangle box = new Rectangle();
    public Vector2 velocity = new Vector2();
    public Object userData;
    public ArrayList<CollisionEntity> collidees = new ArrayList<>();
    public boolean isEnable = true;
    public CollisionFilter filter = new CollisionFilter();
}
