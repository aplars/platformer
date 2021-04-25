package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.Player1Component;
import com.sa.game.components.groups.CoinGroupComponent;

public class CoinSystem extends IteratingSystem {
    CollisionDetection collisionDetection;

    public CoinSystem(final CollisionDetection collisionDetection) {
        super(Family.all(CoinGroupComponent.class, CollisionComponent.class).get());
        this.collisionDetection = collisionDetection;
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final CoinGroupComponent coinGroupComponent = ComponentMappers.coinGroup.get(entity);
        final CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

        for (final CollisionEntity collisionEntity : collisionComponent.entity.collidees) {
            final Player1Component player1Component = ComponentMappers.player1.get((Entity) collisionEntity.userData);
            if (player1Component != null) {
                player1Component.score += coinGroupComponent.points;
                this.getEngine().removeEntity(entity);
                this.collisionDetection.remove(collisionComponent.entity);
            }
        }
    }
}
