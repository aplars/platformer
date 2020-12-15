package com.sa.game;

import com.badlogic.ashley.core.Engine;
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
import com.sa.game.gfx.Sprites;
import com.sa.game.systems.*;

public class GameWorld {
    //game entities
    StaticEnvironment staticEnvironment = new StaticEnvironment();
    //////////////////////////////////////////////////////////////
    OrthographicCamera camera = new OrthographicCamera();
    Sprites sprites = new Sprites();
    CollisionDetection collisionDetection = new CollisionDetection();
    AssetManager assetManager = new AssetManager();
    OrthogonalTiledMapRenderer mapRenderer;

    int visiblelayers[] = {};

    Engine updateEngine = new Engine();

    PerformanceCounters performanceCounters;

    public GameWorld(PerformanceCounters performanceCounters) {
        this.performanceCounters = performanceCounters;
    }

    public void setVisibleLayers(int layers[]) {
        visiblelayers = layers;
    }

    public void preUpdate(float dt, Controller controller) {
    }

    public void update(float dt) {
        updateEngine.update(dt);
        camera.update();
    }

    public void render(float dt) {
        if(mapRenderer != null)
            mapRenderer.setView(camera);

        if(mapRenderer != null)
            mapRenderer.render(visiblelayers);

        sprites.render(camera);
    }

    public void resize(float aspectRatio) {
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
        TiledMap tiledMap = assetManager.get("level_1.tmx", TiledMap.class);
        staticEnvironment = new StaticEnvironment(tiledMap, collisionDetection);
        assetManager.finishLoading();

        updateEngine = new Engine();
        updateEngine.removeAllEntities();

        for(StaticEnvironment.Entity entity : staticEnvironment.entities) {
            if (entity.name.equals("clown")) {
                updateEngine.addEntity(CreateEnteties.clown(assetManager,
                                                            entity.position,
                                                            entity.size.y,
                                                            staticEnvironment,
                                                            collisionDetection));
            }
            if(entity.name.equals("player")) {
                updateEngine.addEntity(CreateEnteties.player(assetManager,
                                                             entity.position,
                                                             entity.size,
                                                             staticEnvironment,
                                                             collisionDetection));
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(staticEnvironment.getMap());

        assetManager.finishLoading();
        while(!assetManager.isFinished())
            assetManager.update();

        updateEngine.addSystem(new PlayerInputSystem());
        updateEngine.addSystem(new ClownAISystem());
        updateEngine.addSystem(new ControlSystem(assetManager, collisionDetection, staticEnvironment.tileSizeInPixels));
        updateEngine.addSystem(new MoveToEntitySystem());
        updateEngine.addSystem(new PhysicsSystem());
        updateEngine.addSystem(new CollisionSystem(performanceCounters.add("collision"), collisionDetection, staticEnvironment));
        updateEngine.addSystem(new DamageSystem());
        updateEngine.addSystem(new PickUpEntitySystem());
        updateEngine.addSystem(new ExplodeOnContactSystem(collisionDetection));
        updateEngine.addSystem(new ResolveCollisionSystem(performanceCounters.add("resolvecollision")));
        updateEngine.addSystem(new MovementSystem());
        updateEngine.addSystem(new DampingSystem());
        updateEngine.addSystem(new AnimationSystem<>());
        updateEngine.addSystem(new RenderSystem(performanceCounters.add("render"), sprites));

        return true;
    }
}
