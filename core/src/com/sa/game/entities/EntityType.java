package com.sa.game.entities;

public enum EntityType {
    Player (1),
    Enemy(2),
    PlayerProjectile(4);

    public int type = 0;

    EntityType(int type) {
        this.type = type;
    }

}
