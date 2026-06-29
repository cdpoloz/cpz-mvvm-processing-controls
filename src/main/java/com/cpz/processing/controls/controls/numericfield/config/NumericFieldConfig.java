package com.cpz.processing.controls.controls.numericfield.config;

import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;

import java.util.Objects;

/**
 * Minimal config DTO for a single numeric field created from external data.
 *
 * @author CPZ
 */
public final class NumericFieldConfig {
    private final String code;
    private final String text;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;
    private final TooltipConfig tooltip;

    public NumericFieldConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this(code, text, x, y, width, height, enabled, visible, style, null);
    }

    public NumericFieldConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this.code = Objects.requireNonNull(code, "code");
        this.text = text == null ? "" : text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
        private final Integer backgroundColor;
        private final Integer borderColor;
        private final Integer textColor;
        private final Integer cursorColor;
        private final Integer selectionColor;
        private final Integer selectionTextColor;
        private final Float textSize;
        private final String fontPath;
        private final String sourcePath;

        public StyleConfig(
                Integer backgroundColor,
                Integer borderColor,
                Integer textColor,
                Integer cursorColor,
                Integer selectionColor,
                Integer selectionTextColor,
                Float textSize
        ) {
            this(
                    backgroundColor,
                    borderColor,
                    textColor,
                    cursorColor,
                    selectionColor,
                    selectionTextColor,
                    textSize,
                    null,
                    null
            );
        }

        public StyleConfig(
                Integer backgroundColor,
                Integer borderColor,
                Integer textColor,
                Integer cursorColor,
                Integer selectionColor,
                Integer selectionTextColor,
                Float textSize,
                String fontPath,
                String sourcePath
        ) {
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            this.textColor = textColor;
            this.cursorColor = cursorColor;
            this.selectionColor = selectionColor;
            this.selectionTextColor = selectionTextColor;
            this.textSize = textSize;
            this.fontPath = fontPath;
            this.sourcePath = sourcePath;
        }

        public Integer getBackgroundColor() {
            return this.backgroundColor;
        }

        public Integer getBorderColor() {
            return this.borderColor;
        }

        public Integer getTextColor() {
            return this.textColor;
        }

        public Integer getCursorColor() {
            return this.cursorColor;
        }

        public Integer getSelectionColor() {
            return this.selectionColor;
        }

        public Integer getSelectionTextColor() {
            return this.selectionTextColor;
        }

        public Float getTextSize() {
            return this.textSize;
        }

        public String getFontPath() {
            return this.fontPath;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }
    }
}
