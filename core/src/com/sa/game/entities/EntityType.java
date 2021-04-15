package com.sa.game.entities;

public enum EntityType {
    None(0),
    Player (1),
    Enemy(2),
    PlayerProjectile(4),
    Key(8);

    public int type = 0;

    EntityType(int type) {
        this.type = type;
    }

}
