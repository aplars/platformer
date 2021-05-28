package com.sa.game.collision;

import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;

import java.util.ArrayList;

import javax.print.attribute.standard.ColorSupported;

public class CollisionDetection {
    final ArrayList<CollisionEntity> entities = new ArrayList<>();
    SweepAndPrune sap = new SweepAndPrune();

    public static void disableCollisionBetweenEntities(final CollisionEntity entityA, final CollisionEntity entityB) {
        entityA.filter.disabled.add(entityB);
        entityB.filter.disabled.add(entityA);
    }

    public static void enableCollisionBetweenEntities(final CollisionEntity entityA, final CollisionEntity entityB) {
        entityA.filter.disabled.remove(entityB);
        entityB.filter.disabled.remove(entityA);
    }

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

    public void update(final float dt, final StaticEnvironment staticEnvironment) {
        sap.update(dt);

        //Clear all collisions. New ones get populated below.
        for(final CollisionEntity a : entities) {
            a.collidees.clear();
        }

        final Vector2 aVel = new Vector2();
        final Vector2 bVel = new Vector2();

        for(final SweepAndPrune.Pair colPair : sap.intersections) {
            final CollisionEntity a = colPair.a;
            aVel.set(a.velocity);
            aVel.x*=dt;
            aVel.y*=dt;
            final CollisionEntity b = colPair.b;
            bVel.set(b.velocity);
            bVel.x*=dt;
            bVel.y*=dt;

            final int aa = colPair.b.filter.category & colPair.a.filter.mask;
            final int bb = colPair.a.filter.category & colPair.b.filter.mask;
            //if(aa == 0  || bb == 0)
            //    continue;

            final RectangleCollisionData data = IntersectionTests.rectangleRectangle(a.box, aVel, b.box, bVel);
            if(data.didCollide) {
                if(aa != 0 && !a.filter.disabled.contains(b))
                    a.collidees.add(b);
                if(bb != 0 && !b.filter.disabled.contains(a))
                    b.collidees.add(a);
            }
        }
        //Check collision against static scene parts
        for(final CollisionEntity collisionEntity : entities) {
            if (collisionEntity.isEnable) {
                IntersectionTests.rectangleGround(dt, collisionEntity.box, collisionEntity.velocity, staticEnvironment, collisionEntity.groundCollisionData);
                IntersectionTests.rectangleWalls(dt, collisionEntity.box, collisionEntity.velocity, staticEnvironment, collisionEntity.wallsCollisionData);

                //If we collides then take a small step up and try again. This makes it possible to traverse small obstacles. 
                if(collisionEntity.wallsCollisionData.didCollide) {
                    Vector2 c = new Vector2();
                    c = collisionEntity.box.getCenter(c);
                    collisionEntity.box.setCenter(c.x, c.y+0.1f);
                    IntersectionTests.rectangleWalls(dt, collisionEntity.box, collisionEntity.velocity, staticEnvironment, collisionEntity.wallsCollisionData);
                }
            }
            else {
                collisionEntity.wallsCollisionData.didCollide = false;
                collisionEntity.groundCollisionData.didCollide = false;
            }
        }
    }


}
