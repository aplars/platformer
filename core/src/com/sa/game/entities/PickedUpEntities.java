package com.sa.game.entities;

import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.Renderer;
import java.util.ArrayList;
import java.util.Iterator;

public class PickedUpEntities {
    public ArrayList<PickedUpEntity> pickedUpEnteties;

    public PickedUpEntities() {
        pickedUpEnteties = new ArrayList<>();
    }

    public void add(PickedUpEntity pickedUpEntity) {
        pickedUpEnteties.add(pickedUpEntity);
    }

    public void clear() {
        pickedUpEnteties.clear();
    }

    public void preUpdate(float dt) {
        for (PickedUpEntity pickedUpEntity : pickedUpEnteties) {
            pickedUpEntity.preUpdate(dt);
        }
    }

    public void update(float dt, float worldBoundY, CollisionDetection collisionDetection) {
        //Remove dead weapons
        Iterator<PickedUpEntity> iterator = pickedUpEnteties.iterator();
        while(iterator.hasNext()) {
            PickedUpEntity pickedUpEntity = iterator.next();
            if(pickedUpEntity.isDead) {
                collisionDetection.remove(pickedUpEntity.collisionEntity);
                iterator.remove();
            }
        }

        //Wrap the position to top if it is below zero
        for (PickedUpEntity pickedUpEntity : pickedUpEnteties) {
            if(pickedUpEntity.position.y < 0f) {
                pickedUpEntity.dstPosition.y = worldBoundY;
                pickedUpEntity.position.y = worldBoundY;
            }
            if(pickedUpEntity.dstPosition.y < 0f) {
                pickedUpEntity.dstPosition.y = worldBoundY;
                pickedUpEntity.position.y = worldBoundY;
            }
        }

        for (PickedUpEntity pickedUpEntity : pickedUpEnteties) {
            pickedUpEntity.update(dt);
        }
    }

    public void render(float dt, Renderer renderer) {
        for (PickedUpEntity pickedUpEntity : pickedUpEnteties) {
            pickedUpEntity.render(dt, renderer);
        }
    }
}
