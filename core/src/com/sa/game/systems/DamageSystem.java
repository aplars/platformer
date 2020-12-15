package com.sa.game.systems;

import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.DamageComponent;
import com.sa.game.components.HealthComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;


public class DamageSystem extends IteratingSystem {
    public DamageSystem()
    {
        super(Family.all(DamageComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DamageComponent damage = ComponentMappers.damage.get(entity);
        CollisionComponent collision = ComponentMappers.collision.get(entity);
        for(CollisionEntity colEnt : collision.entity.collidees) {
            Entity colledee = (Entity)colEnt.userData;
             HealthComponent health = ComponentMappers.health.get(colledee);
             if(health != null && damage.stun)
                 health.isStunned = damage.stun;
        }
    }
}
