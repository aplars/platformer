package com.sa.game;


import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import com.sa.game.collision.*;
import com.sa.game.dirwatcher.DirWatcher;
import com.sa.game.entities.*;
import com.sa.game.gfx.Sprites;

public class MyGdxGame implements ApplicationListener {
    public AtomicBoolean reloadLevel = new AtomicBoolean(false);
    SpriteBatch batch;
    private BitmapFont font;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer mapRenderer;
    StaticEnvironment staticEnvironment;

    //game entities
    Players players = new Players();
    Enemies enemies = new Enemies();
    PlayerProjectiles playerProjectiles = new PlayerProjectiles();
    PlayerWeapons weapons = new PlayerWeapons();
    //////////////////////////////////////////////////////////////

    //sprites
    Sprites sprites;

    //collision system
    CollisionDetection collisionDetection = new CollisionDetection();

    float dt = 1/60f;
    Controller controller;

    @Override
    public void create () {

        batch = new SpriteBatch();
        font = new BitmapFont();
        com.badlogic.gdx.utils.Array<Controller> theControllers = Controllers.getControllers();

        for (Controller c : theControllers) {
            System.out.println(c.getName());
        }
        if(theControllers.size > 0)
            controller = theControllers.first();
        /*
          controller.addListener(new ControllerAdapter() {
          @Override
          public boolean buttonDown (Controller controller, int buttonIndex) {
          System.out.print("button down ");
          System.out.println(butonIndex);
          return true;
          }
          @Override
          public boolean povMoved (Controller controller, int povIndex, PovDirection value) {
          System.out.print("pov moved ");
          System.out.print(povIndex);
          System.out.print(" ");
          System.out.println(value);
          return true;
          }
          @Override
          public boolean axisMoved (Controller controller, int axisIndex, float value) {
          System.out.print("axis moved ");
          System.out.print(axisIndex);
          System.out.print(" ");
          System.out.println(value);
          return true;
          }

});
        */
        sprites = new Sprites();
        camera = new OrthographicCamera();
        loadLevel();
    }


    @Override
    public void render () {
        if(reloadLevel.get()) {
            loadLevel();
            reloadLevel.set(false);
        }
        players.handleInput(dt, controller);

        players.preUpdate(dt);
        weapons.preUpdate(dt);
        enemies.preUpdate(dt);

        collisionDetection.update(dt);

        players.update(dt, staticEnvironment, collisionDetection, playerProjectiles, weapons, enemies);
        playerProjectiles.update(dt, collisionDetection, staticEnvironment.getWorldBoundY());
        weapons.update(dt, staticEnvironment, collisionDetection);
        enemies.update(dt, staticEnvironment);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batch.begin();
        //batch.draw(img, 0, 0);
        //batch.end();
        mapRenderer.setView(camera);
        int l[] = {staticEnvironment.getLayerIndex(StaticEnvironment.TileId.Visible)};
        mapRenderer.render(l);

        //mapRenderer2.setView(camera);
        //mapRenderer2.render();

        enemies.render(dt, sprites);
        weapons.render(dt, sprites);
        players.render(dt, sprites);
        playerProjectiles.render(dt, camera);

        sprites.render(camera);


        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();	}

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        float w = 0f;
        if(Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
            camera.setToOrtho(false,
                              (staticEnvironment.getWidth()),
                              (staticEnvironment.getWidth())*getAspectRatio());
            w = staticEnvironment.getWidth();
        }
        else {
            camera.setToOrtho(false,
                              (staticEnvironment.getWidth())*getAspectRatio(),
                              (staticEnvironment.getWidth()));
            w = staticEnvironment.getWidth()*getAspectRatio();
        }
        camera.translate(staticEnvironment.getWidth()/2f-w/2f, 0f);
        camera.update();
    }


    @Override
    public void dispose () {
        batch.dispose();
    }

    public static MyGdxGame createDesktop() {
        final MyGdxGame game = new MyGdxGame();

        DirWatcher dirWatcher = new DirWatcher("."){
            @Override
            protected void onChange( File file, String action ) {
                game.reloadLevel.set(true);;
            }

        };

        Timer timer = new Timer();
        timer.schedule( dirWatcher , new Date(), 1000 );
        return game;
    }

    void loadLevel() {
        players.clear();
        enemies.clear();
        staticEnvironment = new StaticEnvironment();
        staticEnvironment.setTiledLevel("level_1.tmx", enemies, collisionDetection);
        players.add(new Player(new Vector2(staticEnvironment.tileSizeInPixels * 4, staticEnvironment.tileSizeInPixels*8), new Vector2(), new Vector2(staticEnvironment.tileSizeInPixels*2, staticEnvironment.tileSizeInPixels*2), staticEnvironment, collisionDetection));
        mapRenderer = new OrthogonalTiledMapRenderer(staticEnvironment.getMap());
    }

    float getAspectRatio() {
        if(Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
            return Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        }
        else {
            return Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
        }
    }
}
