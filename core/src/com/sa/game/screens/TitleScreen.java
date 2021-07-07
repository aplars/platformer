package com.sa.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sa.game.DeviceType;
import com.sa.game.MyGdxGame;

public class TitleScreen extends ScreenAdapter{
    MyGdxGame game;
    Controller controllerA;
    Controller controllerB;
    ArrayList<Label> selectorLabels = new ArrayList<>();

    SpriteBatch batch;
    Skin skin;
    Stage stage;
    AssetManager assetManager;
    SelectionLabels selectionLabels;
    //ArrayList<ISelectionEvent> selectionEvents = new ArrayList<>();

    //int selection = -1;

    public TitleScreen(final MyGdxGame game, final Controller controllerA, final Controller controllerB) {
        this.game = game;
        this.controllerA = controllerA;
        this.controllerB = controllerB;

        Texture logo = new Texture(Gdx.files.internal("mainmenulogo.png"), true);
        Texture playGame = new Texture(Gdx.files.internal("playgamebutton.png"), true);
        logo.setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
        playGame.setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        assetManager = new AssetManager();
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        final TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        final Array<TextureAtlas.AtlasRegion> region = atlas.findRegions("start");
        final TextureRegion tRegion = region.get(0);

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("skins/myskin/myskin.json"));
        final BitmapFont font =  new BitmapFont();// skin.getFont("default");
        font.setColor(Color.BLUE);

        //final Label button2 = new Label("Settings", skin, "default-font", Color.WHITE);
        //button2.setScale(1.5f);
        final Image logoImage = new Image(logo);


        final Table mainTable = new Table();
        //mainTable.debug();
        mainTable.setFillParent(true);
        mainTable.top().add(logoImage).row();

        final Table buttonTable = new Table();
        buttonTable.add().size(10,20).row();

        selectionLabels = new SelectionLabels(skin, new ISelectionEvent(){
                public void OnSelect(int selection) {
                    if(selection == 0)
                        game.setScreen(new GameScreen(game, controllerA, controllerB));
                    if(selection == 1)
                        game.setScreen(new DesktopSettingsScreen(game, controllerA, controllerB));
                    if(selection == 2)
                        Gdx.app.exit();
                }
            });

        selectionLabels.addSelectionLabel(buttonTable, "Play");
        selectionLabels.addSelectionLabel(buttonTable, "Settings");
        selectionLabels.addSelectionLabel(buttonTable, "Exit");

        mainTable.add(buttonTable).fill();
        stage.addActor(mainTable);
        //window.pack();
        final FitViewport fitViewport = new FitViewport(1280f, 720f);
        stage.setViewport(fitViewport);

        selectionLabels.setSelection(0);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(this.controllerA != null && this.controllerA.getButton(controllerA.getMapping().buttonStart)) {
            game.setScreen(new GameScreen(game, controllerA, controllerB)); //Set game screen
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
