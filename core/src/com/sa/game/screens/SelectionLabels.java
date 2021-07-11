package com.sa.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sa.game.systems.control.KeyboardMapping;

public class SelectionLabels {
    ArrayList<ISelectionEvent> selectionEvents = new ArrayList<>();
    ArrayList<Label> selectorLabels = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    int selection = -1;
    Table table;
    Skin skin;
    Stage stage;
    final KeyboardMapping keyboardMapping;

    public SelectionLabels(Skin skin, Stage stage, final KeyboardMapping keyboardMapping, ISelectionEvent selectionEvent) {
        this.skin = skin;
        this.stage = stage;
        this.keyboardMapping = keyboardMapping;
        this.selectionEvents.add(selectionEvent);
        this.table = new Table();
        this.table.add().size(10,20).row();

        this.table.addListener(new InputListener(){
                public boolean keyDown (InputEvent event, int keycode) {
                    if (keycode == keyboardMapping.Up) {
                        setSelection(getSelection() - 1);
                    }
                    if (keycode == keyboardMapping.Down) {
                        setSelection(getSelection() + 1);
                    }
                    if (keycode == keyboardMapping.Jump) {
                        setSelection(getSelection());
                    }
                    return false;
                }

        });
        this.stage.setKeyboardFocus(this.table);
    }

    public void setFocus() {
        this.stage.setKeyboardFocus(table);
    }

    public Table getTable() {
        return this.table;
    }

    public void addSelectionLabel(String text) {
        Label label = new Label(text, skin, "whitetitle");
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
            for (ISelectionEvent evt : selectionEvents) {
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

        for(Label selectionLabel : selectorLabels) {
            selectionLabel.setVisible(false);
        }
        this.selectorLabels.get(ss).setVisible(true);
        selection = ss;
    }

    void update() {
        /*if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            setSelection(getSelection() - 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            setSelection(getSelection() + 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            setSelection(getSelection());
            }*/
    }

}
