package com.sa.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
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

public class DesktopControlsSettingsScreen extends ScreenAdapter {
    MyGdxGame game;
    Controller controllerA;
    Controller controllerB;
    Stage stage;
    SelectionLabels selectionLabels;

    public DesktopControlsSettingsScreen(final MyGdxGame game, final Controller controllerA, final Controller controllerB) {
        this.game = game;
        this.controllerA = controllerA;
        this.controllerB = controllerB;

        final Texture logo = new Texture(Gdx.files.internal("settingslogo.png"), true);
        final Image logoImage = new Image(logo);

        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        final Skin skin = new Skin(Gdx.files.internal("skins/myskin/myskin.json"));

        final Table mainTable = new Table();
        mainTable.debug();
        mainTable.setFillParent(true);
        mainTable.top().add(logoImage).row();

        final Table buttonTable = new Table();
        buttonTable.debug();
        buttonTable.add().size(10,20).row();

        selectionLabels = new SelectionLabels(skin, new ISelectionEvent(){
                public void OnSelect(final int selection) {
                if (selection == 0) {
                    //selectionLabels.addSelectionLabel("Controls", buttonTable, "Sub");
                    //selectionLabels.removeSelectionLabel(buttonTable, "Sound");
                    //selectionLabels.removeSelectionLabel(buttonTable, "Back");
                }
                if(selection == 1)
                    ;
                if(selection == 2)
                    game.setScreen(new DesktopSettingsScreen(game, controllerA, controllerB));
                }
            });

        selectionLabels.addSelectionLabel(buttonTable, "Keyboard");
        selectionLabels.addSelectionLabel(buttonTable, "Joystick");
        selectionLabels.addSelectionLabel(buttonTable, "Back");

        mainTable.add(buttonTable).fill();
        stage.addActor(mainTable);
        /*
        mainTable.addListener(new EventListener(){
                @Override
                public boolean handle(Event event) {
                    if(event.getStage().keyDown(Input.Keys.W))
                        System.out.print("");
                    return false;
                }
            });
        */
        final FitViewport fitViewport = new FitViewport(1280f, 720f);
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
    }

    @Override
    public void resize (final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }
}
