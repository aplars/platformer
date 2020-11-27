package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.statemachines.ClownAIData;
import com.sa.game.statemachines.ClownAIState;

public class ClownAIComponent implements Component {
    public ClownAIData clownAIData;
    public ClownAIComponent(ClownAIData data) {
        clownAIData = data;
    }
}
