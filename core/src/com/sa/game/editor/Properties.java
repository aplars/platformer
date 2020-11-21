package com.sa.game.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sa.game.models.LayerToRenderModel;
import com.sa.game.models.LayersToRenderModel;

public class Properties {
    Skin skin = new Skin();

    Tree<Tree.Node, String> tree;

    LayersToRenderModel layersToRenderModel;

    public Tree<Tree.Node, String> getTree() {
        return tree;
    }

    class ButtonNode extends Tree.Node<Tree.Node, String, TextButton> {
        public ButtonNode (final Skin skin, String text) {
            super(new TextButton(text, skin));
            setValue(text);
        }
    }

    class CheckBoxNode extends Tree.Node<Tree.Node, String, CheckBox> {
        public CheckBoxNode (final Skin skin, String text) {
            super(new CheckBox(text, skin));
            setValue(text);
        }
    }

    class LabelNode extends Tree.Node<Tree.Node, String, Label> {
        public LabelNode (final Skin skin, String text) {
            super(new Label(text, skin));
            setValue(text);
            getActor().debug();
        }
    }

    public Properties(final Skin skin, LayersToRenderModel layersToRenderModel) {
        this.skin = skin;

        this.tree = new Tree(skin);
        this.tree.setPadding(10);
        this.tree.setIndentSpacing(25);
        this.tree.setIconSpacing(15, 15);

        this.setModel(layersToRenderModel);
        tree.expandAll();
        tree.layout();

        tree.pack();
    }

    public void setModel(LayersToRenderModel layersToRenderModel) {
        this.layersToRenderModel = layersToRenderModel;

        final LabelNode root = new LabelNode(skin, "layers");
        tree.add(root);
        for (final LayerToRenderModel layer : layersToRenderModel) {
            final CheckBoxNode n = new CheckBoxNode(skin, layer.name());
            n.getActor().setChecked(layer.isVisible());
            n.getActor().addListener(new ChangeListener(){
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                    layer.setVisible(n.getActor().isChecked());
                    }
            });

            root.add(n);
        }
        tree.add(root);
    }

}
