package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.Player1Component;
import com.sa.game.entities.CoinGroupComponent;

public class CoinSystem extends IteratingSystem {
    public CoinSystem() {
        super(Family.all(CoinGroupComponent.class, CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CoinGroupComponent coinGroupComponent = ComponentMappers.coinGroup.get(entity);
        CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

        for (CollisionEntity collisionEntity : collisionComponent.entity.collidees) {
            Player1Component player1Component = ComponentMappers.player1.get((Entity) collisionEntity.userData);
            if (player1Component != null) {
                player1Component.score += coinGroupComponent.points;
                this.getEngine().removeEntity(entity);
            }
        }
    }
}
