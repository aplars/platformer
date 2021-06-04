package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.ILoadNextLevel;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.IGotoGameOverScreen;

public class LastSystem extends IteratingSystem {
    IGotoGameOverScreen loadNextLevel;

    public LastSystem(IGotoGameOverScreen loadNextLevel) {
        super(Family.all(Player1Component.class, HealthComponent.class).get());
        this.loadNextLevel = loadNextLevel;
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        Player1Component player1Component = ComponentMappers.player1.get(entity);
        HealthComponent healthComponent = ComponentMappers.health.get(entity);

        if(healthComponent.lives <= 0) {
            loadNextLevel.gameOverScreen(player1Component.score);
        }
    }
}
