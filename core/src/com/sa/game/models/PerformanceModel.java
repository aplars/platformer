package com.sa.game.models;

import com.badlogic.gdx.utils.PerformanceCounters;

public class PerformanceModel {
    private PerformanceCounters counters = new PerformanceCounters();

    public void add(String name) {
        counters.add(name);
    }

    public void tick(float delta) {
        counters.tick(delta);
    }

}
