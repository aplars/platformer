package com.sa.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.gfx.Renderer;
import com.sa.game.systems.AISystem;
import com.sa.game.systems.AnimationSystem;
import com.sa.game.systems.CoinSystem;
import com.sa.game.systems.CollisionSystem;
import com.sa.game.systems.control.ControlMovementSystem;
import com.sa.game.systems.control.ControlPunchSystem;
import com.sa.game.systems.control.ControlThrowEntitySystem;
import com.sa.game.systems.DamageSystem;
import com.sa.game.systems.DampingSystem;
import com.sa.game.systems.DroppedSystem;
import com.sa.game.systems.ExplodeBoxingGloveOnContactSystem;
import com.sa.game.systems.ExplodeEnemyOnContactSystem;
import com.sa.game.systems.LastSystem;
import com.sa.game.systems.MoveToEntitySystem;
import com.sa.game.systems.MovementSystem;
import com.sa.game.systems.PhysicsSystem;
import com.sa.game.systems.PickUpEntitySystem;
import com.sa.game.systems.control.PlayerInputSystem;
import com.sa.game.systems.render.RenderScoreSystem;
import com.sa.game.systems.render.RenderStarsSystem;
import com.sa.game.systems.render.RenderSystem;
import com.sa.game.systems.ResolveCollisionSystem;
import com.sa.game.systems.ThrownSystem;
import com.sa.game.systems.WrapEntitySystem;
import com.sa.game.systems.render.RenderDebugInfoSystem;
import com.sa.game.systems.render.RenderParticleSystem;

public class GameWorld {
    //game entities
    StaticEnvironment staticEnvironment = new StaticEnvironment();
    //////////////////////////////////////////////////////////////
    OrthographicCamera camera = new OrthographicCamera();
    OrthographicCamera fontCamera = new OrthographicCamera();
    Renderer renderer = new Renderer();
    CollisionDetection collisionDetection = new CollisionDetection();
    AssetManager assetManager = new AssetManager();
    OrthogonalTiledMapRenderer mapRenderer;

    int visiblelayers[] = {};

    Engine updateEngine = new Engine();

    PerformanceCounters performanceCounters;

    public GameWorld(final PerformanceCounters performanceCounters) {
        this.performanceCounters = performanceCounters;
    }

    public void setVisibleLayers(final int layers[]) {
        visiblelayers = layers;
    }

    public void preUpdate(final float dt, final Controller controller) {
    }

    public void update(final float dt) {
        updateEngine.update(dt);
        camera.update();
    }

    public void render(final float dt) {
        if(mapRenderer != null)
            mapRenderer.setView(camera);

        if(mapRenderer != null)
            mapRenderer.render(visiblelayers);

        renderer.render(camera, fontCamera);
    }

    public void resize(final float aspectRatio) {
        if(staticEnvironment == null)
            return;
        float w = 0f;
        if(Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
            camera.setToOrtho(false,
                              (staticEnvironment.getWidth()),
                              (staticEnvironment.getWidth())*aspectRatio);
            w = staticEnvironment.getWidth();
        }
        else {
            camera.setToOrtho(false,
                              (staticEnvironment.getWidth())*aspectRatio,
                              (staticEnvironment.getWidth()));
            w = staticEnvironment.getWidth()*aspectRatio;
        }
        camera.translate(staticEnvironment.getWidth()/2f-w/2f, 0f);
        camera.update();

        fontCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fontCamera.update();
    }

    public boolean loadLevel() {
        collisionDetection.clear();
        staticEnvironment.dispose();
        assetManager.clear();
        assetManager.dispose();
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load("level_1.tmx", TiledMap.class);
        assetManager.finishLoading();
        final TiledMap tiledMap = assetManager.get("level_1.tmx", TiledMap.class);
        staticEnvironment = new StaticEnvironment(tiledMap, collisionDetection);
        assetManager.finishLoading();

        updateEngine = new Engine();
        updateEngine.removeAllEntities();

        for(final StaticEnvironment.Entity entity : staticEnvironment.entities) {
            if (entity.name.equals("clown")) {
                updateEngine.addEntity(CreateEnteties.enemy(assetManager,
                                                            entity.position,
                                                            entity.size.y,
                                                            staticEnvironment,
                                                            collisionDetection));
            }
            if(entity.name.equals("player")) {
                final Entity player = CreateEnteties.player(assetManager,
                                                      entity.position,
                                                      entity.size,
                                                      staticEnvironment,
                                                      collisionDetection);
                updateEngine.addEntity(player);
                //updateEngine.addEntity(CreateEnteties.boxingGlove(assetManager, entity.position, 12, player, staticEnvironment, collisionDetection));
            }
            if (entity.name.equals("key")) {

                updateEngine.addEntity(CreateEnteties.key(assetManager,
                                                          entity.position,
                                                          entity.size.y,
                                                          staticEnvironment,
                                                          collisionDetection));
            }
            if (entity.name.equals("apple")) {
                updateEngine.addEntity(CreateEnteties.apple(assetManager,
                                                            entity.position,
                                                            entity.size.y,
                                                            staticEnvironment,
                                                            collisionDetection));
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(staticEnvironment.getMap());

        assetManager.finishLoading();
        while(!assetManager.isFinished())
            assetManager.update();

        updateEngine.addSystem(new PlayerInputSystem());
        updateEngine.addSystem(new AISystem());
        updateEngine.addSystem(new ControlMovementSystem(assetManager, collisionDetection, staticEnvironment));
        updateEngine.addSystem(new ControlPunchSystem(assetManager, collisionDetection, staticEnvironment));
        updateEngine.addSystem(new ControlThrowEntitySystem());
        updateEngine.addSystem(new ThrownSystem());
        updateEngine.addSystem(new DroppedSystem());
        updateEngine.addSystem(new MoveToEntitySystem());
        updateEngine.addSystem(new PhysicsSystem());
        updateEngine.addSystem(new CollisionSystem(performanceCounters.add("collision"), collisionDetection, staticEnvironment));
        updateEngine.addSystem(new DamageSystem());
        updateEngine.addSystem(new PickUpEntitySystem(collisionDetection));
        updateEngine.addSystem(new ExplodeBoxingGloveOnContactSystem(collisionDetection));
        updateEngine.addSystem(new ExplodeEnemyOnContactSystem(collisionDetection));
        updateEngine.addSystem(new CoinSystem(collisionDetection));
        updateEngine.addSystem(new ResolveCollisionSystem(performanceCounters.add("resolvecollision")));
        updateEngine.addSystem(new WrapEntitySystem());
        updateEngine.addSystem(new MovementSystem());
        updateEngine.addSystem(new DampingSystem());
        updateEngine.addSystem(new AnimationSystem<>());
        updateEngine.addSystem(new RenderSystem(performanceCounters.add("render"), renderer));
        updateEngine.addSystem(new RenderParticleSystem(camera));
        updateEngine.addSystem(new RenderStarsSystem(renderer));
        updateEngine.addSystem(new RenderScoreSystem(renderer, camera, fontCamera, staticEnvironment));
        updateEngine.addSystem(new LastSystem());
        //updateEngine.addSystem(new RenderDebugInfoSystem(renderer, staticEnvironment));

        return true;
    }
}
