package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.sa.game.StaticEnvironment;

public class GameStateMachine<E, S extends State<E> & GameStateCommon>  {
    DefaultStateMachine<E, S> defaultStateMachine;;
    public float dt;

    public GameStateMachine (E owner, S initialState) {
        defaultStateMachine = new DefaultStateMachine<>(owner, initialState);
    }

    public void update(float dt, boolean groundCollision, boolean wallCollision, StaticEnvironment staticEnvironment) {
        if(defaultStateMachine.getCurrentState() != null) {
            defaultStateMachine.getCurrentState().setDt(dt);
            defaultStateMachine.getCurrentState().setGroundCollision(groundCollision);
            defaultStateMachine.getCurrentState().setWallCollision(wallCollision);
            defaultStateMachine.getCurrentState().setStaticEnvironment(staticEnvironment);
        }
        defaultStateMachine.update();
    }
}
