package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ExplodeOnContactComponent;
import com.sa.game.components.MoveToEntityComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.ThrownComponent;
import com.sa.game.components.groups.EnemyGroupComponent;
import com.sa.game.entities.WalkDirection;

public class ThrownSystem extends IteratingSystem{
    public ThrownSystem() {
        super(Family.all(EnemyGroupComponent.class, ThrownComponent.class, PhysicsComponent.class, CollisionComponent.class, MoveToEntityComponent.class).get());
    }

	@Override
	protected void processEntity(final Entity entity, final float deltaTime) {
       final ThrownComponent thrownComponent =  ComponentMappers.thrown.get(entity);
       final PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
       final CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

       collisionComponent.entity.isEnable = true;
       collisionComponent.entity.filter.mask &= ~CollisionFilter.PLAYER; // Disable collision vs player 

       if (thrownComponent.direction == WalkDirection.Right) 
           physicsComponent.velocity.x = 300;
       else
           physicsComponent.velocity.x = -300;

       physicsComponent.friction = 1f;
       physicsComponent.airResistance = 1f;

       entity.add(new ExplodeOnContactComponent());

       entity.remove(MoveToEntityComponent.class);
       entity.remove(ThrownComponent.class);
  }
}