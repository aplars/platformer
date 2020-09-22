package com.sa.game.collision;

public class CollisionFilter {
    public static short CATEGORY_PLAYER = 0x0001;
    public static short CATEGORY_PLAYER_PROJECTILE = 0x0002;
    public static short CATEGORY_ENEMY = 0x0004;
    public static short CATEGORY_ENEMY_PROJECTILE = 0x0008;

    public short category = 0;
    public short mask = 0;
}
