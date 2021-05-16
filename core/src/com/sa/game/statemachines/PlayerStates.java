package com.sa.game.statemachines;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.WorldConstantsComponent;


public enum PlayerStates implements State<Entity> {
    RESPAWN() { 
    },
    REMOVEFROMGAME() {
    },
    DEAD() {
        @Override
        public void update(final Entity data) {
            PositionComponent positionComponent = ComponentMappers.position.get(data);
            if(positionComponent.position.y <= 0) {
                if(ComponentMappers.health.get(data).lives > 0)
                    ComponentMappers.ai.get(data).stateMachine.changeState(RESPAWN);
                else
                    ComponentMappers.ai.get(data).stateMachine.changeState(REMOVEFROMGAME);
            }
        }
        @Override
        public void enter(final Entity data) {
            ComponentMappers.collision.get(data).setIsEnable(false);
            ComponentMappers.control.get(data).buttonA = true;
            ComponentMappers.health.get(data).lives--;
            if(ComponentMappers.pickUp.get(data).entity != null)
                ComponentMappers.control.get(data).buttonB = true;
        }
    },
    JUMP() {
        public void update(final Entity data) {
            if(ComponentMappers.health.get(data).health == 0) {
                ComponentMappers.ai.get(data).stateMachine.changeState(DEAD);
            }
            if(ComponentMappers.collision.get(data).entity.groundCollisionData.didCollide) {
                ComponentMappers.ai.get(data).stateMachine.changeState(IDLE);
            }
        }
    },
    WALK() {
        @Override
        public void update(final Entity data) {
            if(ComponentMappers.health.get(data).health == 0) {
                ComponentMappers.ai.get(data).stateMachine.changeState(DEAD);
            }
            if(!ComponentMappers.collision.get(data).entity.groundCollisionData.didCollide) {
                ComponentMappers.ai.get(data).stateMachine.changeState(JUMP);
            }
            if (Math.abs(ComponentMappers.physics.get(data).velocity.x) <= 0.9f) {
                ComponentMappers.ai.get(data).stateMachine.changeState(IDLE);
            }
        }
    },
    IDLE() {
        @Override public void update(final Entity data) {
            if(ComponentMappers.health.get(data).health == 0) {
                ComponentMappers.ai.get(data).stateMachine.changeState(DEAD);
            }
            if(!ComponentMappers.collision.get(data).entity.groundCollisionData.didCollide) {
                ComponentMappers.ai.get(data).stateMachine.changeState(JUMP);
            }
            if(Math.abs(ComponentMappers.physics.get(data).velocity.x) > 16.0f) {
                ComponentMappers.ai.get(data).stateMachine.changeState(WALK);
            }
        }
    },
    START() {
        @Override public void update(final Entity data) {
            ComponentMappers.ai.get(data).stateMachine.changeState(IDLE);
        }
    };

    PlayerStates() {}

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
