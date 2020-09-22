package com.sa.game.collision;

import java.util.*;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.sa.game.collision.CollisionEntity;

public class SweepAndPrune {

    public class Pair {
        public CollisionEntity a;
        public CollisionEntity b;

        public Pair(CollisionEntity a, CollisionEntity b) {
            this.a = a;
            this.b = b;
        }
    }

    ArrayList<CollisionEntity> axisList = new ArrayList<>();
    public ArrayList<Pair> intersections = new ArrayList<>();

    public SweepAndPrune() {
    }

    void add(CollisionEntity entity) {
        axisList.add(entity);
    }

    void remove(CollisionEntity entity) {
        axisList.remove(entity);
    }

    static int iii = 0;
    void update(final float dt) {

        //Sort the list on min
        axisList.sort(new Comparator<CollisionEntity>(){
                @Override
                public int compare(CollisionEntity o1, CollisionEntity o2) {
                    float o1Ext = 0;
                    float o2Ext = 0;
                    if(o1.velocity.x < 0.0f)
                        o1Ext = o1.velocity.x * dt;
                    if(o2.velocity.x < 0.0f)
                        o2Ext = o2.velocity.x * dt;
                    return Float.compare(o1.box.x+o1Ext, o2.box.x + o2Ext);
                }
            });

        ArrayList<CollisionEntity> activeList = new ArrayList<>();
        Iterator<CollisionEntity> axisListIterator = axisList.iterator();

        intersections.clear();
        //Add the first
        activeList.add(axisListIterator.next());
        while(axisListIterator.hasNext()) {
            CollisionEntity axis = axisListIterator.next();
            ListIterator<CollisionEntity> activeListIterator = activeList.listIterator();
            while(activeListIterator.hasNext()) {
                CollisionEntity active = activeListIterator.next();
                float axisExt = 0f;
                float activeExt = 0f;

                if(axis.velocity.x < 0f)
                    axisExt = axis.velocity.x * dt;
                if(active.velocity.x > 0f)
                    activeExt = active.velocity.x * dt;
                if(axis.box.x+axisExt < (active.box.x+active.box.width) + activeExt) {
                    //We have a pair! report it if both oerands are active.
                    if(axis.isEnable && active.isEnable)
                        intersections.add(new Pair(axis, active));
                    iii++;
                }

                else {
                    //Not intersecting, we can remove item from activeList
                    activeListIterator.remove();
                }
                //Add next entity to active list.
                activeListIterator.add(axis);
            }
        }
    }
}
