package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.groups.DoorGroupComponent;
import com.sa.game.statemachines.DoorState;

public class OpenDoorSystem extends IteratingSystem {
    public OpenDoorSystem() {
        super(Family.all(DoorGroupComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //Open the door if it collides with an entity carrying a key.
        CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

        for (CollisionEntity collidee : collisionComponent.entity.collidees) {
            Entity collideeEnt = (Entity)collidee.userData;
            PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(collideeEnt);
            if (pickUpEntityComponent != null && pickUpEntityComponent.entity!= null && ComponentMappers.keyGroup.get(pickUpEntityComponent.entity) != null) {
                ComponentMappers.ai.get(entity).stateMachine.changeState(DoorState.OPEN);
                this.getEngine().removeEntity(pickUpEntityComponent.entity);
                pickUpEntityComponent.entity = null;
            }
        }
    }
}
