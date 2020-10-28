package com.sa.game.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class Properties {
    Tree<Tree.Node, String> tree;

    public Tree<Tree.Node, String> getTree() {
        return tree;
    }


    class Node extends Tree.Node<Node, String, TextButton> {
        public Node (final Skin skin, String text) {
            super(new TextButton(text, skin));
            setValue(text);
        }
    }
    Properties(final Skin skin) {
        tree = new Tree(skin);
        tree.setPadding(10);
        tree.setIndentSpacing(25);
        tree.setIconSpacing(5, 0);
        final Node moo1 = new Node(skin, "moo1 (add to moo2)");
        final Node moo2 = new Node(skin, "moo2 (moo3 to bottom)");
        final Node moo3 = new Node(skin, "moo3");
        final Node moo4 = new Node(skin, "moo4");
        final Node moo5 = new Node(skin, "moo5 (remove moo4)");
        tree.add(moo1);
        tree.add(moo2);
        moo2.add(moo3);
        moo3.add(moo4);
        tree.add(moo5);
    }

}
