package com.cpz.processing.controls.controls.label.config;

import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;

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
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public LabelConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this.code = Objects.requireNonNull(code, "code");
        this.text = text == null ? "" : text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
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

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public StyleConfig getStyle() {
        return this.style;
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
