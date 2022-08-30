package com.sa.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sa.game.DeviceType;
import com.sa.game.MyGdxGame;
import com.sa.game.models.SoundSettingsModel;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

public class TitleScreen extends ScreenAdapter{
    MyGdxGame game;
    final AssetManager assetManager;
    Controller controllerA;
    ControllerMapping controllerMappingA;
    Controller controllerB;
    ControllerMapping controllerMappingB;
    final KeyboardMapping keyboardMapping;
    SoundSettingsModel soundSettingsModel;

    ArrayList<Label> selectorLabels = new ArrayList<>();

    SpriteBatch batch;
    Skin skin;
    Stage stage;
    SelectionLabels selectionLabels;

    public TitleScreen(final MyGdxGame game, final AssetManager assetManager, final KeyboardMapping keyboardMapping, final Controller controllerA, final ControllerMapping controllerMappingA,  final Controller controllerB, final ControllerMapping controllerMappingB, final SoundSettingsModel soundSettingsModel) {
        this.game = game;
        this.assetManager = assetManager;
        this.controllerA = controllerA;
        this.controllerB = controllerB;

        this.keyboardMapping = keyboardMapping;

        this.soundSettingsModel = soundSettingsModel;

        assetManager.load("mainmenulogo.png", Pixmap.class);
        assetManager.finishLoadingAsset("mainmenulogo.png");
        final Texture logo = new Texture(assetManager.get("mainmenulogo.png", Pixmap.class), true);
        logo.setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("skins/myskin/myskin.json"));
        final BitmapFont font =  new BitmapFont();// skin.getFont("default");
        font.setColor(Color.BLUE);

        final Image logoImage = new Image(logo);
        final Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().add(logoImage).row();

        selectionLabels = new SelectionLabels(skin, stage, keyboardMapping, controllerA, controllerMappingA, new ISelectionEvent() {
                public void onSelect(final String selection) {
                    if(selection.equals("Play"))
                    game.setScreen(new GameScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA,
                            controllerB, controllerMappingB, soundSettingsModel));
                    if(selection.equals("Settings"))
                        game.setScreen(new DesktopSettingsScreen(game, assetManager, keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB, soundSettingsModel));
                    if(selection.equals("Exit"))
                        Gdx.app.exit();
                }
            });

        selectionLabels.addSelectionLabel("Play");
        selectionLabels.addSelectionLabel("Settings");
        if(game.deviceType == DeviceType.Develop) {
            selectionLabels.addSelectionLabel("Sprites");
        }
        selectionLabels.addSelectionLabel("Exit");

        mainTable.add(selectionLabels.getTable()).fill();
        stage.addActor(mainTable);
        //window.pack();
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
