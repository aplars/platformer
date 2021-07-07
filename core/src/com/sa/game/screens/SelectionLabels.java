package com.sa.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SelectionLabels {
    ArrayList<ISelectionEvent> selectionEvents = new ArrayList<>();
    ArrayList<Label> selectorLabels = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    int selection = -1;
    Skin skin;

    public SelectionLabels(Skin skin, ISelectionEvent selectionEvent) {
        this.skin = skin;
        this.selectionEvents.add(selectionEvent);
    }

    public void addSelectionLabel(Table table, String text) {
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
        table.add(selectorLabels.get(selectorLabels.size()-1));
        table.center().add(label).center();//width(600).height(600);
        table.row();
        table.pack();
    }

    int getSelection() {
        return this.selection;
    }

    void setSelection(final int s) {
        if (selection == s) {
            for (ISelectionEvent evt : selectionEvents) {
                evt.OnSelect(s);
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            setSelection(getSelection() - 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            setSelection(getSelection() + 1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            setSelection(getSelection());
        }
    }
}
