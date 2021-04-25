package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ExplodeOnContactComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.groups.EnemyGroupComponent;
import com.sa.game.entities.CreateEnteties;

public class ExplodeEnemyOnContactSystem extends IteratingSystem {
    CollisionDetection collisionDetection;
    public ExplodeEnemyOnContactSystem(final CollisionDetection collisionDetection) {
        super(Family.all(EnemyGroupComponent.class, ExplodeOnContactComponent.class, CollisionComponent.class, PositionComponent.class).get());
        this.collisionDetection = collisionDetection;
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final PositionComponent positionComponent = ComponentMappers.position.get(entity);
        final ExplodeOnContactComponent explodeOnContactComponent = ComponentMappers.explodeOnContact.get(entity);

        boolean isColliding = ComponentMappers.collision.get(entity).entity.collidees.size() > 0;
        //Handle ecplosions collision vs entities
        for(final CollisionEntity entityThatCollidesExplosion : ComponentMappers.collision.get(entity).entity.collidees) {
            //give points to the guilty one
            if (explodeOnContactComponent.theGuiltyEntity != null && ComponentMappers.enemyGroup.get((Entity)entityThatCollidesExplosion.userData) != null) {
                final Player1Component player1Component = ComponentMappers.player1.get(explodeOnContactComponent.theGuiltyEntity);
                if (player1Component != null) {
                    player1Component.score++;
                }
                this.getEngine().removeEntity((Entity)entityThatCollidesExplosion.userData);
                collisionDetection.remove(entityThatCollidesExplosion);
            }
        }

        isColliding |= ComponentMappers.collision.get(entity).entity.groundCollisionData.didCollide;
        isColliding |= ComponentMappers.collision.get(entity).entity.wallsCollisionData.didCollide;
        if (isColliding) {
            //give points to the guilty one
            if (explodeOnContactComponent.theGuiltyEntity != null) {
                final Player1Component player1Component = ComponentMappers.player1
                        .get(explodeOnContactComponent.theGuiltyEntity);
                if (player1Component != null) {
                    player1Component.score++;
                }
            }
            this.collisionDetection.remove(ComponentMappers.collision.get(entity).entity);
            this.getEngine().removeEntity(entity);
            this.getEngine().addEntity(CreateEnteties.explosion(positionComponent.position));
        }
    }
}
