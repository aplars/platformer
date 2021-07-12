package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.WorldConstantsComponent;

public class WrapEntitySystem extends IteratingSystem {
    public WrapEntitySystem() {
        super(Family.all(WorldConstantsComponent.class, PositionComponent.class).get());  
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final WorldConstantsComponent worldConstantsComponent = ComponentMappers.worldConstats.get(entity);
        final PositionComponent positionComponent = ComponentMappers.position.get(entity);
        if(worldConstantsComponent != null && positionComponent.position.y < 0)
            positionComponent.position.y = worldConstantsComponent.height;
        if(worldConstantsComponent != null && positionComponent.position.x < 0)
            positionComponent.position.x = worldConstantsComponent.width;
        if(worldConstantsComponent != null && positionComponent.position.x > worldConstantsComponent.width)
            positionComponent.position.x = 0f;
    }
}
