package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;

public class LastSystem extends IteratingSystem {
    float currentTime = 0f;

    public LastSystem() {
        super(Family.all(ControlComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentTime += deltaTime;
    }
}
