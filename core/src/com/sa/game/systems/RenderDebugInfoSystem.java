package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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
    private BitmapFont font = new BitmapFont();
    private SpriteBatch batch = new SpriteBatch();
    private OrthographicCamera camera;
    private OrthographicCamera fontCamera;
    private Renderer renderer;
    private StaticEnvironment staticEnvironment;

    public RenderDebugInfoSystem(Renderer renderer, OrthographicCamera camera, OrthographicCamera fontCamera, StaticEnvironment staticEnvironment) {
        super(Family.all(RenderDebugInfoComponent.class, RenderComponent.class, PositionComponent.class, PhysicsComponent.class).get());
        this.camera = camera;
        this.fontCamera = fontCamera;
        this.renderer = renderer;
        this.staticEnvironment = staticEnvironment;
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        PhysicsComponent physicsComponent = ComponentMappers.physics.get(entity);
        PositionComponent positionComponent = ComponentMappers.position.get(entity);
        RenderComponent renderComponent = ComponentMappers.render.get(entity);
        CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);

        float xMax = collisionComponent.entity.box.x+collisionComponent.entity.box.width;
        float xMin = collisionComponent.entity.box.x;
        Text txt = new Text();
        txt.chars = String.format("%d, %d", (int)(xMin/staticEnvironment.tileSizeInPixels), (int)(xMax/staticEnvironment.tileSizeInPixels));
        txt.x = positionComponent.position.x-renderComponent.sprite.size.x/2f;
        txt.y = positionComponent.position.y+renderComponent.sprite.size.y;
        renderer.add(txt);

        renderer.add(collisionComponent.entity.box);
    }
}
