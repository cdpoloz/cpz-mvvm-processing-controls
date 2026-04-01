package com.cpz.processing.controls.layout;

public class LayoutConfig {

    private float normalizedX;
    private float normalizedY;
    private Anchor anchor = Anchor.TOP_LEFT;

    public LayoutConfig(float normalizedX, float normalizedY) {
        this.normalizedX = normalizedX;
        this.normalizedY = normalizedY;
    }

    public float getNormalizedX() {
        return normalizedX;
    }

    public float getNormalizedY() {
        return normalizedY;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor == null ? Anchor.TOP_LEFT : anchor;
    }
}
