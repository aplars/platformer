package com.sa.game.statemachines;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.SensorComponent;

public enum DevoDevilStates implements State<Entity> {
    STUNNED() {
        @Override public void update(final Entity data) {
            ComponentMappers.health.get(data).stunTime -= ComponentMappers.ai.get(data).deltaTime;
            if (ComponentMappers.health.get(data).stunTime < 0f) {
                ComponentMappers.health.get(data).isStunned = false;
                ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
            }
        }
    },
    WALK() {
        @Override public void update(final Entity data) {
            SensorComponent sensorComponent = ComponentMappers.sensor.get(data);
            boolean didCollide = sensorComponent.wallCollisionLeft || sensorComponent.wallCollisionRight;

            if(ComponentMappers.health.get(data).isStunned) {
                ComponentMappers.ai.get(data).stateMachine.changeState(STUNNED);
                ComponentMappers.control.get(data).buttonLeft = false;
                ComponentMappers.control.get(data).buttonRight = false;
            }
            if(ComponentMappers.control.get(data).buttonRight == true && didCollide /*ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide*/) {
                ComponentMappers.control.get(data).buttonLeft = true;
                ComponentMappers.control.get(data).buttonRight = false;
            }
            else if(ComponentMappers.control.get(data).buttonLeft == true && didCollide /*ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide*/) {
                ComponentMappers.control.get(data).buttonLeft = false;
                ComponentMappers.control.get(data).buttonRight = true;
            }
        }
        @Override public void enter(final Entity data) {
            //ComponentMappers.control.get(data).buttonLeft = true;
        }
    },
    IDLE() {
        @Override public void enter(final Entity data) {
        }

        @Override public void update(final Entity data) {
        }
    },
    START_RIGHT() {
        @Override public void update(final Entity data) {
            ComponentMappers.control.get(data).buttonLeft = false;
            ComponentMappers.control.get(data).buttonRight = true;
            ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
        }
        @Override public void enter(final Entity data) {
            ComponentMappers.control.get(data).buttonLeft = false;
            ComponentMappers.control.get(data).buttonRight = true;
        }
    },
    START_LEFT() {
        @Override public void update(final Entity data) {
            ComponentMappers.control.get(data).buttonLeft = true;
            ComponentMappers.control.get(data).buttonRight = false;
            ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
        }
        @Override public void enter(final Entity data) {
            ComponentMappers.control.get(data).buttonLeft = true;
            ComponentMappers.control.get(data).buttonRight = false;
        }
    };


    DevoDevilStates() {}

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
