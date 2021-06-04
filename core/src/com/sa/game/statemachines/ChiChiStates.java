package com.sa.game.statemachines;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.SensorComponent;

public enum ChiChiStates implements State<Entity> {
    FLYING() {
        @Override public void update(Entity entity) {
            SensorComponent sensorComponent = ComponentMappers.sensor.get(entity);
            boolean didCollideWall = sensorComponent.wallCollisionLeft || sensorComponent.wallCollisionRight;
            boolean didCollideGround = sensorComponent.isOnground;

            if (ComponentMappers.sensor.get(entity).isOnTop && ComponentMappers.control.get(entity).buttonUp) {
                down(entity);
            }
            if(ComponentMappers.control.get(entity).buttonRight == true && didCollideWall /*ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide*/) {
                left(entity);
            }
            else if(ComponentMappers.control.get(entity).buttonLeft == true && didCollideWall /*ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide*/) {
                right(entity);
            }
            if (didCollideGround && ComponentMappers.control.get(entity).buttonDown) {
                up(entity);
            }
            else if (didCollideGround && ComponentMappers.control.get(entity).buttonUp) {
                //    down(entity);
            }
        }
    },
    IDLE() {},
    START_RIGHT() {
        @Override
            public void update(Entity entity) {
            ComponentMappers.control.get(entity).buttonLeft=true;
            ComponentMappers.control.get(entity).buttonUp=true;
            ComponentMappers.ai.get(entity).stateMachine.changeState(FLYING);
        }
    },
    START_LEFT() {
        @Override
            public void update(Entity entity) {
            ComponentMappers.control.get(entity).buttonLeft=true;
            ComponentMappers.control.get(entity).buttonUp=true;
            ComponentMappers.ai.get(entity).stateMachine.changeState(FLYING);
        }
    };

    private static void left(final Entity data) {
        ComponentMappers.control.get(data).buttonLeft = true;
        ComponentMappers.control.get(data).buttonRight = false;
    }

    private static void right(final Entity data) {
        ComponentMappers.control.get(data).buttonLeft = false;
        ComponentMappers.control.get(data).buttonRight = true;
    }

    private static void up(final Entity data) {
        ComponentMappers.control.get(data).buttonUp = true;
        ComponentMappers.control.get(data).buttonDown = false;
    }

    private static void down(final Entity data) {
        ComponentMappers.control.get(data).buttonUp = false;
        ComponentMappers.control.get(data).buttonDown = true;
    }

    @Override
    public void enter(Entity entity) {
    }

    @Override
    public void update(Entity entity) {
    }

    @Override
    public void exit(Entity entity) {
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
