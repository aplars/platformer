package com.sa.game.collision;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Iterator;

public class SweepAndPrune {

    public class Pair {
        public CollisionEntity a;
        public CollisionEntity b;

        public Pair(final CollisionEntity a, final CollisionEntity b) {
            this.a = a;
            this.b = b;
        }
    }

    ArrayList<CollisionEntity> axisList = new ArrayList<>();
    public ArrayList<Pair> intersections = new ArrayList<>();

    public SweepAndPrune() {
    }
 
    void add(final CollisionEntity entity) {
        axisList.add(entity);
    }

    void remove(final CollisionEntity entity) {
        axisList.remove(entity);
    }

    public void clear() {
        axisList.clear();
        intersections.clear();
    }

    static int iii = 0;
    void update(final float dt) {
        iii = 0;
        //Sort the list on min
        axisList.sort(new Comparator<CollisionEntity>(){
                @Override
                public int compare(final CollisionEntity o1, final CollisionEntity o2) {
                    float o1Ext = 0;
                    float o2Ext = 0;
                    if(o1.velocity.x < 0.0f)
                        o1Ext = o1.velocity.x * dt;
                    if(o2.velocity.x < 0.0f)
                        o2Ext = o2.velocity.x * dt;
                    return Float.compare(o1.box.x+o1Ext, o2.box.x + o2Ext);
                }
            });

        final ArrayList<CollisionEntity> activeList = new ArrayList<>();
        final Iterator<CollisionEntity> axisListIterator = axisList.iterator();
        //Add first to active list.
        activeList.add(axisListIterator.next());

        intersections.clear();
        //Add the first

        while(axisListIterator.hasNext()) {
            final CollisionEntity axis = axisListIterator.next();
            final ListIterator<CollisionEntity> activeListIterator = activeList.listIterator();
            while(activeListIterator.hasNext()) {
                final CollisionEntity active = activeListIterator.next();

                if(axis.box.x < (active.box.x+active.box.width)) {
                    //We have a pair! report it if both operands are active.
                    if(axis.isEnable && active.isEnable) {
                        intersections.add(new Pair(axis, active));
                    }
                }

                else {
                    //Not intersecting, we can remove item from activeList
                    activeListIterator.remove();
                }
                iii++;
            }
            activeListIterator.add(axis);
        }
    }
}
