package com.sa.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

public class SelectionLabels {
    final ArrayList<ISelectionEvent> selectionEvents = new ArrayList<>();
    final ArrayList<Label> selectorLabels = new ArrayList<>();
    final ArrayList<Label> labels = new ArrayList<>();
    int selection = -1;
    final Table table;
    final Skin skin;
    final Stage stage;
    final KeyboardMapping keyboardMapping;
    Controller controller;
    final ControllerMapping controllerMapping;
    boolean lastUp = true;
    boolean lastDown = true;
    boolean lastJump = true;

    boolean hasFocus = true;

    public SelectionLabels(final Skin skin, final Stage stage, final KeyboardMapping keyboardMapping, final Controller controller, final ControllerMapping controllerMapping, final ISelectionEvent selectionEvent) {
        this.skin = skin;
        this.stage = stage;
        this.keyboardMapping = keyboardMapping;
        this.controller = controller;
        this.controllerMapping = controllerMapping;
        this.selectionEvents.add(selectionEvent);
        this.table = new Table();
        this.table.add().size(10,20).row();

        this.table.addListener(new InputListener(){
                public boolean keyDown (final InputEvent event, final int keycode) {
                    if (keycode == keyboardMapping.Up) {
                        setSelection(getSelection() - 1);
                    }
                    if (keycode == keyboardMapping.Down) {
                        setSelection(getSelection() + 1);
                    }
                    if (keycode == keyboardMapping.A) {
                        setSelection(getSelection());
                    }
                    return false;
                }

        });
        this.stage.setKeyboardFocus(this.table);
    }

    public void setFocus() {
        this.stage.setKeyboardFocus(table);
        hasFocus = true;
    }

    public void unFocus() {
        hasFocus = false;
    }

    public Table getTable() {
        return this.table;
    }

    public void addSelectionLabel(final String text) {
        final Label label = new Label(text, skin, "whitetitle");
        final int index = selectorLabels.size();
        label.addListener(new InputListener() {
                @Override
                public boolean touchDown (final InputEvent event, final float x, final float y, final int pointer, final int button) {
                    setSelection(index);
                    return true;
                }
                });
        this.selectorLabels.add(new Label("*", skin, "whitetitle"));
        this.labels.add(label);
        this.table.add(selectorLabels.get(selectorLabels.size()-1));
        this.table.center().add(label).center();//width(600).height(600);
        this.table.row();
        this.table.pack();
    }

    int getSelection() {
        return this.selection;
    }

    void setSelection(final int s) {
        if (selection == s) {
            for (final ISelectionEvent evt : selectionEvents) {
                evt.onSelect(s);
            }
        }

        int ss = 0;
        if(s < 0) {
            ss = selectorLabels.size() - 1;
        }
        else if(s >= selectorLabels.size())
            ss = 0;
        else
            ss = Math.abs((s) % selectorLabels.size());

        for(final Label selectionLabel : selectorLabels) {
            selectionLabel.setVisible(false);
        }
        this.selectorLabels.get(ss).setVisible(true);
        selection = ss;
    }

    void update() {
        if (this.controller != null && hasFocus) {
            if (this.controller.getButton(controllerMapping.Up) && lastUp == false) {
                setSelection(selection - 1);
            }
            else if (this.controller.getButton(controllerMapping.Down) && lastDown == false) {
                setSelection(selection + 1);
            }
            else if (this.controller.getButton(controllerMapping.A) && lastJump == false) {
                setSelection(selection);
            }
            lastUp = this.controller.getButton(controllerMapping.Up);
            lastDown = this.controller.getButton(controllerMapping.Down);
            lastJump = this.controller.getButton(controllerMapping.A);
        }
    }

}
