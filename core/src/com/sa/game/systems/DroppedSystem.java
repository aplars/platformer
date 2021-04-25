package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.RectangleCollisionData;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.ThrownComponent;
import com.sa.game.components.groups.ToolGroupComponent;

public class DroppedSystem extends IteratingSystem {
    public DroppedSystem() {
        super(Family.all(ToolGroupComponent.class, ThrownComponent.class, CollisionComponent.class).get());
    }

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
      final CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);
      final ThrownComponent thrownComponent = ComponentMappers.thrown.get(entity);

      collisionComponent.entity.isEnable = true;
      collisionComponent.entity.filter.mask &= ~CollisionFilter.PLAYER; // Disable collision vs player 

      entity.remove(MoveToEntityComponent.class);

      final CollisionComponent parentCollisionComponent = ComponentMappers.collision.get(thrownComponent.parent);
      final RectangleCollisionData colData = IntersectionTests.rectangleRectangle(collisionComponent.entity.box, new Vector2(), parentCollisionComponent.entity.box);
      if(!colData.didCollide && collisionComponent.entity.groundCollisionData.didCollide) {
          entity.remove(ThrownComponent.class);
          collisionComponent.entity.filter.mask |= CollisionFilter.PLAYER; // Disable collision vs player 
      }
  }
}
