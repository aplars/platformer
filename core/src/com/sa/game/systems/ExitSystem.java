package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.ILoadNextLevel;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.Player1Component;
import com.sa.game.components.groups.ExitGroupComponent;

public class ExitSystem extends IteratingSystem {
    ILoadNextLevel nextLevel;

    public ExitSystem(ILoadNextLevel nextLevel) {
        super(Family.all(ExitGroupComponent.class, CollisionComponent.class).get());
        this.nextLevel = nextLevel;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

        for(CollisionEntity ent : collisionComponent.entity.collidees) {
            //ent is colliding with the exit, check if ent is a player.
            Player1Component player1Component = ComponentMappers.player1.get((Entity)ent.userData);
            if(player1Component != null) {
                nextLevel.nextLevel(player1Component.score);
            }
        }
    }
}
