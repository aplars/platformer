package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.DamageComponent;
import com.sa.game.components.DelayControlComponent;
import com.sa.game.components.HealthComponent;


///The entity gives damage to colliding entities.
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

        for(final CollisionEntity collidee : collision.entity.collidees) {
            final Entity colledeeEntity = (Entity)collidee.userData;
            final HealthComponent colledeeHealthComponent = ComponentMappers.health.get(colledeeEntity);

            //Stun the collidee if this entity can stun.
            if (colledeeHealthComponent != null && damage.stun > 0) {
                colledeeHealthComponent.stun -= damage.stun;
                colledeeHealthComponent.stunTime = damage.stunTime;
                damage.stun = 0;
                //colledeeEntity.add(new DelayControlComponent(1f));
            }

            //Take health from collidee if entity is not stunned.
            if (health == null || !health.isStunned()) {
                if (colledeeHealthComponent != null && colledeeHealthComponent.health > 0) {
                    colledeeHealthComponent.health -= damage.damage;
                }
            }
        }
    }
}
