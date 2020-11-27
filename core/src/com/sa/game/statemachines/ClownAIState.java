package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum ClownAIState implements State<ClownAIData> {
    RESTING() {
        @Override public void update(ClownAIData data) {
        }
    },

    WALK_RIGHT() {
        @Override public void update(ClownAIData data) {
            if(data.input.collisionEntity.wallsCollisionData.didCollide) {
                data.stateMachine.changeState(WALK_LEFT);
            }
            data.output.moveForce = data.constants.moveForce;
        }
    },
    WALK_LEFT() {
        @Override public void update(ClownAIData data) {
            if(data.input.collisionEntity.wallsCollisionData.didCollide) {
                data.stateMachine.changeState(WALK_RIGHT);
            }
            data.output.moveForce = -data.constants.moveForce;
        }
    },

    IDLE() {
        @Override public void enter(ClownAIData data) {
            data.countDown.setTime(5f);
        }

        @Override public void update(ClownAIData data) {
            if(data.countDown.isComplete())
                data.stateMachine.changeState(WALK_LEFT);
        }
    },

    START() {

        @Override public void update(ClownAIData data) {
            data.stateMachine.changeState(IDLE);
        }
    };


    ClownAIState() {}

    @Override
    public void update(ClownAIData data) {
    }

    @Override
    public void enter(ClownAIData data) {
    }

    @Override
    public void exit(ClownAIData data) {
    }

    @Override
    public boolean onMessage(ClownAIData data, Telegram telegram) {
        return false;
    }
}
