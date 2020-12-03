package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.AIComponent;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;

public class ClownAISystem extends IteratingSystem {
    private ComponentMapper<AIComponent> aiMapper = ComponentMapper.getFor(AIComponent.class);
    private ComponentMapper<PhysicsComponent> controlMapper = ComponentMapper.getFor(PhysicsComponent.class);

    public ClownAISystem() {
        super(Family.all(AIComponent.class, ControlComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AIComponent ai = aiMapper.get(entity);
        PhysicsComponent control = controlMapper.get(entity);

        ai.stateMachine.update();
        //ai.clownAIData.input.dt = deltaTime;

        //ai.clownAIData.stateMachine.update();
        //ClownAIData data = ai.clownAIData.stateMachine.getOwner();
        //physics.force.x+=data.output.moveForce;
        //ai.clownAIData.input.time+=deltaTime;
        //ai.clownAIData.countDown.act(deltaTime);
    }
}
