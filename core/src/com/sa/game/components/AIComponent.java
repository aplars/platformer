package com.sa.game.components;

import java.util.Dictionary;
import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.sa.game.statemachines.ClownAIState;

public class AIComponent<S extends State<Entity>> implements Component {
    public DefaultStateMachine<Entity, S> stateMachine;
    public float deltaTime = 0f; //The time between frames.
    public HashMap<String, Float> countDownTimers = new HashMap<String, Float>(); //Stores timers used for ai.

    public AIComponent(Entity entity, DefaultStateMachine<Entity, S> stateMachine) {
        this.stateMachine = stateMachine;
    }
}
