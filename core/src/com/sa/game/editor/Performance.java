package com.sa.game.editor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.badlogic.gdx.utils.IntFloatMap.Entry;
import com.sa.game.collision.SweepAndPrune.Pair;

public class Performance {
    Skin skin = new Skin();

    Tree<Tree.Node, String> tree;

    public Tree<Tree.Node, String> getTree() {
        return tree;
    }

    class LabelNode extends Tree.Node<Tree.Node, String, Label> {
        public LabelNode (final Skin skin, String text) {
            super(new Label(text, skin));
            setValue(text);
            getActor().debug();
        }
    }

    class LabelPair extends HorizontalGroup {
        Label label1;
        public LabelPair(String l0, String l1, final Skin skin) {
            super();
            this.addActor(new Label(l0.concat(": "), skin));
            label1 = new Label(l1, skin);
            this.addActor(label1);
        }

        public void setL1(String l1) {
            label1.setText(l1);
        }
    }

    HashMap<PerformanceCounter, LabelPair> modelMap = new HashMap<>();

    class LabelPairNode extends Tree.Node<Tree.Node, String, LabelPair> {
        public LabelPairNode(final Skin skin,  String l0, String l1) {
            super(new LabelPair(l0, l1, skin));
        }

        public void  setL1(String l1) {
            this.getActor().setL1(l1);
        }
    }

    Performance(Skin skin, PerformanceCounters counters) {
        this.skin = skin;

        this.tree = new Tree(skin);
        this.tree.setPadding(10);
        this.tree.setIndentSpacing(25);
        this.tree.setIconSpacing(15, 15);

        final LabelNode root = new LabelNode(skin, "performance");
        tree.add(root);

        for (PerformanceCounter pc : counters.counters) {
            LabelPairNode labelPairNode = new LabelPairNode(skin, pc.name, "______");
            modelMap.put(pc, labelPairNode.getActor());
            root.add(labelPairNode);
        }

        tree.expandAll();
        tree.layout();
        tree.pack();
    }

    public void update() {
        for(java.util.Map.Entry<PerformanceCounter, LabelPair> e : modelMap.entrySet()) {
            e.getValue().setL1(String.format("%.2f", e.getKey().current));
        }
    }
}
