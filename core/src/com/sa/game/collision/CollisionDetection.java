package com.sa.game.collision;

import com.badlogic.gdx.math.Vector2;
import com.sa.game.entities.PlayerProjectile;

import java.util.ArrayList;

public class CollisionDetection {
    ArrayList<CollisionEntity> entities = new ArrayList<>();

    public void add(final CollisionEntity entity) {
        entities.add(entity);
    }

    public void remove(final CollisionEntity entity) {
        entities.remove(entity);
    }

    public void update(float dt) {
        //Clear all collisions. New ones get populated below.
        for(final CollisionEntity a : entities) {
            a.collidees.clear();
        }

        int startI = 0;
        Vector2 aVel = new Vector2();
        Vector2 bVel = new Vector2();

        for(final CollisionEntity a : entities) {
            aVel.set(a.velocity);
            aVel.x*=dt;
            aVel.y*=dt;

            for (int i = startI; i < entities.size(); ++i) {
                final CollisionEntity b = entities.get(i);
                if (a == b) {
                    startI++;
                    continue;
                }
                bVel.set(b.velocity);
                bVel.x*=dt;
                bVel.y*=dt;

                final RectangleCollisionData data = IntersectionTests.rectangleRectangle(a.box, aVel, b.box, bVel);
                if(data.didCollide) {
                    a.collidees.add(b);
                    b.collidees.add(a);
                }
            }
            startI++;
        }
    }


}
