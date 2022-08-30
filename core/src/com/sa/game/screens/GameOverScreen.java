package com.sa.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sa.game.MyGdxGame;
import com.sa.game.models.SoundSettingsModel;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

public class GameOverScreen extends ScreenAdapter {
    MyGdxGame game;
    final AssetManager assetManager;
    Controller controllerA;
    ControllerMapping controllerMappingA;
    Controller controllerB;
    final ControllerMapping controllerMappingB;
    final KeyboardMapping keyboardMapping;
    final SoundSettingsModel soundSettingsModel;

    Skin skin;
    Stage stage;
    SelectionLabels selectionLabels;

    public GameOverScreen(final MyGdxGame game, final AssetManager assetManager, final KeyboardMapping keyboardMapping, final Controller controllerA, final ControllerMapping controllerMappingA,  final Controller controllerB, final ControllerMapping controllerMappingB, final SoundSettingsModel soundSettingsModel) {
        this.game = game;
        this.assetManager = assetManager;
        this.keyboardMapping = keyboardMapping;
        this.controllerA = controllerA;
        this.controllerMappingA = controllerMappingA;
        this.controllerB = controllerB;
        this.controllerMappingB = controllerMappingB;
        this.soundSettingsModel = soundSettingsModel;
        
        assetManager.load("gameover.png", Pixmap.class);
        assetManager.finishLoadingAsset("gameover.png");
        final Texture logo = new Texture(assetManager.get("gameover.png", Pixmap.class), true);
        logo.setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/myskin/myskin.json"));
        final BitmapFont font =  new BitmapFont();// skin.getFont("default");

        final Image logoImage = new Image(logo);
        final Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().add(logoImage).row();
        selectionLabels = new SelectionLabels(skin, stage, keyboardMapping, controllerA, controllerMappingA, new ISelectionEvent() {
                public void onSelect(final String selection) {
                    if(selection.equals("Continue"))
                        game.setScreen(new GameScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, soundSettingsModel));
                    if(selection.equals("End")) {
                        game.setScreen(new TitleScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, soundSettingsModel));
                    }
                }
        });
        selectionLabels.addSelectionLabel("Continue");
        selectionLabels.addSelectionLabel("End");

        mainTable.add(selectionLabels.getTable()).fill();
        stage.addActor(mainTable);

        final FitViewport fitViewport = new FitViewport(ScreenConstants.ViewportWidth, ScreenConstants.ViewportHeight);
        stage.setViewport(fitViewport);

        selectionLabels.setSelection(0);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(this.controllerA != null && this.controllerA.getButton(controllerA.getMapping().buttonStart)) {
            game.setScreen(new GameScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, soundSettingsModel)); //Set game screen
        }
        selectionLabels.update();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize (final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }
}
