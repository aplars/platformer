package com.sa.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sa.game.collision.CollisionDetection;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerStunProjectiles implements Iterable<PlayerStunProjectile> {
    ArrayList<PlayerStunProjectile> projectiles = new ArrayList<>();

    public void dispose() {
        for(PlayerStunProjectile proj : projectiles) {
            proj.dispose();
        }
    }

    public Iterator<PlayerStunProjectile> iterator() {
        return projectiles.iterator();
    }

    public void add(PlayerStunProjectile projectile) {
        projectiles.add(projectile);
    }


    public void update(float dt, CollisionDetection collisionDetection, float worldBound) {
        for (PlayerStunProjectile projectile : projectiles) {
            projectile.update(dt);
        }

        Iterator<PlayerStunProjectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            PlayerStunProjectile projectile = iterator.next();
            if(projectile.position.y > worldBound) {
                collisionDetection.remove(projectile.collisionEntity);
                iterator.remove();
            }
            if(projectile.collisionEntity.collidees.size() > 0) {
                collisionDetection.remove(projectile.collisionEntity);
                iterator.remove();
            }
        }
    }

    public void render(float dt, OrthographicCamera camera) {
        for (PlayerStunProjectile projectile : projectiles) {
            projectile.render(dt, camera);
        }
    }
}
