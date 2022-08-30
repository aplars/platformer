package com.sa.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sa.game.MyGdxGame;
import com.sa.game.models.SoundSettingsModel;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

public class DesktopSettingsScreen extends ScreenAdapter {
    MyGdxGame game;
    Controller controllerA;
    ControllerMapping controllerMappingA;
    Controller controllerB;
    ControllerMapping controllerMappingB;
    KeyboardMapping keyboardMapping;
    Stage stage;
    SelectionLabels selectionLabels;

    SoundSettingsWindow soundSettingsWindow;
    public DesktopSettingsScreen(final MyGdxGame game, final AssetManager assetManager, final KeyboardMapping keyboardMapping, final Controller controllerA, final ControllerMapping controllerMappingA, final Controller controllerB, final ControllerMapping controllerMappingB, final SoundSettingsModel soundSettingsModel) {
        this.game = game;
        this.controllerA = controllerA;
        this.controllerMappingA = controllerMappingA;
        this.controllerB = controllerB;
        this.controllerMappingB = controllerMappingB;
        this.keyboardMapping = keyboardMapping;

        assetManager.load("mainmenulogo.png", Pixmap.class);
        assetManager.finishLoadingAsset("mainmenulogo.png");
        final Texture logo = new Texture(assetManager.get("mainmenulogo.png", Pixmap.class), true);

        final Image logoImage = new Image(logo);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        final Skin skin = new Skin(Gdx.files.internal("skins/myskin/myskin.json"));

        final Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().add(logoImage).row();

        selectionLabels = new SelectionLabels(skin, stage, keyboardMapping, controllerA, controllerMappingA, new ISelectionEvent(){
                public void onSelect(final String selection) {
                    if (selection.equals("Controls")) {
                        game.setScreen(new DesktopControlsSettingsScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, soundSettingsModel));
                    }
                    if (selection.equals("Sound")) {
                        selectionLabels.unFocus();
                        soundSettingsWindow = new SoundSettingsWindow(skin, stage, keyboardMapping, new ISoundConfigurationCloseEvent() {

                                @Override
                                public void onWindowClose(int volume) {
                                    Preferences preferences = Gdx.app.getPreferences(ScreenConstants.PreferencesName);
                                    preferences.putInteger("SoundVolume", volume);
                                    preferences.flush();
                                    soundSettingsModel.soundVolume = volume;
                                    selectionLabels.setFocus();
                                }
                            }, soundSettingsModel);
                    }
                    if(selection.equals("Back"))
                        game.setScreen(new TitleScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, soundSettingsModel));
                }
            });

        selectionLabels.addSelectionLabel("Controls");
        selectionLabels.addSelectionLabel("Sound");
        selectionLabels.addSelectionLabel("Back");

        mainTable.add(selectionLabels.getTable()).fill();
        stage.addActor(mainTable);

        final FitViewport fitViewport = new FitViewport(ScreenConstants.ViewportWidth, ScreenConstants.ViewportHeight);
        stage.setViewport(fitViewport);

        selectionLabels.setSelection(0);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        selectionLabels.update();
        if(soundSettingsWindow != null)
            soundSettingsWindow.update(delta);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize (final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }
}
