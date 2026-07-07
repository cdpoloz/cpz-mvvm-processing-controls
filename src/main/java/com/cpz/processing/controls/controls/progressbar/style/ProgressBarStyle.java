package com.cpz.processing.controls.controls.progressbar.style;

/**
 * Visual style for the non-interactive progress bar facade.
 *
 * @author CPZ
 */
public final class ProgressBarStyle {
    public static final int DEFAULT_TRACK_COLOR = 0xFF30343A;
    public static final int DEFAULT_FILL_COLOR = 0xFF2F80ED;
    public static final int DEFAULT_STROKE_COLOR = 0xFF1F2328;

    private int trackColor = DEFAULT_TRACK_COLOR;
    private int fillColor = DEFAULT_FILL_COLOR;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private float strokeWeight = 1.0F;

    public ProgressBarStyle() {
    }

    public ProgressBarStyle(ProgressBarStyle source) {
        if (source != null) {
            this.trackColor = source.trackColor;
            this.fillColor = source.fillColor;
            this.strokeColor = source.strokeColor;
            this.strokeWeight = source.strokeWeight;
        }
    }

    public int getTrackColor() {
        return this.trackColor;
    }

    public ProgressBarStyle setTrackColor(int color) {
        this.trackColor = color;
        return this;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public ProgressBarStyle setFillColor(int color) {
        this.fillColor = color;
        return this;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public ProgressBarStyle setStrokeColor(int color) {
        this.strokeColor = color;
        return this;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public ProgressBarStyle setStrokeWeight(float weight) {
        this.strokeWeight = Math.max(0.0F, weight);
        return this;
    }
}
