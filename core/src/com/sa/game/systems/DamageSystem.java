package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.DamageComponent;
import com.sa.game.components.HealthComponent;


public class DamageSystem extends IteratingSystem {
    public DamageSystem()
    {
        super(Family.all(DamageComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final DamageComponent damage = ComponentMappers.damage.get(entity);
        final HealthComponent health = ComponentMappers.health.get(entity);
        final CollisionComponent collision = ComponentMappers.collision.get(entity);

        for(final CollisionEntity colEnt : collision.entity.collidees) {
            final Entity colledee = (Entity)colEnt.userData;
            final HealthComponent colledeeHealth = ComponentMappers.health.get(colledee);

            if (colledeeHealth != null && damage.stun) {
                colledeeHealth.isStunned = damage.stun;
                colledeeHealth.stunTime = damage.stunTime;
            }

            if(health == null || !health.isStunned)
                if(colledeeHealth != null && colledeeHealth.health > 0) {
                    colledeeHealth.health -= damage.damage;
                }
        }
    }
}
