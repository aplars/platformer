package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.SensorComponent;

/**
 * The processed entity picks up stunned entities when colliding with them.
 * The picked up entity gets its collision detection fully disabled and it is set to follow the entity.
 *
 */
public class PickUpEntitySystem extends IteratingSystem {
    CollisionDetection collisionDetection;
    public PickUpEntitySystem(CollisionDetection collisionDetection) {
        super(Family.all(PickUpEntityComponent.class, CollisionComponent.class).get());
        this.collisionDetection = collisionDetection;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent collision = ComponentMappers.collision.get(entity);
        PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(entity);

        if(pickUpEntityComponent != null && pickUpEntityComponent.entity != null) {
            HealthComponent h = ComponentMappers.health.get(pickUpEntityComponent.entity);
            if(h != null)
                h.stunTime = Math.max(h.stunTime, 1.0f);
        }

        for(CollisionEntity collidee : collision.entity.collidees) {
            Entity collideeEnt = (Entity)collidee.userData;
            HealthComponent health = ComponentMappers.health.get(collideeEnt);

            if(health != null && health.isStunned() && collidee.groundCollisionData.didCollide) {
                if(!ComponentMappers.moveToEntity.has(collideeEnt) || !ComponentMappers.moveToEntity.get(collideeEnt).isEnable) {
                    if(pickUpEntityComponent.entity == null) {
                        collideeEnt.add(new MoveToEntityComponent(entity, new Vector2(0, collision.entity.box.height), 130f));
                        ComponentMappers.collision.get(collideeEnt).setIsEnable(false);
                        pickUpEntityComponent.entity = collideeEnt;
                    }
                }
            }
        }
    }
}
