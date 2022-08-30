package com.sa.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.sa.game.MyGdxGame;
import com.sa.game.models.SoundSettingsModel;
import com.sa.game.systems.control.KeyboardMapping;

class SoundSettingsWindow extends ControllerAdapter {
    Stage stage;
    Window window;
    Table table;
    Skin skin;
    //int volume;

    Slider slider;

    float timeToWait = 0.15f;

    boolean moveRight = false;
    boolean moveLeft = false;
    SoundSettingsWindow(final Skin skin, final Stage stage, KeyboardMapping keyboardMapping, final ISoundConfigurationCloseEvent closeEvent, final SoundSettingsModel soundSettingsModel) {
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);
        this.skin = skin;

        table = new Table();
        table.setFillParent(true);
        window = new Window("Sound configuration", skin);
        window.addListener(new InputListener() {
                public  boolean keyUp(final InputEvent event, final int keycode) {
                    moveLeft = false;
                    moveRight = false;
                    return true;
                }

                public boolean keyDown (final InputEvent event, final int keycode) {
                    if(keycode == Input.Keys.ESCAPE) {
                        window.setVisible(false);
                        table.remove();
                        closeEvent.onWindowClose((int)slider.getValue());
                    }
                    else if(keycode == keyboardMapping.Left) {
                        //slider.setValue((float)(int)slider.getValue() - 5);
                        moveLeft = true;
                        moveRight = false;
                    }
                    else if(keycode == keyboardMapping.Right) {
                        //slider.setValue((float)(int)slider.getValue() + 5);
                        moveLeft = false;
                        moveRight = true;
                    }
                    return true;
                }

                public boolean keyTyped (final InputEvent event, final int keycode) {
                    return true;

                }
            });

        window.setModal(true);
        window.setResizable(true);
        table.add(window);
        final Table soundTable = new Table();
        Label label = new Label("Volume", skin);
        soundTable.add(label);
        slider = new Slider(0, 100, 5, false, skin);
        slider.setValue(soundSettingsModel.soundVolume);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //volume = (int)(slider.getValue());
            }
        });
        soundTable.add(slider).row();
        window.add(soundTable).left().row();

        table.pack();
        window.pack();
        stage.addActor(table);
        this.stage.setKeyboardFocus(window);

    }

    public void update(float dt) {
        timeToWait-=dt;
        if(timeToWait < 0.0) {
            if(moveLeft)
                slider.setValue((float)(int)slider.getValue() - 5);
            if(moveRight)
                slider.setValue((float)(int)slider.getValue() + 5);

            timeToWait = 0.15f;
        }
    }
}
