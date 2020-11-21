package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class EntityControlComponent implements Component {
    public static float SPAWN_TIME_INTERVAL = 1f;

    public float jumpImpulse;
    public float moveForce;
    public float timeUntilNextBulletSpawnPossible = 0f;
}
