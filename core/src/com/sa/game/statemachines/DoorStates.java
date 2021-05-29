package com.sa.game.statemachines;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.components.ComponentMappers;


public enum DoorStates implements State<Entity> {
    OPEN(),
    CLOSED() {
        @Override
        public void update(final Entity data) {
            if(ComponentMappers.doorGroup.get(data).isOpen)
                ComponentMappers.ai.get(data).stateMachine.changeState(OPEN);
        }
    };

    DoorStates() {}

    @Override
    public void update(final Entity data) {
    }

    @Override
    public void enter(final Entity data) {
    }

    @Override
    public void exit(final Entity data) {
    }

    @Override
    public boolean onMessage(final Entity data, final Telegram telegram) {
        return false;
    }
}
