package com.sa.game.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sa.game.StaticEnvironment;
import com.sa.game.StaticEnvironment.LayerId;

public class LayersToRenderModel implements Iterable<LayerToRenderModel> {
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    HashMap <String, LayerToRenderModel> layers = new HashMap<>();

    public LayersToRenderModel(final StaticEnvironment staticEnvironment) {
        final EnumSet<StaticEnvironment.LayerId> theSet = EnumSet.allOf(StaticEnvironment.LayerId.class);
        for(final StaticEnvironment.LayerId layer : theSet) {
            final boolean visibleLayer = layer == LayerId.Visible;
            add(
                staticEnvironment.getLayerName(layer),
                staticEnvironment.getLayerIndex(layer),
                visibleLayer);
        }
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    private void add(final String name, final int index, final boolean isVisible) {
        layers.put(name, new LayerToRenderModel(name, index, isVisible));
        firePropertyChange("layers", null, null);
    }

    public LayerToRenderModel get(final String layerName) {
        return layers.get(layerName);
    }

    public int[] getVisibleLayerIndices() {
        final List<Integer> vis = new ArrayList<>();
        for(final LayerToRenderModel layer : layers.values()) {
            if(layer.isVisible) {
                vis.add(layer.index);
            }
        }
        final int intArr[] = new int[vis.size()];
        for(int i = 0; i < vis.size(); i++) {
            intArr[i] = vis.get(i);
        }
        return intArr;
    }

    public Iterator<LayerToRenderModel> iterator() {
        return layers.values().iterator();
    }
}
