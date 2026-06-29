package com.cpz.processing.controls.core.overlay.tooltip.config;

/**
 * JSON-facing tooltip configuration.
 *
 * @author CPZ
 */
public final class TooltipConfig {
    private final String text;
    private final boolean enabled;
    private final StyleConfig style;

    public TooltipConfig(String text, boolean enabled, StyleConfig style) {
        this.text = text == null ? "" : text;
        this.enabled = enabled;
        this.style = style;
    }

    public String getText() {
        return this.text;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public StyleConfig getStyle() {
        return this.style;
    }

    public static final class StyleConfig {
        private final Integer backgroundColor;
        private final Integer textColor;
        private final Integer borderColor;
        private final Float textPadding;
        private final Float offset;
        private final Float cornerRadius;
        private final Float textSize;
        private final String fontPath;
        private final String sourcePath;

        public StyleConfig(
                Integer backgroundColor,
                Integer textColor,
                Integer borderColor,
                Float textPadding,
                Float offset,
                Float cornerRadius,
                Float textSize,
                String fontPath,
                String sourcePath
        ) {
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            this.borderColor = borderColor;
            this.textPadding = textPadding;
            this.offset = offset;
            this.cornerRadius = cornerRadius;
            this.textSize = textSize;
            this.fontPath = fontPath;
            this.sourcePath = sourcePath;
        }

        public Integer getBackgroundColor() {
            return this.backgroundColor;
        }

        public Integer getTextColor() {
            return this.textColor;
        }

        public Integer getBorderColor() {
            return this.borderColor;
        }

        public Float getTextPadding() {
            return this.textPadding;
        }

        public Float getOffset() {
            return this.offset;
        }

        public Float getCornerRadius() {
            return this.cornerRadius;
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
