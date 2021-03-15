package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.WorldConstantsComponent;

//Moves an entity towards another one in an absolutly brute force way.
public class MoveToEntitySystem extends IteratingSystem  {
    public MoveToEntitySystem() {
        super(Family.all(WorldConstantsComponent.class, PositionComponent.class, PhysicsComponent.class, MoveToEntityComponent.class).get());
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      PositionComponent srcPositionComponent = ComponentMappers.position.get(entity);
      Vector2 srcPosition = new Vector2(srcPositionComponent.position);

      PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);

      MoveToEntityComponent moveToEntityComponent = ComponentMappers.moveToEntity.get(entity);
      if(moveToEntityComponent.isEnable == false)
          return;
      PositionComponent dstPositionComponent = ComponentMappers.position.get(moveToEntityComponent.entity);
      Vector2 dstPositionA = new Vector2(dstPositionComponent.position);

      WorldConstantsComponent worldConstantsComponent = ComponentMappers.worldConstats.get(entity);
      Vector2 dstPositionB = new Vector2(dstPositionComponent.position.x, dstPositionComponent.position.y-worldConstantsComponent.height);

      Vector2 directionToTargetA = new Vector2(dstPositionA.sub(srcPosition));
      Vector2 directionToTargetB = new Vector2(dstPositionB.sub(srcPosition));

      Vector2 directionToTarget = directionToTargetA;
      if(directionToTargetB.len() < directionToTarget.len())
          directionToTarget = directionToTargetB;

      float speed = Math.min(directionToTarget.len()/deltaTime, moveToEntityComponent.maxSpeed);

      directionToTarget.setLength(speed);

      physicsComponent.velocity.set(directionToTarget);
  }
}
