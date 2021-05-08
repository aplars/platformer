package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.components.groups.ExitGroupComponent;

public class Exit {
    static Entity create(Vector2 position, Vector2 size, CollisionDetection collisionDetection) {
        Entity entity = new Entity();

        ExitGroupComponent exitGroupComponent = new ExitGroupComponent();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size.x, size.y);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.OBJECT;
        collisionDetection.add(collisionEntity);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        PositionComponent positionComponent = new PositionComponent();
        positionComponent.position.set(position);

        RenderDebugInfoComponent renderDebugInfoComponent = new RenderDebugInfoComponent();

        entity.add(exitGroupComponent);
        entity.add(collisionComponent);
        entity.add(positionComponent);
        entity.add(renderDebugInfoComponent);
        return entity;
    }
}
