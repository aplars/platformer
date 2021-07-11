package com.sa.game.screens;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.sa.game.systems.control.ControllerMapping;

public class JoystickConfigurationWindow extends ControllerAdapter {
    final Stage stage;
    final Skin skin;
    final Controller controller;
    final IWindowCloseEvent closeEvent;
    final Table table;
    ArrayList<Label> labels = new ArrayList<>();
    ArrayList<Label> values = new ArrayList<>();
    final Window window;
    int current = 0;
    final ControllerMapping controllerMapping;
    HashSet<Integer> usedSlots = new HashSet<>();

    public JoystickConfigurationWindow(final Skin skin, final Stage stage, final Controller controller,
                                       final IWindowCloseEvent closeEvent) {
        this.stage = stage;
        this.skin = skin;
        this.controller = controller;
        this.closeEvent = closeEvent;
        controllerMapping = new ControllerMapping(controller);
        this.table = new Table();

        table.setFillParent(true);
        window = new Window("Joystick configuration", skin);
        window.addListener(new InputListener() {
                public boolean keyDown (final InputEvent event, final int keycode) {
                    if(keycode == Input.Keys.ESCAPE) {
                        window.setVisible(false);
                        table.remove();
                        closeEvent.onWindowCLose();
                        dispose();
                    }
                    return true;
                }
            });

        controller.addListener(this);
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
            setKeysTable.add(label).width(ScreenConstants.ViewportWidth / 4);
            setKeysTable.add(value).width(ScreenConstants.ViewportWidth / 4).row();
        }
        setKeysTable.pack();
        labels.get(current).setStyle(skin.get("title", Label.LabelStyle.class));
        window.add(setKeysTable).left().row();
        table.pack();
        window.pack();
        stage.addActor(table);
        this.stage.setKeyboardFocus(window);

    }

    public void dispose() {
        controller.removeListener(this);
    }

    public boolean buttonDown(Controller controller, int buttonIndex) {
        if (current < values.size()) {
            if(usedSlots.add(buttonIndex)==false)
                return true;
            labels.get(current).setStyle(skin.get("default", Label.LabelStyle.class));
            values.get(current).setText(String.valueOf(buttonIndex));
            switch (current) {
                case 0:
                    controllerMapping.Left = buttonIndex;
                    break;
                case 1:
                    controllerMapping.Right = buttonIndex;
                    break;
                case 2:
                    controllerMapping.A = buttonIndex;
                    break;
                case 3:
                    controllerMapping.B = buttonIndex;
                    break;
                case 4:
                    controllerMapping.Start = buttonIndex;
                    break;
                case 5:
                    controllerMapping.Up = buttonIndex;
                    break;
                case 6:
                    controllerMapping.Down = buttonIndex;
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
                preferences.putInteger("ControllerALeft", controllerMapping.Left);
                preferences.putInteger("ControllerARight", controllerMapping.Right);
                preferences.putInteger("ControllerAA", controllerMapping.A);
                preferences.putInteger("ControllerAB", controllerMapping.B);
                preferences.putInteger("ControllerAStart", controllerMapping.Start);
                preferences.putInteger("ControllerAUp", controllerMapping.Up);
                preferences.putInteger("ControllerADown", controllerMapping.Down);
                preferences.flush();
                closeEvent.onWindowClose(controllerMapping);
            }
        }
        return true;
    }
}
