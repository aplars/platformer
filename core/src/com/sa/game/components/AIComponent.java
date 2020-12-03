package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.sa.game.statemachines.ClownAIState;

public class AIComponent<S extends State<Entity>> implements Component {
    public DefaultStateMachine<Entity, S> stateMachine;

    public AIComponent(Entity entity, DefaultStateMachine<Entity, S> stateMachine) {
        this.stateMachine = stateMachine;
    }
}
