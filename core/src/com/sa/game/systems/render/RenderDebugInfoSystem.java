package com.sa.game.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PositionComponent;
import com.sa.game.components.RenderComponent;
import com.sa.game.components.RenderDebugInfoComponent;
import com.sa.game.gfx.Renderer;
import com.sa.game.gfx.Text;

public class RenderDebugInfoSystem extends IteratingSystem {
    private Renderer renderer;
    private StaticEnvironment staticEnvironment;

    public RenderDebugInfoSystem(Renderer renderer, StaticEnvironment staticEnvironment) {
        super(Family.all(RenderDebugInfoComponent.class, RenderComponent.class, PositionComponent.class, PhysicsComponent.class).get());
        this.renderer = renderer;
        this.staticEnvironment = staticEnvironment;
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        //PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
        //PositionComponent positionComponent = ComponentMappers.position.get(entity);
        //RenderComponent renderComponent = ComponentMappers.render.get(entity);
        CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

        Rectangle rect = new Rectangle(collisionComponent.entity.box);
        rect.x += collisionComponent.offset.x;
        rect.y += collisionComponent.offset.y;

        float xMax = collisionComponent.entity.box.x+collisionComponent.entity.box.width;
        float xMin = collisionComponent.entity.box.x;
        Text txt = new Text();
        txt.chars = String.format("%d, %d", (int)(xMin/staticEnvironment.tileSizeInPixels), (int)(xMax/staticEnvironment.tileSizeInPixels));
        txt.x = rect.x; //positionComponent.position.x-renderComponent.sprite.size.x/2f;
        txt.y = rect.y; //positionComponent.position.y+renderComponent.sprite.size.y;
        renderer.add(txt);

        renderer.add(rect);

        Vector2 cent = new Vector2();
        collisionComponent.entity.box.getCenter(cent);

        //Circle circle = new Circle(positionComponent.position.x, positionComponent.position.y, 10);
        Circle circle = new Circle(cent.x, cent.y, 10);
        renderer.add(circle);
    }
}
