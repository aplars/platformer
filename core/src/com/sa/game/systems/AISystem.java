package com.sa.game.systems;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AIComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.DelayControlComponent;

public class AISystem extends IteratingSystem {
    private final ComponentMapper<AIComponent> aiMapper = ComponentMapper.getFor(AIComponent.class);

    public AISystem() {
        super(Family.all(AIComponent.class, ControlComponent.class)./*exclude(DelayControlComponent.class).*/get());
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final AIComponent ai = aiMapper.get(entity);

        HashMap<String, Float> timers =  ai.countDownTimers;
        if (timers != null) {
            for (Map.Entry<String, Float> e : timers.entrySet()) {
                e.setValue(e.getValue() - deltaTime);
            }
        }
        ai.deltaTime = deltaTime;
        ai.stateMachine.update();
    }
}
