package com.cpz.processing.controls.controls.indicator.style;

/**
 * Visual style for the non-interactive indicator facade.
 *
 * @author CPZ
 */
public final class IndicatorStyle {
    public static final int DEFAULT_ON_COLOR = 0xFF2ECC71;
    public static final int DEFAULT_OFF_COLOR = 0xFF30343A;
    public static final int DEFAULT_STROKE_COLOR = 0xFF1F2328;

    private int onColor = DEFAULT_ON_COLOR;
    private int offColor = DEFAULT_OFF_COLOR;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private float strokeWeight = 1.0F;
    private String rendererType;
    private String rendererPath;

    public IndicatorStyle() {
    }

    public IndicatorStyle(IndicatorStyle source) {
        if (source != null) {
            this.onColor = source.onColor;
            this.offColor = source.offColor;
            this.strokeColor = source.strokeColor;
            this.strokeWeight = source.strokeWeight;
            this.rendererType = source.rendererType;
            this.rendererPath = source.rendererPath;
        }
    }

    public int getOnColor() {
        return this.onColor;
    }

    public IndicatorStyle setOnColor(int color) {
        this.onColor = color;
        return this;
    }

    public int getOffColor() {
        return this.offColor;
    }

    public IndicatorStyle setOffColor(int color) {
        this.offColor = color;
        return this;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public IndicatorStyle setStrokeColor(int color) {
        this.strokeColor = color;
        return this;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public IndicatorStyle setStrokeWeight(float weight) {
        this.strokeWeight = Math.max(0.0F, weight);
        return this;
    }

    public String getRendererType() {
        return this.rendererType;
    }

    public String getRendererPath() {
        return this.rendererPath;
    }

    public IndicatorStyle setRenderer(String type, String path) {
        this.rendererType = type;
        this.rendererPath = path;
        return this;
    }

    public boolean isSvgRenderer() {
        return "svg".equals(this.rendererType);
    }
}
