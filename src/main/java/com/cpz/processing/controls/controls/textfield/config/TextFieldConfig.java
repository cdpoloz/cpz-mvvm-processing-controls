package com.cpz.processing.controls.controls.textfield.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.Objects;

/**
 * Minimal config DTO for a single text field created from external data.
 *
 * @author CPZ
 */
public final class TextFieldConfig {
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

    public TextFieldConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this(code, text, x, y, width, height, enabled, visible, style, null);
    }

    public TextFieldConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this(code, text, ControlBounds.absolute(x, y, width, height), null, enabled, visible, style, tooltip);
    }

    public TextFieldConfig(String code, String text, ControlBounds bounds, ControlMeasure textSize, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this.code = ControlCode.requireNonBlank(code);
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
