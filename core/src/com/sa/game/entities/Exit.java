package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.collision.CollisionFilter;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.groups.ExitGroupComponent;

public class Exit {
    static Entity create(Vector2 position, float size, CollisionDetection collisionDetection) {
        Entity entity = new Entity();

        ExitGroupComponent exitGroupComponent = new ExitGroupComponent();

        Rectangle collisionRectangle = new Rectangle();
        collisionRectangle.setSize(size, size);
        collisionRectangle.setCenter(position.x, position.y);
        CollisionEntity collisionEntity = new CollisionEntity();
        collisionEntity.box.set(collisionRectangle);
        collisionEntity.velocity.set(0, 0);
        collisionEntity.userData = entity;
        collisionEntity.filter.category = CollisionFilter.OBJECT;
        collisionDetection.add(collisionEntity);

        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.entity = collisionEntity;

        entity.add(exitGroupComponent);
        entity.add(collisionComponent);
        return entity;
    }
}
