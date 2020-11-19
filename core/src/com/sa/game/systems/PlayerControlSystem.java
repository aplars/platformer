package com.sa.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.EntityControlComponent;
import com.sa.game.components.PhysicsComponent;

public class PlayerControlSystem extends IteratingSystem{
    private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<EntityControlComponent> cm = ComponentMapper.getFor(EntityControlComponent.class);
    private ComponentMapper<CollisionComponent> colm = ComponentMapper.getFor(CollisionComponent.class);

    public PlayerControlSystem() {
        super(Family.all(PhysicsComponent.class).get());
        
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
      PhysicsComponent physicsComponent = pm.get(entity);
      EntityControlComponent entityControlComponent = cm.get(entity); 
      CollisionComponent collisionComponent = colm.get(entity);
      if(physicsComponent != null && entityControlComponent != null && collisionComponent != null) {
          if (Gdx.input.isKeyPressed(Input.Keys.A)) {
              physicsComponent.force.x -= entityControlComponent.moveForce;
          }
          if (Gdx.input.isKeyPressed(Input.Keys.D)) {
              physicsComponent.force.x += entityControlComponent.moveForce;
          }
          if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && collisionComponent.entity.groundCollisionData.didCollide) {
              physicsComponent.velocity.y = entityControlComponent.jumpImpulse;
          }
          if(Gdx.input.isKeyPressed(Input.Keys.W)) {
              //float projDir = (physicsComponent.walkDirection == WalkDirection.Left) ? -300f : 300f;
              //playerStunProjectiles.add(CreateEnteties.playerStunProjectile(new Vector2(positionComponent.position), new Vector2(projDir, 0f), tileSizeInPixels, collisionDetection, preUpdateEngine, updateEngine));

          }
      }
	}
}
