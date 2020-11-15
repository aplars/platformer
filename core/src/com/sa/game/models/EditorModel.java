package com.sa.game.models;

import java.util.ArrayList;

import com.sa.game.StaticEnvironment;

public class EditorModel
{
    final LayersToRenderModel layersToRenderModel;

    public EditorModel(StaticEnvironment staticEnvironment) {
        layersToRenderModel = new LayersToRenderModel(staticEnvironment);
    }

    public LayersToRenderModel getLayersToRenderModel() {
        return layersToRenderModel;
    }
}
