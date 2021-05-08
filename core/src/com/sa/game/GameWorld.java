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
import com.sa.game.models.LayersToRenderModel;
import com.sa.game.systems.AISystem;
import com.sa.game.systems.AnimationSystem;
import com.sa.game.systems.PickUpCoinSystem;
import com.sa.game.systems.CollisionSystem;
import com.sa.game.systems.control.ControlMovementSystem;
import com.sa.game.systems.control.ControlPunchSystem;
import com.sa.game.systems.control.ControlThrowEntitySystem;
import com.sa.game.systems.DamageSystem;
import com.sa.game.systems.DampingSystem;
import com.sa.game.systems.DelayAISystem;
import com.sa.game.systems.DroppedSystem;
import com.sa.game.systems.ExitSystem;
import com.sa.game.systems.ExplodeBoxingGloveOnContactSystem;
import com.sa.game.systems.ExplodeEnemyOnContactSystem;
import com.sa.game.systems.LastSystem;
import com.sa.game.systems.MoveToEntitySystem;
import com.sa.game.systems.MovementSystem;
import com.sa.game.systems.OpenDoorSystem;
import com.sa.game.systems.PhysicsSystem;
import com.sa.game.systems.PickUpEntitySystem;
import com.sa.game.systems.control.PlayerInputSystem;
import com.sa.game.systems.render.RenderScoreBoardSystem;
import com.sa.game.systems.render.RenderStarsSystem;
import com.sa.game.systems.render.RenderSystem;
import com.sa.game.systems.render.RenderScoreSystem;
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
    AssetManager assetManager = null;//new AssetManager();
    OrthogonalTiledMapRenderer mapRenderer;

    int visiblelayers[] = {};

    Engine engine = null;

    public boolean loadNextLevel = false; 
    PerformanceCounters performanceCounters;

    public GameWorld(final PerformanceCounters performanceCounters) {
        this.performanceCounters = performanceCounters;
    }

    public void setVisibleLayers(final int layers[]) {
        visiblelayers = layers;
    }

    public void update(final float dt) {
        engine.update(dt);
        camera.update();
        fontCamera.update();
        //System.out.println("**EndFrame**");
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

    public boolean loadLevel(String level) {
        collisionDetection.clear();
        //staticEnvironment.dispose();

        if(assetManager == null)
            assetManager = new AssetManager();

        assetManager.clear();
        //assetManager.dispose();

        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load(level, TiledMap.class);
        assetManager.finishLoading();
        final TiledMap tiledMap = assetManager.get(level, TiledMap.class);

        if(staticEnvironment == null)
            staticEnvironment = new StaticEnvironment(tiledMap);
        else
            staticEnvironment.setTiledLevel(tiledMap);

        assetManager.finishLoading();

        boolean addSystems = false;
        if (engine == null) {
            engine = new Engine();
            addSystems = true;
        }
        engine.removeAllEntities();

        for(final StaticEnvironment.Entity entity : staticEnvironment.entities) {
            if (entity.name.equals("devo_devil")) {
                engine.addEntity(CreateEnteties.enemy(assetManager,
                                                      entity.position,
                                                      entity.size.y,
                                                      staticEnvironment,
                                                      collisionDetection));
            }
            if (entity.name.equals("key")) {

                engine.addEntity(CreateEnteties.key(assetManager,
                                                    entity.position,
                                                    entity.size.y,
                                                    staticEnvironment,
                                                    collisionDetection));
            }
            if (entity.name.equals("apple")) {
                engine.addEntity(CreateEnteties.apple(assetManager,
                                                      entity.position,
                                                      entity.size.y,
                                                      staticEnvironment,
                                                      collisionDetection));
            }
            if (entity.name.equals("door")) {
                engine.addEntity(CreateEnteties.door(assetManager,
                                                     entity.position,
                                                     entity.size.y,
                                                     collisionDetection));
            }
            if(entity.name.equals("player")) {
                final Entity player = CreateEnteties.player(assetManager,
                                                            entity.position,
                                                            entity.size,
                                                            staticEnvironment,
                                                            collisionDetection);
                engine.addEntity(player);
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(staticEnvironment.getMap());
        assetManager.finishLoading();
        while(!assetManager.isFinished())
            assetManager.update();

        if (addSystems) {
            engine.addSystem(new PlayerInputSystem());
            engine.addSystem(new AISystem());
            engine.addSystem(new OpenDoorSystem(collisionDetection));
            engine.addSystem(new ControlMovementSystem(assetManager, collisionDetection, staticEnvironment));
            engine.addSystem(new ControlPunchSystem(assetManager, collisionDetection, staticEnvironment));
            engine.addSystem(new ControlThrowEntitySystem());
            engine.addSystem(new ThrownSystem());
            engine.addSystem(new DroppedSystem());
            engine.addSystem(new MoveToEntitySystem());
            engine.addSystem(new PhysicsSystem());
            engine.addSystem(new CollisionSystem(performanceCounters.add("collision"), collisionDetection, staticEnvironment));
            engine.addSystem(new DamageSystem());
            engine.addSystem(new PickUpEntitySystem(collisionDetection));
            engine.addSystem(new ExitSystem(new ILoadNextLevel() {
                public void nextLevel() {
                    loadNextLevel = true;
                }
            }));
            engine.addSystem(new ExplodeBoxingGloveOnContactSystem(collisionDetection));
            engine.addSystem(new ExplodeEnemyOnContactSystem(collisionDetection));
            engine.addSystem(new PickUpCoinSystem(collisionDetection));
            engine.addSystem(new ResolveCollisionSystem(performanceCounters.add("resolvecollision")));
            engine.addSystem(new WrapEntitySystem());
            engine.addSystem(new MovementSystem());
            engine.addSystem(new DampingSystem());
            engine.addSystem(new AnimationSystem<>());
            engine.addSystem(new DelayAISystem());
            engine.addSystem(new RenderSystem(performanceCounters.add("render"), renderer));
            engine.addSystem(new RenderParticleSystem(camera));
            engine.addSystem(new RenderStarsSystem(renderer));
            engine.addSystem(new RenderScoreSystem(renderer));
            engine.addSystem(new RenderScoreBoardSystem(renderer, camera, staticEnvironment));
            engine.addSystem(new LastSystem());
            engine.addSystem(new RenderDebugInfoSystem(renderer, staticEnvironment));
        }

        LayersToRenderModel layersToRenderModel = new LayersToRenderModel(staticEnvironment);
        setVisibleLayers(layersToRenderModel.getVisibleLayerIndices());
        return true;
    }
}
