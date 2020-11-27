package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ClownAIComponent;
import com.sa.game.components.EntityControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.statemachines.ClownAIData;

public class ClownAISystem extends IteratingSystem {
    private ComponentMapper<ClownAIComponent> aiMapper = ComponentMapper.getFor(ClownAIComponent.class);
    private ComponentMapper<PhysicsComponent> physicsMapper = ComponentMapper.getFor(PhysicsComponent.class);

    public ClownAISystem() {
        super(Family.all(ClownAIComponent.class, PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ClownAIComponent ai = aiMapper.get(entity);
        PhysicsComponent physics = physicsMapper.get(entity);

        ai.clownAIData.input.dt = deltaTime;

        ai.clownAIData.stateMachine.update();
        ClownAIData data = ai.clownAIData.stateMachine.getOwner();
        physics.force.x+=data.output.moveForce;
        ai.clownAIData.input.time+=deltaTime;
        ai.clownAIData.countDown.act(deltaTime);
    }
}
