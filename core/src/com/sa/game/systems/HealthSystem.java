package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.DamageComponent;
import com.sa.game.components.HealthComponent;

public class HealthSystem extends IteratingSystem {
    public HealthSystem() {
        super(Family.all(HealthComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = ComponentMappers.health.get(entity);
        if(healthComponent.immortal > 0)
            healthComponent.immortal-=deltaTime;
    }
}
