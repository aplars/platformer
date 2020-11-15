package com.sa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.entities.CreateEnteties;
import com.sa.game.entities.Enemies;
import com.sa.game.entities.PickedUpEntities;
import com.sa.game.entities.PlayerStunProjectiles;
import com.sa.game.entities.Players;
import com.sa.game.gfx.Sprites;
import com.sa.game.models.EditorModel;

public class GameWorld {
    //game entities
    StaticEnvironment staticEnvironment = new StaticEnvironment();
    Players players = new Players();
    Enemies enemies = new Enemies();
    PlayerStunProjectiles playerStunProjectiles = new PlayerStunProjectiles();
    PickedUpEntities pickedUpEntities = new PickedUpEntities();
    //////////////////////////////////////////////////////////////
    OrthographicCamera camera = new OrthographicCamera();
    Sprites sprites = new Sprites();
    CollisionDetection collisionDetection = new CollisionDetection();
    AssetManager assetManager = new AssetManager();
    OrthogonalTiledMapRenderer mapRenderer;

    int visiblelayers[] = {};
    public void setVisibleLayers(int layers[]) {
        visiblelayers = layers;
    }

    public void preUpdate(float dt, Controller controller) {
        players.handleInput(dt, controller);
        players.preUpdate(dt);
        pickedUpEntities.preUpdate(dt);
        enemies.preUpdate(dt);
    }

    public void update(float dt) {
        collisionDetection.update(dt, staticEnvironment);
        players.update(dt, assetManager, staticEnvironment.getWorldBoundY(), staticEnvironment.tileSizeInPixels,
                       collisionDetection, playerStunProjectiles, pickedUpEntities, enemies);
        playerStunProjectiles.update(dt, collisionDetection, staticEnvironment.getWorldBoundY());
        pickedUpEntities.update(dt, staticEnvironment.getWorldBoundY(), collisionDetection);
        enemies.update(dt, staticEnvironment, collisionDetection);
        camera.update();
    }

    public void render(float dt) {
        if(mapRenderer != null)
            mapRenderer.setView(camera);

        if(mapRenderer != null)
            mapRenderer.render(visiblelayers);
        enemies.render(dt, sprites);
        pickedUpEntities.render(dt, sprites);
        players.render(dt, sprites);
        playerStunProjectiles.render(dt, camera);
        sprites.render(camera);
    }

    public void resize(float aspectRatio) {
        if(staticEnvironment == null) return;
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
        playerStunProjectiles.dispose();
        players.clear();
        enemies.clear();
        pickedUpEntities.clear();
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
        for(StaticEnvironment.Entity entity : staticEnvironment.entities) {
            if (entity.name.equals("clown")) {
                enemies.add(
                            CreateEnteties.clown(
                                                 assetManager,
                                                 entity.position,
                                                 entity.size.y,
                                                 staticEnvironment,
                                                 collisionDetection));
            }
            if(entity.name.equals("player")) {
                players.add(
                            CreateEnteties.player(assetManager,
                                                  entity.position,
                                                  entity.size,
                                                  staticEnvironment,
                                                  collisionDetection)
                );
            }
        }

        mapRenderer = new OrthogonalTiledMapRenderer(staticEnvironment.getMap());

        assetManager.finishLoading();
        while(!assetManager.isFinished())
            assetManager.update();
        return true;
    }
}
