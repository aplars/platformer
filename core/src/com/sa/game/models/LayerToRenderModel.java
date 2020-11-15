package com.sa.game.models;

public class LayerToRenderModel {
    String name = "";
    int index = -1;
    boolean isVisible = false;

    public LayerToRenderModel(String name, int index, boolean isVisible) {
        this.name = name;
        this.index = index;
        this.isVisible = isVisible;
    }

    public String name() {
        return name;
    }

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public int getIndex() {
		return index;
	}
}
