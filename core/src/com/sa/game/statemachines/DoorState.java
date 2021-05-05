package com.sa.game.statemachines;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;

public enum DoorState implements State<Entity> {
    OPEN()  {
        @Override
        public void update(final Entity data) {
        }

        @Override
        public void enter(final Entity data) {

        }
    },
    CLOSED() {
        @Override
        public void update(final Entity data) {
            /*final CollisionComponent collisionComponent = ComponentMappers.collision.get(data);
            for(final CollisionEntity collisionEntity : collisionComponent.entity.collidees) {
                final Entity colEnt = (Entity)collisionEntity.userData;
                if (ComponentMappers.player1.get(colEnt) != null && ComponentMappers.pickUp.get(colEnt) != null) {
                    final Entity pickUpEnt = ComponentMappers.pickUp.get(colEnt).entity;
                    if (pickUpEnt != null) {
                        if (ComponentMappers.keyGroup.get(pickUpEnt) != null) {
                            ComponentMappers.ai.get(data).stateMachine.changeState(OPEN);
                        }
                    }
                }
                }*/
        }
    };

    DoorState() {}

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
