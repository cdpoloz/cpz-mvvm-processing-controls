package com.cpz.processing.controls.controls.panel.config;

/**
 * JSON-backed optional style values for a panel.
 *
 * <p>Every field is nullable. {@code null} means the property was absent or
 * explicitly {@code null} in JSON and should not override the corresponding
 * {@code PanelStyle} default or theme fallback.</p>
 *
 * @author CPZ
 */
public final class PanelStyleConfig {
    private final Boolean backgroundVisible;
    private final Integer backgroundColor;
    private final Boolean strokeVisible;
    private final Integer strokeColor;
    private final Float strokeWeight;
    private final Float cornerRadius;

    public PanelStyleConfig(
            Boolean backgroundVisible,
            Integer backgroundColor,
            Boolean strokeVisible,
            Integer strokeColor,
            Float strokeWeight,
            Float cornerRadius
    ) {
        this.backgroundVisible = backgroundVisible;
        this.backgroundColor = backgroundColor;
        this.strokeVisible = strokeVisible;
        this.strokeColor = strokeColor;
        this.strokeWeight = strokeWeight;
        this.cornerRadius = cornerRadius;
    }

    public Boolean getBackgroundVisible() {
        return this.backgroundVisible;
    }

    public Integer getBackgroundColor() {
        return this.backgroundColor;
    }

    public Boolean getStrokeVisible() {
        return this.strokeVisible;
    }

    public Integer getStrokeColor() {
        return this.strokeColor;
    }

    public Float getStrokeWeight() {
        return this.strokeWeight;
    }

    public Float getCornerRadius() {
        return this.cornerRadius;
    }
}
