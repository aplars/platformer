package com.sa.game.entities;

import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.Sprites;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerWeapons {
    public ArrayList<PlayerWeapon> weapons;

    public PlayerWeapons() {
        weapons = new ArrayList<>();
    }

    public void add(PlayerWeapon weapon) {
        weapons.add(weapon);
    }

    public void preUpdate(float dt) {
        for (PlayerWeapon weapon : weapons) {
            weapon.preUpdate(dt);
        }
    }

    public void update(float dt, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        //Remove dead weapons
        Iterator<PlayerWeapon> weIterator = weapons.iterator();
        while(weIterator.hasNext()) {
            PlayerWeapon weapon = weIterator.next();
            if(weapon.isDead) {
                collisionDetection.remove(weapon.collisionEntity);
                weIterator.remove();
            }
        }

        //Wrap the position to top if it is below zero
        for (PlayerWeapon weapon : weapons) {
            if(weapon.position.y < 0f) {
                weapon.dstPosition.y = staticEnvironment.getWorldBoundY();
                weapon.position.y = staticEnvironment.getWorldBoundY();
            }
            if(weapon.dstPosition.y < 0f) {
                weapon.dstPosition.y = staticEnvironment.getWorldBoundY();
                weapon.position.y = staticEnvironment.getWorldBoundY();
            }
        }

        for (PlayerWeapon weapon : weapons) {
            weapon.update(dt, staticEnvironment);
        }
    }

    public void render(float dt, Sprites sprites) {
        for (PlayerWeapon weapon : weapons) {
            weapon.render(dt, sprites);
        }
    }
}
