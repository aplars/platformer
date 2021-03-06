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

    public AtomicBoolean reloadLevel = new AtomicBoolean(true);
    private boolean showEditor = false;
    SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont bigFont;

    float dt = 1 / 60f;
    Controller controller;

    Editor editor;
    EditorModel editorModel;
    PerformanceCounters performanceCounters = new PerformanceCounters();

    @Override
    public void create() {
        gameWorld = new GameWorld(performanceCounters);

        batch = new SpriteBatch();
        font = new BitmapFont();

        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);

        com.badlogic.gdx.utils.Array<Controller> theControllers = Controllers.getControllers();

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
        long startTime = TimeUtils.millis();

        if (reloadLevel.get()) {
            gameWorld.loadLevel();
            gameWorld.resize(getAspectRatio());

            editorModel = new EditorModel(gameWorld.staticEnvironment);
            editor = new Editor(editorModel, performanceCounters);

            reloadLevel.set(false);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            reloadLevel.set(true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            showEditor = ! showEditor;
        }

        gameWorld.setVisibleLayers(editorModel.getLayersToRenderModel().getVisibleLayerIndices());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameWorld.preUpdate(dt, controller);
        gameWorld.update(dt);
        int numSprites = gameWorld.renderer.numberOfSprites();
        gameWorld.render(dt);

        if(showEditor) {
            editor.render(numSprites);
        }

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
        if(editor != null)
            editor.resize(width, height);
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
