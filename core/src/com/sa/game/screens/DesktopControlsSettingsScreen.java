package com.sa.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sa.game.MyGdxGame;
import com.sa.game.systems.control.KeyboardMapping;

public class DesktopControlsSettingsScreen extends ScreenAdapter {
    MyGdxGame game;
    Controller controllerA;
    Controller controllerB;
    Stage stage;
    SelectionLabels selectionLabels;
    KeyboardConfigurationWindow keyboardConfigurationWindow;
    final KeyboardMapping keyboardMapping;

    public DesktopControlsSettingsScreen(final MyGdxGame game, final AssetManager assetManager, final KeyboardMapping keyboardMapping, final Controller controllerA, final Controller controllerB) {
        this.game = game;
        this.controllerA = controllerA;
        this.controllerB = controllerB;
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

        selectionLabels = new SelectionLabels(skin, stage, keyboardMapping, new ISelectionEvent(){
                public void onSelect(final int selection) {
                if (selection == 0) {
                    keyboardConfigurationWindow = new KeyboardConfigurationWindow(skin, stage, new IWindowCloseEvent(){
                            @Override
                            public void onWindowClose(KeyboardMapping _keyboardMapping) {
                                keyboardConfigurationWindow = null;
                                selectionLabels.setFocus();
                                keyboardMapping.set(_keyboardMapping);
                            }
                        });
                }
                if(selection == 1)
                    ;
                if(selection == 2)
                    game.setScreen(new DesktopSettingsScreen(game, assetManager, keyboardMapping, controllerA, controllerB));
                }
            });

        selectionLabels.addSelectionLabel("Keyboard");
        selectionLabels.addSelectionLabel("Joystick");
        selectionLabels.addSelectionLabel("Back");

        mainTable.add(selectionLabels.getTable()).fill();
        stage.addActor(mainTable);
        final FitViewport fitViewport = new FitViewport(ScreenConstants.ViewportWidth, ScreenConstants.ViewportHeight
                                                        );
        stage.setViewport(fitViewport);

        selectionLabels.setSelection(0);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        selectionLabels.update();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if(keyboardConfigurationWindow != null)
            keyboardConfigurationWindow.render(delta);
    }

    @Override
    public void resize (final int width, final int height) {
        stage.getViewport().update(width, height, true);
        if(keyboardConfigurationWindow != null)
            keyboardConfigurationWindow.resize(width, height);
    }
}
