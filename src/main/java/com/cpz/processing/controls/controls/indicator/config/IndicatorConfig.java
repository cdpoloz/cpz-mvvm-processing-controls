package com.cpz.processing.controls.controls.indicator.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;
import com.cpz.processing.controls.core.util.ControlCode;
import java.util.Objects;

/**
 * Config DTO for an indicator created from external data.
 *
 * @author CPZ
 */
public final class IndicatorConfig {
    private final String code;
    private final boolean on;
    private final int onColor;
    private final int offColor;
    private final int strokeColor;
    private final float strokeWeight;
    private final ControlBounds bounds;
    private final boolean enabled;
    private final boolean visible;
    private final RendererConfig renderer;
    private final TooltipConfig tooltip;

    public IndicatorConfig(
            String code,
            boolean on,
            int onColor,
            int offColor,
            int strokeColor,
            float strokeWeight,
            ControlBounds bounds,
            boolean enabled,
            boolean visible,
            RendererConfig renderer,
            TooltipConfig tooltip
    ) {
        this.code = ControlCode.requireNonBlank(code);
        this.on = on;
        this.onColor = onColor;
        this.offColor = offColor;
        this.strokeColor = strokeColor;
        this.strokeWeight = Math.max(0.0F, strokeWeight);
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.enabled = enabled;
        this.visible = visible;
        this.renderer = renderer;
        this.tooltip = tooltip;
    }

    public String getCode() {
        return this.code;
    }

    public boolean isOn() {
        return this.on;
    }

    public int getOnColor() {
        return this.onColor;
    }

    public int getOffColor() {
        return this.offColor;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public float getStrokeWeight() {
        return this.strokeWeight;
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

    public RendererConfig getRenderer() {
        return this.renderer;
    }

    public TooltipConfig getTooltip() {
        return this.tooltip;
    }

    public static final class RendererConfig {
        private final String type;
        private final String path;

        public RendererConfig(String type, String path) {
            this.type = Objects.requireNonNull(type, "type");
            this.path = Objects.requireNonNull(path, "path");
        }

        public String getType() {
            return this.type;
        }

        public String getPath() {
            return this.path;
        }
    }
}
