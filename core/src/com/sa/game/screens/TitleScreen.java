package com.sa.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TitleScreen extends ScreenAdapter{
    Game game;
    Controller controllerA;
    Controller controllerB;
    private final BitmapFont sourcecodepro64Font;
    SpriteBatch batch;
    Skin skin;
    Stage stage;
    AssetManager assetManager;
    public TitleScreen(final Game game, final Controller controllerA, final Controller controllerB) {
        this.game = game;
        this.controllerA = controllerA;
        this.controllerB = controllerB;
        this.sourcecodepro64Font = new BitmapFont(Gdx.files.internal("skins/fonts/sourcecodepro64.fnt"),
                                             Gdx.files.internal("skins/fonts/sourcecodepro64.png"), false);
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        assetManager = new AssetManager();
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        Array<TextureAtlas.AtlasRegion> region = atlas.findRegions("start");
        TextureRegion tRegion = region.get(0);

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("skins/gdx-holo/skin/uiskin.json"));


        // Create a table that fills the screen. Everything else will go inside this table.
        Window window = new Window("preferences", skin) {

            };
        window.setResizable(true);
        window.setResizeBorder(10);

        ImageButton startGameButton = new ImageButton(new TextureRegionDrawable(tRegion));
        //startGameButton.getLabel().setScale(2);
        startGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game, controllerA, controllerB));
                return true;
            }
        });
        //button1.setSize(500, 500);
        TextButton button2 = new TextButton("Settings", skin);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.setFillParent(true);
        horizontalGroup.center().expand().addActor(startGameButton);
        horizontalGroup.center().addActor(button2);

        Table table = new Table();
        table.debug();
        table.setFillParent(true);
        table.add(startGameButton).fill();//width(600).height(600);
        table.add(button2).fill();//width(600).height(600);
        //horizontalGroup.addActor(button1);
        //horizontalGroup.addActor(button2);
        //table.setFillParent(true);
        //stage.addActor(window);
        stage.addActor(table);
        window.pack();
        FitViewport fitViewport = new FitViewport(1280f, 720f);
        stage.setViewport(fitViewport);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(this.controllerA != null && this.controllerA.getButton(controllerA.getMapping().buttonStart)) {
            game.setScreen(new GameScreen(game, controllerA, controllerB)); //Set game screen
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game, controllerA, controllerB)); //Set game screen
        }
        /*
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
        */
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize (final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

}
