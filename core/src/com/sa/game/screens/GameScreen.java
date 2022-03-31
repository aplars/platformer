package com.sa.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.sa.game.GameLevel;
import com.sa.game.MyGdxGame;
import com.sa.game.models.EditorModel;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

public class GameScreen extends ScreenAdapter {
    MyGdxGame game;
    AssetManager assetManager;
    final KeyboardMapping keyboardMapping;
    Controller controllerA;
    ControllerMapping controllerMappingA;
    Controller controllerB;
    ControllerMapping controllerMappingB;

    private final GameLevel gameWorld;
    SpriteBatch batch;
    private final BitmapFont font;
    private final BitmapFont bigFont;
    private final BitmapFont sourcecodepro64Font;

    float dt = 1 / 60f;
    EditorModel editorModel;
    PerformanceCounters performanceCounters = new PerformanceCounters();
    String levels[] = { "level_1.tmx", "level_2.tmx" };
    int levelIndex = 0;

    float timeToShowNextLevelText = 0f;

    float startDelayTime = 4f;

    public GameScreen(final Game game, final AssetManager assetManager, final KeyboardMapping keyboardMapping, final Controller controllerA, final ControllerMapping controllerMappingA, final Controller controllerB, final ControllerMapping controllerMappingB) {
        this.game = (MyGdxGame)game;
        this.assetManager = assetManager;
        this.keyboardMapping = keyboardMapping;
        this.controllerA = controllerA;
        this.controllerMappingA = controllerMappingA;
        this.controllerB = controllerB;
        this.controllerMappingB = controllerMappingB;
        gameWorld = new GameLevel(assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, performanceCounters);
        gameWorld.loadNextLevel = true;
        batch = new SpriteBatch();
        font = new BitmapFont();
        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);
        sourcecodepro64Font = new BitmapFont(Gdx.files.internal("skins/fonts/sourcecodepro64.fnt"),
                                             Gdx.files.internal("skins/fonts/sourcecodepro64.png"), false);
        final ScalingViewport scalingViewport = new ScalingViewport(Scaling.fill, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scalingViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    int currentLevel = 0;
    @Override
    public void render(final float delta) {
        if(gameWorld.playersAreDead) {
            this.game.setScreen(new GameOverScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB));
            //this.game.setScreen(new TitleScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB));
        }
        final long startTime = TimeUtils.millis();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            gameWorld.loadNextLevel = true;
            gameWorld.nextLevelPlayer1Lives = 3;
            gameWorld.nextLevelPlayer1Score = 0;
        }
        gameWorld.update(dt);
        gameWorld.render(dt);

        if(gameWorld.loadNextLevel) {
            currentLevel = levelIndex;
            gameWorld.loadLevel(levels[levelIndex], gameWorld.nextLevelPlayer1Score, gameWorld.nextLevelPlayer1Lives, startDelayTime);
            levelIndex = (levelIndex + 1) % levels.length;

            //gameWorld.resize(getAspectRatio(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
            timeToShowNextLevelText = startDelayTime;
            gameWorld.loadNextLevel = false;
        }
        if(timeToShowNextLevelText > 0) {
            final String txt = "LEVEL: " + (currentLevel+1);
            final GlyphLayout glyphLayout = new GlyphLayout(); 
            glyphLayout.setText(sourcecodepro64Font, txt);
            final float w = glyphLayout.width; //Get width of Total Text for this font size
            final float h = glyphLayout.height; // Get Height of Total Text for this font size

            float t = 1f - (timeToShowNextLevelText/startDelayTime);
            t = Math.min(1f, t * 4f);

            batch.begin();
            final float scale = 1.05f;
            sourcecodepro64Font.getData().setScale(scale, scale);
            sourcecodepro64Font.setColor(0, 0, 0, 1f);
            sourcecodepro64Font.draw(batch, txt, Gdx.graphics.getWidth()/2f - scale*w/2f, Gdx.graphics.getHeight()/2f + scale*h/2f);

            sourcecodepro64Font.getData().setScale(1.0f, 1.0f);
            sourcecodepro64Font.setColor(t, 0, 0, 1f);
            sourcecodepro64Font.draw(batch, txt, 2+Gdx.graphics.getWidth()/2f - w/2f, -2+Gdx.graphics.getHeight()/2f + h/2f);

            sourcecodepro64Font.setColor(t, t, t, 1f);
            sourcecodepro64Font.draw(batch, txt, Gdx.graphics.getWidth()/2f - w/2f, Gdx.graphics.getHeight()/2f + h/2f);
            batch.end();
            timeToShowNextLevelText -= delta;
        }

        final long estimatedTime = TimeUtils.millis() - startTime;
        if(estimatedTime < 16) {
            try {
                Thread.sleep(16-estimatedTime);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }

    @Override
    public void resize(final int width, final int height) {
        gameWorld.resize();
    }
}
