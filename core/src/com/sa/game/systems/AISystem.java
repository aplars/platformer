package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AIComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.StateComponent;

public class AISystem extends IteratingSystem {
    private ComponentMapper<AIComponent> aiMapper = ComponentMapper.getFor(AIComponent.class);
    private ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);

    public AISystem() {
        super(Family.all(AIComponent.class, ControlComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AIComponent ai = aiMapper.get(entity);
        ai.stateMachine.update();
        StateComponent stateComponent = stateMapper.get(entity);
        stateComponent.state = ai.stateMachine.getCurrentState();
    }
}
