package com.cpz.processing.controls.controls.label.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;

import java.util.Objects;

/**
 * Minimal config DTO for a single label created from external data.
 *
 * @author CPZ
 */
public final class LabelConfig {
    private final String code;
    private final String text;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final ControlBounds bounds;
    private final ControlMeasure textSize;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;
    private final TooltipConfig tooltip;

    public LabelConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this(code, text, x, y, width, height, enabled, visible, style, null);
    }

    public LabelConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this(code, text, ControlBounds.absolute(x, y, width, height), null, enabled, visible, style, tooltip);
    }

    public LabelConfig(String code, String text, ControlBounds bounds, ControlMeasure textSize, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this.code = Objects.requireNonNull(code, "code");
        this.text = text == null ? "" : text;
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.x = bounds.x().value();
        this.y = bounds.y().value();
        this.width = bounds.width().value();
        this.height = bounds.height().value();
        this.textSize = textSize;
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
        this.tooltip = tooltip;
    }

    public String getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public ControlBounds getBounds() {
        return this.bounds;
    }

    public ControlMeasure getTextSizeMeasure() {
        return this.textSize;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public StyleConfig getStyle() {
        return this.style;
    }

    public TooltipConfig getTooltip() {
        return this.tooltip;
    }

    public static final class StyleConfig {
        private final Float textSize;
        private final String fontPath;
        private final Integer textColor;
        private final Float lineSpacingMultiplier;
        private final HorizontalAlign alignX;
        private final VerticalAlign alignY;
        private final Integer disabledAlpha;
        private final String sourcePath;

        public StyleConfig(Float textSize, String fontPath, Integer textColor, Float lineSpacingMultiplier, HorizontalAlign alignX, VerticalAlign alignY, Integer disabledAlpha, String sourcePath) {
            this.textSize = textSize;
            this.fontPath = fontPath;
            this.textColor = textColor;
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            this.alignX = alignX;
            this.alignY = alignY;
            this.disabledAlpha = disabledAlpha;
            this.sourcePath = sourcePath;
        }

        public Float getTextSize() {
            return this.textSize;
        }

        public String getFontPath() {
            return this.fontPath;
        }

        public Integer getTextColor() {
            return this.textColor;
        }

        public Float getLineSpacingMultiplier() {
            return this.lineSpacingMultiplier;
        }

        public HorizontalAlign getAlignX() {
            return this.alignX;
        }

        public VerticalAlign getAlignY() {
            return this.alignY;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }
    }
}
