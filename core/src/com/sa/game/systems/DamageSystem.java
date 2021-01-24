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
        HealthComponent health = ComponentMappers.health.get(entity);

        CollisionComponent collision = ComponentMappers.collision.get(entity);
        for(CollisionEntity colEnt : collision.entity.collidees) {
            Entity colledee = (Entity)colEnt.userData;
            HealthComponent colledeeHealth = ComponentMappers.health.get(colledee);
            if(colledeeHealth != null && damage.stun)
                colledeeHealth.isStunned = damage.stun;

            if(health == null || !health.isStunned)
                if(colledeeHealth != null && colledeeHealth.health > 0) {
                    colledeeHealth.health -= damage.damage;
                }
        }
    }
}
