package com.sa.game.screens;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sa.game.systems.control.KeyboardMapping;

public class KeyboardConfigurationWindow {
    Stage stage;
    ArrayList<Label> labels = new ArrayList<>();
    ArrayList<Label> values = new ArrayList<>();
    int current = 0;
    HashSet<Integer> usedSlots = new HashSet<>();

    KeyboardConfigurationWindow(final Skin skin, final Stage stage, final IKeyboardConfigurationWindowCloseEvent closeEvent)
    {
        this.stage = stage;
        final Table table = new Table();
        table.setFillParent(true);
        final Window window = new Window("Keyboard configuration", skin);
        window.addListener(new InputListener() {
                KeyboardMapping keyboardMapping = new KeyboardMapping();

                public boolean keyDown(final InputEvent event, final int keycode) {
                    if(keycode == Input.Keys.ESCAPE) {
                        window.setVisible(false);
                        table.remove();
                        closeEvent.onWindowCLose();
                    }

                    if (current < values.size()) {
                        if(usedSlots.add(keycode)==false)
                            return true;
                        labels.get(current).setStyle(skin.get("default", Label.LabelStyle.class));
                        values.get(current).setText(Input.Keys.toString(keycode));
                        switch (current) {
                        case 0:
                            keyboardMapping.Left = keycode;
                            break;
                        case 1:
                            keyboardMapping.Right = keycode;
                            break;
                        case 2:
                            keyboardMapping.A = keycode;
                            break;
                        case 3:
                            keyboardMapping.B = keycode;
                            break;
                        case 4:
                            keyboardMapping.Start = keycode;
                            break;
                        case 5:
                            keyboardMapping.Up = keycode;
                            break;
                        case 6:
                            keyboardMapping.Down = keycode;
                            break;
                        }
                        current++;
                        if (current < values.size()) {
                            labels.get(current).setStyle(skin.get("title", Label.LabelStyle.class));
                        }
                    } else {
                        window.setVisible(false);
                        if (closeEvent != null) {
                            table.remove();
                            Preferences preferences = Gdx.app.getPreferences(ScreenConstants.PreferencesName);
                            preferences.putInteger("KeyLeft", keyboardMapping.Left);
                            preferences.putInteger("KeyRight", keyboardMapping.Right);
                            preferences.putInteger("KeyJump", keyboardMapping.A);
                            preferences.putInteger("KeyFire", keyboardMapping.B);
                            preferences.putInteger("KeyStart", keyboardMapping.Start);
                            preferences.putInteger("KeyUp", keyboardMapping.Up);
                            preferences.putInteger("KeyDown", keyboardMapping.Down);
                            preferences.flush();
                            closeEvent.onWindowClose(keyboardMapping);
                        }
                    }
                    return false;
                }
            });
        window.setModal(true);
        window.setResizable(true);
        table.add(window);
        final Table setKeysTable = new Table();
        labels.add(new Label("Left", skin));
        labels.add(new Label("Right", skin));
        labels.add(new Label("Jump", skin));
        labels.add(new Label("Fire", skin));
        labels.add(new Label("Start", skin));
        labels.add(new Label("Up", skin));
        labels.add(new Label("Down", skin));

        for (final Label label : labels) {
            Label value = new Label("          ", skin);
            values.add(value);
            setKeysTable.add(label).width(ScreenConstants.ViewportWidth/4);
            setKeysTable.add(value).width(ScreenConstants.ViewportWidth/4).row();
        }
        setKeysTable.pack();
        labels.get(current).setStyle(skin.get("title", Label.LabelStyle.class));
        window.add(setKeysTable).left().row();
        table.pack();
        window.pack();
        stage.addActor(table);
        this.stage.setKeyboardFocus(window);
    }

    public void render(final float delta) {
    }

    public void resize (final int width, final int height) {
    }

}
