package com.sa.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sa.game.collision.CollisionDetection;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerProjectiles implements Iterable<PlayerProjectile> {
    ArrayList<PlayerProjectile> projectiles = new ArrayList<>();

    public Iterator<PlayerProjectile> iterator() {
        return projectiles.iterator();
    }

    public void add(PlayerProjectile projectile) {
        projectiles.add(projectile);
    }


    public void update(float dt, CollisionDetection collisionDetection, float worldBound) {
        for (PlayerProjectile projectile : projectiles) {
            projectile.update(dt);
        }

        Iterator<PlayerProjectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            PlayerProjectile projectile = iterator.next();
            if(projectile.position.y > worldBound) {
                collisionDetection.remove(projectile.collisionEntity);
                iterator.remove();
            }
        }
    }

    public void render(float dt, OrthographicCamera camera) {
        for (PlayerProjectile projectile : projectiles) {
            projectile.render(dt, camera);
        }
    }
}
