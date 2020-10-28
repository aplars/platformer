package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.GameWorld;

public enum GameStateMachine implements State<GameWorld> {
    SHOW_MAIN_MENU() {
        public void update(GameWorld state) {

        }
    },

    SHOW_INTRO() {
        public void update(GameWorld state) {

        }
    },

    START() {
        public void update(GameWorld state) {

        }
    };

    @Override
    public void enter(GameWorld state) {
    }

    @Override
    public void exit(GameWorld state) {
    }

    @Override
    public boolean onMessage(GameWorld state, Telegram telegram) {
        return false;
    }

}
