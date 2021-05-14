package com.sa.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.badlogic.gdx.utils.TimeUtils;
import com.sa.game.GameWorld;
import com.sa.game.models.EditorModel;

public class GameScreen extends ScreenAdapter{
    private GameWorld gameWorld;
    SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont bigFont;
    private BitmapFont sourcecodepro64Font;

    float dt = 1 / 60f;
    EditorModel editorModel;
    PerformanceCounters performanceCounters = new PerformanceCounters();
    String levels[] = { "level_1.tmx", "level_2.tmx" };
    int levelIndex = 0;

    float timeToShowNextLevelText = 0f;

    float startDelayTime = 4f;
    public GameScreen() {
        gameWorld = new GameWorld(performanceCounters);
        gameWorld.loadNextLevel = true;
        batch = new SpriteBatch();
        font = new BitmapFont();

        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);
        sourcecodepro64Font = new BitmapFont(Gdx.files.internal("skins/fonts/sourcecodepro64.fnt"),
                                             Gdx.files.internal("skins/fonts/sourcecodepro64.png"), false);
    }

    int currentLevel = 0;
    @Override
    public void render(final float delta) {
        long startTime = TimeUtils.millis();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            gameWorld.loadNextLevel = true;
        }
        gameWorld.update(dt);
        gameWorld.render(dt);

        if(gameWorld.loadNextLevel) {
            currentLevel = levelIndex;
            gameWorld.loadLevel(levels[levelIndex], gameWorld.nextLevelPlayer1Score, startDelayTime);
            levelIndex = (levelIndex + 1) % levels.length;
            gameWorld.resize(getAspectRatio());
            timeToShowNextLevelText = startDelayTime;
            gameWorld.loadNextLevel = false;
        }

        if(timeToShowNextLevelText > 0) {
            String txt = "LEVEL: " + (currentLevel+1);
            GlyphLayout glyphLayout = new GlyphLayout(); 
            glyphLayout.setText(sourcecodepro64Font, txt);
            float w = glyphLayout.width; //Get width of Total Text for this font size
            float h = glyphLayout.height; // Get Height of Total Text for this font size

            float t = 1f - (timeToShowNextLevelText/startDelayTime);
            t = Math.min(1f, t * 4f);

            batch.begin();
            float scale = 1.05f;
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

    private float getAspectRatio() {
        if (Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
            return Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        } else {
            return Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        }
    }

    @Override
    public void resize(final int width, final int height) {
        gameWorld.resize(getAspectRatio());
    }
}
