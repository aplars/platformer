package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AIComponent;
import com.sa.game.components.ControlComponent;

public class AISystem extends IteratingSystem {
    private ComponentMapper<AIComponent> aiMapper = ComponentMapper.getFor(AIComponent.class);

    public AISystem() {
        super(Family.all(AIComponent.class, ControlComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AIComponent ai = aiMapper.get(entity);
        ai.deltaTime = deltaTime;
        ai.stateMachine.update();
    }
}
