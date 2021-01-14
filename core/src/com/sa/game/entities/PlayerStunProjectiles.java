package com.sa.game.entities;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerStunProjectiles implements Iterable<PlayerStunProjectile> {
    ArrayList<PlayerStunProjectile> projectiles = new ArrayList<>();

    public Iterator<PlayerStunProjectile> iterator() {
        return projectiles.iterator();
    }

    public void add(PlayerStunProjectile projectile) {
        projectiles.add(projectile);
    }
}
