package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.Game;

public enum GameStateMachine implements State<Game> {
    SHOW_MAIN_MENU() {
        public void update(Game state) {

        }
    },

    SHOW_INTRO() {
        public void update(Game state) {

        }
    },

    START() {
        public void update(Game state) {

        }
    };

    @Override
    public void enter(Game state) {
    }

    @Override
    public void exit(Game state) {
    }

    @Override
    public boolean onMessage(Game state, Telegram telegram) {
        return false;
    }

}
