package com.sa.game.entities;

import java.util.ArrayList;
import java.util.Iterator;

import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.Sprites;

public class Enemies {
    ArrayList<Enemy> enemies = new ArrayList<>();

    public void add(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void remove(Enemy enemy) {
        enemies.remove(enemy);
    }

    public void clear() {

        enemies.clear();
    }

    public Iterator<Enemy> iterator() {
        return enemies.iterator();
    }

    public void preUpdate(float dt) {
        for(Enemy enemy : enemies) {
            enemy.preUpdate(dt);
        }
    }

    public void update(float dt, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        //Move the player to the top if it falls under zero
        for (Enemy enemy : enemies) {
            if(enemy.position.y < 0) {
                enemy.position.y = staticEnvironment.getWorldBoundY();
            }

        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while(enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if(enemy.isShoot) {
                enemyIterator.remove();
                collisionDetection.remove(enemy.collisionEntity);
            }
        }

        for(Enemy enemy : enemies) {
            enemy.update(dt, staticEnvironment);
        }
    }

    public void render(float dt, Sprites sprites) {
        for(Enemy enemy : enemies) {
            enemy.render(dt, sprites);
        }
    }
}
