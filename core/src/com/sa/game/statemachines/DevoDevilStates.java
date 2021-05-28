package com.sa.game.statemachines;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.sa.game.components.AIComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.SensorComponent;

public enum DevoDevilStates implements State<Entity> {
    STUNNED() {
        @Override public void update(final Entity data) {
            ComponentMappers.health.get(data).stunTime -= ComponentMappers.ai.get(data).deltaTime;
            if (ComponentMappers.health.get(data).stunTime < 0f) {
                ComponentMappers.health.get(data).stun = 3;
                ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
                if(MathUtils.random.nextInt(2) == 0) {
                    DevoDevilStates.left(data);
                }
                else
                {
                    DevoDevilStates.right(data);
                }
            }
        }
    },
    JUMP() {
        @Override
        public void update(final Entity data) {
            AIComponent aiComponent = ComponentMappers.ai.get(data);
            if(ComponentMappers.sensor.get(data).isOnground) {
                aiComponent.stateMachine.changeState(WALK);
            }
        }

        @Override public void enter(final Entity data) {
            ComponentMappers.control.get(data).buttonA = true;
            ComponentMappers.ai.get(data).countDownTimers.put("timetothink", .5f);
        }
    },
    WALK() {
        @Override public void update(final Entity data) {
            SensorComponent sensorComponent = ComponentMappers.sensor.get(data);
            boolean didCollideWall = sensorComponent.wallCollisionLeft || sensorComponent.wallCollisionRight;

            AIComponent aiComponent = ComponentMappers.ai.get(data);

            Float timetothink = (Float)aiComponent.countDownTimers.get("timetothink");
            if(timetothink <= 0) {
                //aiComponent.stateMachine.changeState(JUMP);
            }
            if(sensorComponent.isOnground && !sensorComponent.groundOnNextTile && !didCollideWall)
            {
                int num = MathUtils.random.nextInt(100);
                if(num < 3)
                    aiComponent.stateMachine.changeState(JUMP);
            }
            if(ComponentMappers.health.get(data).isStunned()) {
                ComponentMappers.ai.get(data).stateMachine.changeState(STUNNED);
                ComponentMappers.control.get(data).buttonLeft = false;
                ComponentMappers.control.get(data).buttonRight = false;
            }
            if(ComponentMappers.control.get(data).buttonRight == true && didCollideWall /*ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide*/) {
                DevoDevilStates.left(data);
            }
            else if(ComponentMappers.control.get(data).buttonLeft == true && didCollideWall /*ComponentMappers.collision.get(data).entity.wallsCollisionData.didCollide*/) {
                DevoDevilStates.right(data);
            }
        }
        @Override public void enter(final Entity data) {
            //ComponentMappers.control.get(data).buttonLeft = true;
            AIComponent aiComponent = ComponentMappers.ai.get(data);
            aiComponent.countDownTimers.put("timetothink", (float)MathUtils.random.nextInt(10));
            ComponentMappers.control.get(data).buttonA = false;
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
            DevoDevilStates.right(data);
            ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
        }
        @Override public void enter(final Entity data) {
            DevoDevilStates.right(data);
        }
    },
    START_LEFT() {
        @Override public void update(final Entity data) {
            DevoDevilStates.left(data);
            ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
        }
        @Override public void enter(final Entity data) {
            DevoDevilStates.left(data);
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
