package com.sa.game.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;

public class AIComponent<S extends State<Entity>> implements Component {
    public DefaultStateMachine<Entity, S> stateMachine;
    public float deltaTime = 0f; //The time between frames.
    public HashMap<String, Float> countDownTimers = new HashMap<>(); //Stores timers used for ai.

    public AIComponent(final Entity entity, final DefaultStateMachine<Entity, S> stateMachine) {
        this.stateMachine = stateMachine;
    }
}
