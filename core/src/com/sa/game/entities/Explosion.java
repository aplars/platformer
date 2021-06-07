package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.components.ParticleEffectComponent;
import com.sa.game.components.PositionComponent;

public class Explosion {
    static Entity create(Vector2 position) {
        Entity entity = new Entity();
        entity.flags = EntityType.Explosion.type;

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        ParticleEffectComponent particleEffectComponent = new ParticleEffectComponent("particlesystems.party");

        entity.add(positionComponent);
        entity.add(particleEffectComponent);
        return entity;
    }
}
