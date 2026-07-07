package com.cpz.processing.controls.controls.progressbar.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.progressbar.ProgressBarFillDirection;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;
import java.util.Objects;

/**
 * Config DTO for a progress bar created from external data.
 *
 * @author CPZ
 */
public final class ProgressBarConfig {
    private final String code;
    private final float min;
    private final float max;
    private final float value;
    private final int trackColor;
    private final int fillColor;
    private final int strokeColor;
    private final float strokeWeight;
    private final ProgressBarFillDirection fillDirection;
    private final ControlBounds bounds;
    private final boolean enabled;
    private final boolean visible;
    private final TooltipConfig tooltip;

    public ProgressBarConfig(
            String code,
            float min,
            float max,
            float value,
            int trackColor,
            int fillColor,
            int strokeColor,
            float strokeWeight,
            ProgressBarFillDirection fillDirection,
            ControlBounds bounds,
            boolean enabled,
            boolean visible,
            TooltipConfig tooltip
    ) {
        this.code = Objects.requireNonNull(code, "code");
        if (min <= max) {
            this.min = min;
            this.max = max;
        } else {
            this.min = max;
            this.max = min;
        }
        this.value = Math.max(this.min, Math.min(this.max, value));
        this.trackColor = trackColor;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWeight = Math.max(0.0F, strokeWeight);
        this.fillDirection = fillDirection == null ? ProgressBarFillDirection.LEFT_TO_RIGHT : fillDirection;
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.enabled = enabled;
        this.visible = visible;
        this.tooltip = tooltip;
    }

    public String getCode() {
        return this.code;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getValue() {
        return this.value;
    }

    public int getTrackColor() {
        return this.trackColor;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
    }

    public ProgressBarFillDirection getFillDirection() {
        return this.fillDirection;
    }

    public ControlBounds getBounds() {
        return this.bounds;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public TooltipConfig getTooltip() {
        return this.tooltip;
    }
}
