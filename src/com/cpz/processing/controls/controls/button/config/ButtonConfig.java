package com.cpz.processing.controls.controls.button.config;

import java.util.Objects;

/**
 * Minimal config DTO for a single button created from external data.
 */
public final class ButtonConfig {
    private final String text;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public ButtonConfig(String text, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this.text = Objects.requireNonNull(text, "text");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
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
