package com.sa.game.statemachines;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.components.ComponentMappers;

public enum ClownAIState implements State<Entity> {
    STUNNED() {
    },
    WALK() {
        @Override public void update(final Entity data) {
            if(ComponentMappers.health.get(data).isStunned) {
                ComponentMappers.ai.get(data).stateMachine.changeState(STUNNED);
                ComponentMappers.control.get(data).buttonLeft = false;
                ComponentMappers.control.get(data).buttonRight = false;
            }
            if(ComponentMappers.control.get(data).buttonRight == true && ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide) {
                ComponentMappers.control.get(data).buttonLeft = true;
                ComponentMappers.control.get(data).buttonRight = false;
            }
            else if(ComponentMappers.control.get(data).buttonLeft == true && ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide) {
                ComponentMappers.control.get(data).buttonLeft = false;
                ComponentMappers.control.get(data).buttonRight = true;
            }
        }
        @Override public void enter(final Entity data) {
            ComponentMappers.control.get(data).buttonLeft = true;
        }
    },
    IDLE() {
        @Override public void enter(final Entity data) {
        }

        @Override public void update(final Entity data) {
        }
    },
    START() {
        @Override public void update(final Entity data) {
            ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
        }
    };


    ClownAIState() {}

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
