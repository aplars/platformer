package com.sa.game.collision;

public class CollisionFilter {
    public static short PLAYER = 0x0001;
    public static short PLAYER_PROJECTILE = 0x0002;
    public static short ENEMY = 0x0004;
    public static short ENEMY_PROJECTILE = 0x0008;

    public short category = Short.MAX_VALUE;
    public short mask = Short.MAX_VALUE;
}
