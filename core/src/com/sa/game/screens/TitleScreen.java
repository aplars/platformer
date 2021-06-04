package com.sa.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen extends ScreenAdapter{
    Game game;
    Controller controllerA;
    Controller controllerB;
    private final BitmapFont sourcecodepro64Font;
    SpriteBatch batch;

    public TitleScreen(Game game, Controller controllerA, Controller controllerB) {
        this.game = game;
        this.controllerA = controllerA;
        this.controllerB = controllerB;
        this.sourcecodepro64Font = new BitmapFont(Gdx.files.internal("skins/fonts/sourcecodepro64.fnt"),
                                             Gdx.files.internal("skins/fonts/sourcecodepro64.png"), false);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(this.controllerA != null && this.controllerA.getButton(controllerA.getMapping().buttonStart)) {
            game.setScreen(new GameScreen(game, controllerA, controllerB));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game, controllerA, controllerB));
        }

        final GlyphLayout glyphLayout = new GlyphLayout(); 
        glyphLayout.setText(sourcecodepro64Font, "Title");
        final float w = glyphLayout.width; //Get width of Total Text for this font size
        final float h = glyphLayout.height; // Get Height of Total Text for this font size

        batch.begin();
        final float scale = 1.05f;
        sourcecodepro64Font.getData().setScale(scale, scale);
        sourcecodepro64Font.setColor(1, 1, 0.5f, 1f);
        sourcecodepro64Font.draw(batch, "Title", Gdx.graphics.getWidth()/2f - scale*w/2f, Gdx.graphics.getHeight()/2f + scale*h/2f);
        batch.end();

    }
}
