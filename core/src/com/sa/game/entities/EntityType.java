package com.sa.game.entities;

public enum EntityType {
    None(0),
    Player (1),
    Enemy(2),
    BoxingGlove(4),
    Key(8),
    Door(16),
    Explosion(32),
    Apple(64);

    public int type = 0;

    EntityType(int type) {
        this.type = type;
    }

}
