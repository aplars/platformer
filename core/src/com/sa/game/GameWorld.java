package com.sa.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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
import com.sa.game.systems.CollisionSystem;
import com.sa.game.systems.DamageSystem;
import com.sa.game.systems.DelayControlSystem;
import com.sa.game.systems.DroppedSystem;
import com.sa.game.systems.ExitSystem;
import com.sa.game.systems.explode.ExplodeBoxingGloveOnContactSystem;
import com.sa.game.systems.explode.ExplodeEnemyOnContactSystem;
import com.sa.game.systems.LastSystem;
import com.sa.game.systems.movement.MoveToEntitySystem;
import com.sa.game.systems.OpenDoorSystem;
import com.sa.game.systems.movement.PhysicsSystem;
import com.sa.game.systems.PickUpCoinSystem;
import com.sa.game.systems.PickUpEntitySystem;
import com.sa.game.systems.movement.ResolveCollisionSystem;
import com.sa.game.systems.RespawnPlayer1System;
import com.sa.game.systems.SensorSystem;
import com.sa.game.systems.ThrownSystem;
import com.sa.game.systems.WrapEntitySystem;
import com.sa.game.systems.control.ControlMovementSystem;
import com.sa.game.systems.control.ControlPunchSystem;
import com.sa.game.systems.control.ControlThrowEntitySystem;
import com.sa.game.systems.control.PlayerInputSystem;
import com.sa.game.systems.movement.DampingSystem;
import com.sa.game.systems.movement.MovementSystem;
import com.sa.game.systems.render.RenderDebugInfoSystem;
import com.sa.game.systems.render.RenderParticleSystem;
import com.sa.game.systems.render.RenderScoreBoardSystem;
import com.sa.game.systems.render.RenderScoreSystem;
import com.sa.game.systems.render.RenderStarsSystem;
import com.sa.game.systems.render.RenderSystem;
//testar för att visa kajsa
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
    public int nextLevelPlayer1Score = 0;
    public int nextLevelPlayer1Lives = 3;

    public int player1Score = 0;

    PerformanceCounters performanceCounters;

    public GameWorld(final PerformanceCounters performanceCounters) {
        this.performanceCounters = performanceCounters;
    }

    public void setVisibleLayers(final int layers[]) {
        if (engine == null)
            return;
        visiblelayers = layers;
    }

    public void update(final float dt) {
        if (engine == null)
            return;
        engine.update(dt);
        camera.update();
        fontCamera.update();
    }

    public void render(final float dt) {
        if (engine == null)
            return;
        if(mapRenderer != null)
            mapRenderer.setView(camera);

        if(mapRenderer != null)
            mapRenderer.render(visiblelayers);

        renderer.render(camera, fontCamera);
    }

    public void resize(final float aspectRatio) {
        if (engine == null)
            return;
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

    public boolean loadLevel(String level, int player1Score, int player1Lives, float startDelay) {
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

        engine.addEntityListener(new EntityListener(){
                @Override
                public void entityAdded(Entity entity) {
                }

                @Override
                public void entityRemoved(Entity entity) {
                }

            });


        for(final StaticEnvironment.Entity entity : staticEnvironment.entities) {
            if (entity.name.equals("devo_devil")) {
                engine.addEntity(CreateEnteties.enemy(assetManager,
                                                      startDelay,
                                                      entity.position,
                                                      entity.size.y,
                                                      false,
                                                      staticEnvironment,
                                                      collisionDetection));
            }
            if (entity.name.equals("devodevil")) {
                engine.addEntity(CreateEnteties.enemy(assetManager,
                        startDelay,
                        entity.position,
                        entity.size.y,
                        entity.isFlipped,
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
                engine.addEntity(CreateEnteties.player(assetManager,
                                                       startDelay,
                                                       player1Score,
                                                       player1Lives,
                                                       entity.position,
                                                       entity.size,
                                                       entity.isFlipped,
                                                       staticEnvironment,
                                                       collisionDetection));
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
            engine.addSystem(new ControlMovementSystem(collisionDetection, staticEnvironment));
            engine.addSystem(new ControlPunchSystem(assetManager, collisionDetection, staticEnvironment));
            engine.addSystem(new ControlThrowEntitySystem());
            engine.addSystem(new ThrownSystem());
            engine.addSystem(new DroppedSystem());
            engine.addSystem(new MoveToEntitySystem());
            engine.addSystem(new PhysicsSystem());
            engine.addSystem(new CollisionSystem(performanceCounters.add("collision"), collisionDetection, staticEnvironment));
            engine.addSystem(new SensorSystem(staticEnvironment));
            engine.addSystem(new PickUpEntitySystem(collisionDetection));
            engine.addSystem(new DamageSystem());
            engine.addSystem(new ExitSystem(new ILoadNextLevel() {
                    public void nextLevel(int player1Score, int player1Lives) {
                    loadNextLevel = true;
                    nextLevelPlayer1Score = player1Score;
                    nextLevelPlayer1Lives = player1Lives;
                }
            }));
            engine.addSystem(new ExplodeBoxingGloveOnContactSystem(collisionDetection));
            engine.addSystem(new ExplodeEnemyOnContactSystem(collisionDetection));
            engine.addSystem(new PickUpCoinSystem(collisionDetection));
            engine.addSystem(new ResolveCollisionSystem(performanceCounters.add("resolvecollision")));

            engine.addSystem(new RespawnPlayer1System(assetManager, collisionDetection, staticEnvironment));

            engine.addSystem(new WrapEntitySystem());
            engine.addSystem(new MovementSystem());
            engine.addSystem(new DampingSystem());
            engine.addSystem(new AnimationSystem<>());
            engine.addSystem(new DelayControlSystem());
            engine.addSystem(new RenderSystem(performanceCounters.add("render"), renderer));
            engine.addSystem(new RenderParticleSystem(camera));
            engine.addSystem(new RenderStarsSystem(renderer));
            engine.addSystem(new RenderScoreSystem(renderer));
            engine.addSystem(new RenderScoreBoardSystem(assetManager, renderer, camera, staticEnvironment));
            engine.addSystem(new LastSystem());
            engine.addSystem(new RenderDebugInfoSystem(renderer, staticEnvironment));
        }

        LayersToRenderModel layersToRenderModel = new LayersToRenderModel(staticEnvironment);
        setVisibleLayers(layersToRenderModel.getVisibleLayerIndices());
        return true;
    }
}
