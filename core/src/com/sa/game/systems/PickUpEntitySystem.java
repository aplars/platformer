package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.PickUpEntityComponent;

public class PickUpEntitySystem extends IteratingSystem {

    public PickUpEntitySystem() {
        super(Family.all(PickUpEntityComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent collision = ComponentMappers.collision.get(entity);

        for(CollisionEntity collidee : collision.entity.collidees) {
            Entity collideeEnt = (Entity)collidee.userData;
            HealthComponent health = ComponentMappers.health.get(collideeEnt);
            if(health != null && health.isStunned) {
                if(!ComponentMappers.moveToEntity.has(collideeEnt)) {
                    collideeEnt.add(new MoveToEntityComponent(entity, new Vector2(0, collision.entity.box.height), 130f));
                    collideeEnt.remove(CollisionComponent.class);
                }
            }
        }
    }
}
