package com.cpz.processing.controls.core.overlay.tooltip.config;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;

/**
 * JSON-facing tooltip configuration.
 *
 * @author CPZ
 */
public final class TooltipConfig {
    private final String text;
    private final boolean enabled;
    private final StyleConfig style;
    private final String styleRef;
    private final TooltipStyleConfig resolvedStyle;

    public TooltipConfig(String text, boolean enabled, StyleConfig style) {
        this(text, enabled, style, null, null);
    }

    public TooltipConfig(String text, boolean enabled, StyleConfig style, String styleRef, TooltipStyleConfig resolvedStyle) {
        this.text = text == null ? "" : text;
        this.enabled = enabled;
        this.style = style;
        this.styleRef = styleRef;
        this.resolvedStyle = resolvedStyle == null ? null : new TooltipStyleConfig(resolvedStyle);
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

    public String getStyleRef() {
        return this.styleRef;
    }

    public TooltipStyleConfig getResolvedStyle() {
        return this.resolvedStyle == null ? null : new TooltipStyleConfig(this.resolvedStyle);
    }

    /**
     * Parsed style values. Legacy Float getters return the raw configured value
     * (pixels or factor); measure getters also retain its explicit mode.
     */
    public static final class StyleConfig {
        private final Integer backgroundColor;
        private final Integer textColor;
        private final Integer borderColor;
        private final ControlMeasure textPadding;
        private final ControlMeasure offset;
        private final ControlMeasure cornerRadius;
        private final ControlMeasure strokeWeight;
        private final ControlMeasure textSize;
        private final ControlMeasure minHeight;
        private final ControlMeasure minWidth;
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
            this(backgroundColor, textColor, borderColor, textPadding, offset, cornerRadius, null, textSize, fontPath, sourcePath);
        }

        public StyleConfig(
                Integer backgroundColor,
                Integer textColor,
                Integer borderColor,
                Float textPadding,
                Float offset,
                Float cornerRadius,
                Float strokeWeight,
                Float textSize,
                String fontPath,
                String sourcePath
        ) {
            this(backgroundColor, textColor, borderColor,
                    textPadding == null ? null : ControlMeasure.absolute(textPadding),
                    offset == null ? null : ControlMeasure.absolute(offset),
                    cornerRadius == null ? null : ControlMeasure.absolute(cornerRadius),
                    strokeWeight == null ? null : ControlMeasure.absolute(strokeWeight),
                    textSize == null ? null : ControlMeasure.absolute(textSize),
                    null, null, fontPath, sourcePath);
        }

        public StyleConfig(
                Integer backgroundColor,
                Integer textColor,
                Integer borderColor,
                ControlMeasure textPadding,
                ControlMeasure offset,
                ControlMeasure cornerRadius,
                ControlMeasure strokeWeight,
                ControlMeasure textSize,
                ControlMeasure minHeight,
                ControlMeasure minWidth,
                String fontPath,
                String sourcePath
        ) {
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            this.borderColor = borderColor;
            this.textPadding = textPadding;
            this.offset = offset;
            this.cornerRadius = cornerRadius;
            this.strokeWeight = strokeWeight;
            this.textSize = textSize;
            this.minHeight = minHeight;
            this.minWidth = minWidth;
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
            return this.textPadding == null ? null : this.textPadding.value();
        }

        public Float getOffset() {
            return this.offset == null ? null : this.offset.value();
        }

        public Float getCornerRadius() {
            return this.cornerRadius == null ? null : this.cornerRadius.value();
        }

        public Float getStrokeWeight() {
            return this.strokeWeight == null ? null : this.strokeWeight.value();
        }

        public Float getTextSize() {
            return this.textSize == null ? null : this.textSize.value();
        }

        public ControlMeasure getTextPaddingMeasure() {
            return this.textPadding;
        }

        public ControlMeasure getOffsetMeasure() {
            return this.offset;
        }

        public ControlMeasure getCornerRadiusMeasure() {
            return this.cornerRadius;
        }

        public ControlMeasure getStrokeWeightMeasure() {
            return this.strokeWeight;
        }

        public ControlMeasure getTextSizeMeasure() {
            return this.textSize;
        }

        public ControlMeasure getMinHeightMeasure() {
            return this.minHeight;
        }

        public ControlMeasure getMinWidthMeasure() {
            return this.minWidth;
        }

        public String getFontPath() {
            return this.fontPath;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }
    }
}
