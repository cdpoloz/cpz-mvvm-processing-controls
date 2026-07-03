package com.cpz.processing.controls.controls.button.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;

import java.util.Objects;

/**
 * Minimal config DTO for a single button created from external data.
 *
 * @author CPZ
 */
public final class ButtonConfig {
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

    public ButtonConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this(code, text, x, y, width, height, enabled, visible, style, null);
    }

    public ButtonConfig(String code, String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this(code, text, ControlBounds.absolute(x, y, width, height), null, enabled, visible, style, tooltip);
    }

    public ButtonConfig(String code, String text, ControlBounds bounds, ControlMeasure textSize, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this.code = Objects.requireNonNull(code, "code");
        this.text = Objects.requireNonNull(text, "text");
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

    /**
     * Optional style block for the first config-driven iteration.
     */
    public static final class StyleConfig {
        private final Integer baseColor;
        private final Integer textColor;
        private final Integer strokeColor;
        private final Float strokeWeight;
        private final Float strokeWeightHover;
        private final Float cornerRadius;
        private final Integer disabledAlpha;
        private final Float hoverBlendWithWhite;
        private final Float pressedBlendWithBlack;
        private final RendererConfig renderer;
        private final String fontPath;
        private final Float textSize;
        private final String sourcePath;

        public StyleConfig(
                Integer baseColor,
                Integer textColor,
                Integer strokeColor,
                Float strokeWeight,
                Float strokeWeightHover,
                Float cornerRadius,
                Integer disabledAlpha,
                Float hoverBlendWithWhite,
                Float pressedBlendWithBlack,
                RendererConfig renderer
        ) {
            this(
                    baseColor,
                    textColor,
                    strokeColor,
                    strokeWeight,
                    strokeWeightHover,
                    cornerRadius,
                    disabledAlpha,
                    hoverBlendWithWhite,
                    pressedBlendWithBlack,
                    renderer,
                    null,
                    null,
                    null
            );
        }

        public StyleConfig(
                Integer baseColor,
                Integer textColor,
                Integer strokeColor,
                Float strokeWeight,
                Float strokeWeightHover,
                Float cornerRadius,
                Integer disabledAlpha,
                Float hoverBlendWithWhite,
                Float pressedBlendWithBlack,
                RendererConfig renderer,
                String fontPath,
                Float textSize,
                String sourcePath
        ) {
            this.baseColor = baseColor;
            this.textColor = textColor;
            this.strokeColor = strokeColor;
            this.strokeWeight = strokeWeight;
            this.strokeWeightHover = strokeWeightHover;
            this.cornerRadius = cornerRadius;
            this.disabledAlpha = disabledAlpha;
            this.hoverBlendWithWhite = hoverBlendWithWhite;
            this.pressedBlendWithBlack = pressedBlendWithBlack;
            this.renderer = renderer;
            this.fontPath = fontPath;
            this.textSize = textSize;
            this.sourcePath = sourcePath;
        }

        public Integer getBaseColor() {
            return this.baseColor;
        }

        public Integer getTextColor() {
            return this.textColor;
        }

        public Integer getStrokeColor() {
            return this.strokeColor;
        }

        public Float getStrokeWeight() {
            return this.strokeWeight;
        }

        public Float getStrokeWeightHover() {
            return this.strokeWeightHover;
        }

        public Float getCornerRadius() {
            return this.cornerRadius;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }

        public Float getHoverBlendWithWhite() {
            return this.hoverBlendWithWhite;
        }

        public Float getPressedBlendWithBlack() {
            return this.pressedBlendWithBlack;
        }

        public RendererConfig getRenderer() {
            return this.renderer;
        }

        public String getFontPath() {
            return this.fontPath;
        }

        public Float getTextSize() {
            return this.textSize;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }
    }

    /**
     * Minimal renderer config for the current config-driven button flow.
     */
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
