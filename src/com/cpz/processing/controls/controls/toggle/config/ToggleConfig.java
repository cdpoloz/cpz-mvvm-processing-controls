package com.cpz.processing.controls.controls.toggle.config;

import java.util.Objects;

/**
 * Minimal config DTO for a single toggle created from external data.
 */
public final class ToggleConfig {
    private final String code;
    private final int state;
    private final int totalStates;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public ToggleConfig(String code, int state, int totalStates, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this.code = Objects.requireNonNull(code, "code");
        this.state = state;
        this.totalStates = totalStates;
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

    public int getState() {
        return this.state;
    }

    public int getTotalStates() {
        return this.totalStates;
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
        private final Integer offFillOverride;
        private final Integer onFillOverride;
        private final Integer hoverFillOverride;
        private final Integer pressedFillOverride;
        private final Integer strokeOverride;
        private final Integer[] stateColors;
        private final Integer strokeColor;
        private final Float strokeWidth;
        private final Float strokeWidthHover;
        private final Float hoverBlendWithWhite;
        private final Float pressedBlendWithBlack;
        private final Integer disabledAlpha;
        private final RendererConfig renderer;

        public StyleConfig(
                Integer offFillOverride,
                Integer onFillOverride,
                Integer hoverFillOverride,
                Integer pressedFillOverride,
                Integer strokeOverride,
                Integer[] stateColors,
                Integer strokeColor,
                Float strokeWidth,
                Float strokeWidthHover,
                Float hoverBlendWithWhite,
                Float pressedBlendWithBlack,
                Integer disabledAlpha,
                RendererConfig renderer
        ) {
            this.offFillOverride = offFillOverride;
            this.onFillOverride = onFillOverride;
            this.hoverFillOverride = hoverFillOverride;
            this.pressedFillOverride = pressedFillOverride;
            this.strokeOverride = strokeOverride;
            this.stateColors = stateColors;
            this.strokeColor = strokeColor;
            this.strokeWidth = strokeWidth;
            this.strokeWidthHover = strokeWidthHover;
            this.hoverBlendWithWhite = hoverBlendWithWhite;
            this.pressedBlendWithBlack = pressedBlendWithBlack;
            this.disabledAlpha = disabledAlpha;
            this.renderer = renderer;
        }

        public Integer getOffFillOverride() {
            return this.offFillOverride;
        }

        public Integer getOnFillOverride() {
            return this.onFillOverride;
        }

        public Integer getHoverFillOverride() {
            return this.hoverFillOverride;
        }

        public Integer getPressedFillOverride() {
            return this.pressedFillOverride;
        }

        public Integer getStrokeOverride() {
            return this.strokeOverride;
        }

        public Integer[] getStateColors() {
            return this.stateColors;
        }

        public Integer getStrokeColor() {
            return this.strokeColor;
        }

        public Float getStrokeWidth() {
            return this.strokeWidth;
        }

        public Float getStrokeWidthHover() {
            return this.strokeWidthHover;
        }

        public Float getHoverBlendWithWhite() {
            return this.hoverBlendWithWhite;
        }

        public Float getPressedBlendWithBlack() {
            return this.pressedBlendWithBlack;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }

        public RendererConfig getRenderer() {
            return this.renderer;
        }
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
