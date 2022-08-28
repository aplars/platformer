package com.sa.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.sa.game.MyGdxGame;

class SoundSettingsScreen extends ControllerAdapter {
    Stage stage;
    Window window;
    Table table;
    Skin skin;
    int volume;
    SoundSettingsScreen(final Skin skin, final Stage stage, final ISoundConfigurationCloseEvent closeEvent) {
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);
        this.skin = skin;

        table = new Table();
        table.setFillParent(true);
        window = new Window("Sound configuration", skin);
        window.addListener(new InputListener() {
                public boolean keyDown (final InputEvent event, final int keycode) {
                    if(keycode == Input.Keys.ESCAPE) {
                        window.setVisible(false);
                        table.remove();
                        closeEvent.onWindowClose(volume);
                    }
                    return true;
                }
            });

        window.setModal(true);
        window.setResizable(true);
        table.add(window);
        final Table soundTable = new Table();
        Label label = new Label("Volume", skin);
        soundTable.add(label);
        Slider slider = new Slider(0, 100, 5, false, skin);
        soundTable.add(slider).row();
        window.add(soundTable).left().row();

        table.pack();
        window.pack();
        stage.addActor(table);
        this.stage.setKeyboardFocus(window);

    }
}
