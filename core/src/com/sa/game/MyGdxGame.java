package com.sa.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sa.game.dirwatcher.DirWatcher;
import com.sa.game.editor.Editor;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyGdxGame implements ApplicationListener {
    private GameWorld gameWorld;

    public AtomicBoolean reloadLevel = new AtomicBoolean(true);
    private boolean showEditor = false;
    SpriteBatch batch;
    private BitmapFont font;

    float dt = 1 / 60f;
    Controller controller;

    Editor editor;

    @Override
    public void create() {
        gameWorld = new GameWorld();

        batch = new SpriteBatch();
        font = new BitmapFont();
        com.badlogic.gdx.utils.Array<Controller> theControllers = Controllers.getControllers();

        editor = new Editor();

        for (Controller c : theControllers) {
            System.out.println(c.getName());
        }
        if (theControllers.size > 0)
            controller = theControllers.first();
        /*
         * controller.addListener(new ControllerAdapter() {
         * 
         * @Override public boolean buttonDown (Controller controller, int buttonIndex)
         * { System.out.print("button down "); System.out.println(butonIndex); return
         * true; }
         * 
         * @Override public boolean povMoved (Controller controller, int povIndex,
         * PovDirection value) { System.out.print("pov moved ");
         * System.out.print(povIndex); System.out.print(" "); System.out.println(value);
         * return true; }
         * 
         * @Override public boolean axisMoved (Controller controller, int axisIndex,
         * float value) { System.out.print("axis moved "); System.out.print(axisIndex);
         * System.out.print(" "); System.out.println(value); return true; }
         * 
         * });
         */
    }

    @Override
    public void render() {
        if (reloadLevel.get()) {
            gameWorld.loadLevel();
            gameWorld.resize(getAspectRatio());
            reloadLevel.set(false);
        }
        gameWorld.handleInput(dt, controller);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            reloadLevel.set(true);
        }

        
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            showEditor = ! showEditor;
        }


        gameWorld.preUpdate(dt);
        gameWorld.update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameWorld.render(dt);

        if(showEditor)
            editor.render();
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
        gameWorld.resize(getAspectRatio());
        editor.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static MyGdxGame createDesktop() {
        final MyGdxGame game = new MyGdxGame();

        DirWatcher dirWatcher = new DirWatcher(".") {
            @Override
            protected void onChange(File file, String action) {
                game.reloadLevel.set(true);
                ;
            }
        };

        Timer timer = new Timer();
        timer.schedule(dirWatcher, new Date(), 1000);
        return game;
    }

    private float getAspectRatio() {
        if (Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
            return Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        } else {
            return Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        }
    }
}
