package com.sa.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.RenderSpriteInFlickeringColorsComponent;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.gfx.Renderer;
import com.sa.game.models.LayersToRenderModel;
import com.sa.game.systems.*;
import com.sa.game.systems.control.ControlMovementSystem;
import com.sa.game.systems.control.ControlPunchSystem;
import com.sa.game.systems.control.ControlThrowEntitySystem;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;
import com.sa.game.systems.control.PlayerInputSystem;
import com.sa.game.systems.explode.ExplodeBoxingGloveOnContactSystem;
import com.sa.game.systems.explode.ExplodeEnemyOnContactSystem;
import com.sa.game.systems.movement.DampingSystem;
import com.sa.game.systems.movement.MoveToEntitySystem;
import com.sa.game.systems.movement.MovementSystem;
import com.sa.game.systems.movement.PhysicsSystem;
import com.sa.game.systems.movement.ResolveCollisionSystem;
import com.sa.game.systems.render.RenderParticleSystem;
import com.sa.game.systems.render.RenderScoreBoardSystem;
import com.sa.game.systems.render.RenderScoreSystem;
import com.sa.game.systems.render.RenderSpriteInFlickeringColorsSystem;
import com.sa.game.systems.render.RenderSpriteInWhiteColorSystem;
import com.sa.game.systems.render.RenderStarsSystem;
import com.sa.game.systems.render.RenderSystem;
import com.sa.game.systems.sound.CoinSoundSystem;
import com.sa.game.systems.sound.PlayerSoundSystem;

public class GameLevel {
    KeyboardMapping keyboardMapping;
    StaticEnvironment staticEnvironment = new StaticEnvironment();
    //////////////////////////////////////////////////////////////
    OrthographicCamera camera = new OrthographicCamera();
    OrthographicCamera fontCamera = new OrthographicCamera();
    Renderer renderer = new Renderer();
    CollisionDetection collisionDetection = new CollisionDetection();
    AssetManager assetManager = null;
    OrthogonalTiledMapRenderer mapRenderer;
    ScalingViewport scalingViewport;
    int visiblelayers[] = {};

    Engine engine = null;

    public boolean loadNextLevel = false;
    public int nextLevelPlayer1Score = 0;
    public int nextLevelPlayer1Lives = 3;

    public boolean playersAreDead = false;
    public int deadPlayer1Score = 0;

    public int player1Score = 0;

    Controller controllerA;
    ControllerMapping controllerMappingA;
    Controller controllerB;
    ControllerMapping controllerMappingB;
    PerformanceCounters performanceCounters;

    public GameLevel(final AssetManager assetManager, final KeyboardMapping keyboardMapping, final Controller controllerA, final ControllerMapping controllerMappingA, final Controller controllerB, final ControllerMapping controllerMappingB, final PerformanceCounters performanceCounters) {
        this.assetManager = assetManager;
        this.keyboardMapping = keyboardMapping;
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        this.performanceCounters = performanceCounters;
        this.controllerA = controllerA;
        this.controllerMappingA = controllerMappingA;
        this.controllerB = controllerB;
        this.controllerMappingB = controllerMappingB;
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

        resize();
        if(mapRenderer != null)
            mapRenderer.setView(camera);

        if(mapRenderer != null)
            mapRenderer.render(visiblelayers);
        renderer.render(camera, fontCamera);
    }

    private float getAspectRatio() {
        if (Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
            return Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        } else {
            return Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        }
    }

    public void resize() {
        final float aspectRatio = getAspectRatio();
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
            camera.viewportHeight = Gdx.graphics.getHeight();
            camera.viewportWidth = Gdx.graphics.getWidth();
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

    public boolean loadLevel(final String level, final int player1Score, final int player1Lives, final float startDelay) {
        collisionDetection.clear();

        //Remove the level-tile-map if its already loaded. This is needed because static environment changes the map.
        //Yes! It would be better if StaticEnvironment cloned the map and then updated it but the map cannot be cloned
        //So its not possible without writing the clone function (and i dont know how to do it)
        if(assetManager.contains(level))
            assetManager.unload(level);
        assetManager.load(level, TiledMap.class);
        assetManager.finishLoading();
        this.staticEnvironment.setTiledLevel(assetManager.get(level, TiledMap.class));

        assetManager.finishLoading();

        boolean addSystems = false;
        if (engine == null) {
            engine = new Engine();
            addSystems = true;
        }
        engine.removeAllEntities();

        engine.addEntityListener(new EntityListener(){
                @Override
                public void entityAdded(final Entity entity) {

                }

                @Override
                public void entityRemoved(final Entity entity) {
                    final CollisionComponent collisionComponent = ComponentMappers.collision.get(entity);
                    if(collisionComponent != null && collisionComponent.entity != null)
                        collisionDetection.remove(collisionComponent.entity);
                }

            });


        for(final StaticEnvironment.Entity entity : staticEnvironment.entities) {
            if (entity.name.equals("devodevil")) {
                engine.addEntity(CreateEnteties.devoDevil(assetManager,
                        startDelay,
                        entity.position,
                        entity.size.y,
                        entity.isFlipped,
                        staticEnvironment,
                        collisionDetection));

            }
            if (entity.name.equals("chichi")) {
                engine.addEntity(CreateEnteties.chiChi(assetManager,
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
                                                       0.0f,
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
            engine.addSystem(new PlayerInputSystem(controllerA, controllerMappingA, this.keyboardMapping));
            engine.addSystem(new AISystem());
            engine.addSystem(new OpenDoorSystem(collisionDetection));
            engine.addSystem(new ControlMovementSystem(collisionDetection, staticEnvironment));
            engine.addSystem(new ControlThrowEntitySystem());
            engine.addSystem(new ControlPunchSystem(assetManager, collisionDetection, staticEnvironment));
            engine.addSystem(new ThrownSystem());
            engine.addSystem(new DroppedSystem());
            engine.addSystem(new MoveToEntitySystem());
            engine.addSystem(new PhysicsSystem());
            engine.addSystem(new PlayerSoundSystem(assetManager));
            engine.addSystem(new CollisionSystem(performanceCounters.add("collision"), collisionDetection, staticEnvironment));
            engine.addSystem(new SensorSystem(staticEnvironment));
            engine.addSystem(new PickUpEntitySystem(collisionDetection));
            engine.addSystem(new DamageSystem(assetManager));
            engine.addSystem(new ExitSystem(new ILoadNextLevel() {
                    @Override
                    public void nextLevel(final int player1Score, final int player1Lives) {
                        loadNextLevel = true;
                        nextLevelPlayer1Score = player1Score;
                        nextLevelPlayer1Lives = player1Lives;
                    }
            }));
            engine.addSystem(new ExplodeBoxingGloveOnContactSystem(collisionDetection));
            engine.addSystem(new ExplodeEnemyOnContactSystem(collisionDetection));
            engine.addSystem(new PickUpCoinSystem(collisionDetection));

            engine.addSystem(new CoinSoundSystem(assetManager));

            engine.addSystem(new ResolveCollisionSystem(performanceCounters.add("resolvecollision")));
            engine.addSystem(new RespawnPlayer1System(assetManager, collisionDetection, staticEnvironment));
            engine.addSystem(new WrapEntitySystem());
            engine.addSystem(new MovementSystem());
            engine.addSystem(new DampingSystem());
            engine.addSystem(new AnimationSystem<>());
            engine.addSystem(new DelayControlSystem());
            engine.addSystem(new HealthSystem());
            engine.addSystem(new RenderSpriteInFlickeringColorsSystem());
            engine.addSystem(new RenderSpriteInWhiteColorSystem());
            engine.addSystem(new RenderSystem(performanceCounters.add("render"), renderer));
            engine.addSystem(new RenderParticleSystem(camera));
            engine.addSystem(new RenderStarsSystem(renderer));
            engine.addSystem(new RenderScoreSystem(renderer));
            engine.addSystem(new RenderScoreBoardSystem(assetManager, renderer, camera, staticEnvironment));
            engine.addSystem(new DeleteEntitySystem());
            engine.addSystem(new LastSystem(new IGotoGameOverScreen(){
                    @Override
                    public void gameOverScreen(final int player1Score) {
                        playersAreDead = true;
                        deadPlayer1Score = player1Score;
                    }
                }));
            //engine.addSystem(new RenderDebugInfoSystem(renderer, staticEnvironment));
        }

        final LayersToRenderModel layersToRenderModel = new LayersToRenderModel(staticEnvironment);
        setVisibleLayers(layersToRenderModel.getVisibleLayerIndices());
        return true;
    }
}
