package com.sa.game.collision;

import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.entities.PlayerStunProjectile;

import java.util.ArrayList;

public class CollisionDetection {
    final ArrayList<CollisionEntity> entities = new ArrayList<>();
    SweepAndPrune sap = new SweepAndPrune();

    public void add(final CollisionEntity entity) {
        entities.add(entity);
        sap.add(entity);
    }

    public void remove(final CollisionEntity entity) {
        entities.remove(entity);
        sap.remove(entity);
    }

    public  void clear() {
        entities.clear();
        sap.clear();
    }

    public void update(float dt, StaticEnvironment staticEnvironment) {
        sap.update(dt);

        //Clear all collisions. New ones get populated below.
        for(final CollisionEntity a : entities) {
            a.collidees.clear();
        }

        Vector2 aVel = new Vector2();
        Vector2 bVel = new Vector2();

        for(SweepAndPrune.Pair colPair : sap.intersections) {
            CollisionEntity a = colPair.a;
            aVel.set(a.velocity);
            aVel.x*=dt;
            aVel.y*=dt;
            CollisionEntity b = colPair.b;
            bVel.set(b.velocity);
            bVel.x*=dt;
            bVel.y*=dt;

            final RectangleCollisionData data = IntersectionTests.rectangleRectangle(a.box, aVel, b.box, bVel);
            if(data.didCollide) {
                a.collidees.add(b);
                b.collidees.add(a);
            }
        }
        //Check collision against static scene parts
        for(final CollisionEntity collisionEntity : entities) {
            IntersectionTests.rectangleGround(dt, collisionEntity.box, collisionEntity.velocity, staticEnvironment, collisionEntity.groundCollisionData);
            IntersectionTests.rectangleWalls(dt, collisionEntity.box, collisionEntity.velocity, staticEnvironment, collisionEntity.wallsCollisionData);
        }
    }


}
