package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.AIComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.HealthComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderSpriteInFlickeringColorsComponent;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.statemachines.PlayerStates;

public class RespawnPlayer1System extends IteratingSystem {
    AssetManager assetManager;
    CollisionDetection collisionDetection;
    StaticEnvironment staticEnvironment;

    public RespawnPlayer1System(final AssetManager assetManager, final CollisionDetection collisionDetection, final StaticEnvironment staticEnvironment) {
        super(Family.all(Player1Component.class, AIComponent.class, HealthComponent.class, RenderComponent.class).get());
        this.assetManager = assetManager;
        this.collisionDetection = collisionDetection;
        this.staticEnvironment = staticEnvironment;
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final Player1Component player1Component = ComponentMappers.player1.get(entity);
        final AIComponent aiComponent = ComponentMappers.ai.get(entity);
        final HealthComponent healthComponent = ComponentMappers.health.get(entity);
        final RenderComponent renderComponent = ComponentMappers.render.get(entity);

        if (aiComponent.stateMachine.getCurrentState() == PlayerStates.RESPAWN) {
            Entity newEntity =  CreateEnteties.player(assetManager,
                                                      0,
                                                      player1Component.score,
                                                      healthComponent.lives,
                                                      3.0f,
                                                      player1Component.initialPosition,
                                                      renderComponent.sprite.size,
                                                      renderComponent.sprite.mirrorX,
                                                      staticEnvironment,
                                                      collisionDetection);
            newEntity.add(new RenderSpriteInFlickeringColorsComponent(3));
            this.getEngine().addEntity(newEntity);
            this.getEngine().removeEntity(entity);
      }
  }
}
