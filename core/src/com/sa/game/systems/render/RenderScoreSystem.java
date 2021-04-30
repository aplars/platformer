package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.groups.CoinGroupComponent;
import com.sa.game.gfx.Renderer;
import com.sa.game.gfx.Text;


public class RenderScoreSystem extends IteratingSystem {
    Renderer renderer;

    public RenderScoreSystem(final Renderer renderer) {
        super(Family.all(CoinGroupComponent.class, PositionComponent.class).get());
        this.renderer = renderer;
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final CoinGroupComponent coinGroupComponent = ComponentMappers.coinGroup.get(entity);
        final PositionComponent positionComponent = ComponentMappers.position.get(entity);

        if(coinGroupComponent.thief != null && coinGroupComponent.timeSinceTaken < 1.0f) {
            final Text text = new Text();
            text.font = Text.Font.Big;
            //text.color.set(1,0,0,1);
            text.chars = String.format("%d", coinGroupComponent.points);
            text.x = positionComponent.position.x-8;
            text.y = positionComponent.position.y+4;
            renderer.add(text);
        }
    }
}
