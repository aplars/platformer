package com.sa.game;

import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.badlogic.gdx.utils.TimeUtils;
import com.sa.game.editor.Editor;
import com.sa.game.models.EditorModel;

public class MyGdxGame implements ApplicationListener {
    private GameWorld gameWorld;
    SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont bigFont;
    float dt = 1 / 60f;
    EditorModel editorModel;
    PerformanceCounters performanceCounters = new PerformanceCounters();
    String levels[] = { "level_1.tmx", "level_2.tmx" };
    int levelIndex = 0;

    @Override
    public void create() {
        gameWorld = new GameWorld(performanceCounters);
        gameWorld.loadNextLevel = true;
        batch = new SpriteBatch();
        font = new BitmapFont();

        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);
    }


    @Override
    public void render() {
        long startTime = TimeUtils.millis();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameWorld.loadNextLevel) {
            gameWorld.loadLevel(levels[levelIndex]);
            levelIndex = (levelIndex + 1) % levels.length;
            gameWorld.resize(getAspectRatio());
            gameWorld.loadNextLevel = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            gameWorld.loadNextLevel = true;
        }

        gameWorld.update(dt);
        gameWorld.render(dt);

        long estimatedTime = TimeUtils.millis() - startTime;
        if(estimatedTime < 16) {
            try {
                Thread.sleep(16-estimatedTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static MyGdxGame createDesktop() {
        final MyGdxGame game = new MyGdxGame();
        /*
        DirWatcher dirWatcher = new DirWatcher(".") {
            @Override
            protected void onChange(File file, String action) {
                game.reloadLevel.set(true);
                ;
            }
            };*/

        //Timer timer = new Timer();
        //timer.schedule(dirWatcher, new Date(), 1000);
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
