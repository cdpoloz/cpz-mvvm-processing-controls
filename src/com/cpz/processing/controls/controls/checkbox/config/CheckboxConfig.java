package com.cpz.processing.controls.controls.checkbox.config;

import java.util.Objects;

/**
 * Minimal config DTO for a single checkbox created from external data.
 *
 * @author CPZ
 */
public final class CheckboxConfig {
    private final String code;
    private final boolean checked;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public CheckboxConfig(String code, boolean checked, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this.code = Objects.requireNonNull(code, "code");
        this.checked = checked;
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

    public boolean isChecked() {
        return this.checked;
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
        private final Integer checkedFillOverride;
        private final Integer uncheckedFillOverride;
        private final Integer hoverFillOverride;
        private final Integer pressedFillOverride;
        private final Integer checkOverride;
        private final Integer strokeOverride;
        private final Integer boxColor;
        private final Integer boxHoverColor;
        private final Integer boxPressedColor;
        private final Integer checkColor;
        private final Integer borderColor;
        private final Float borderWidth;
        private final Float borderWidthHover;
        private final Float cornerRadius;
        private final Integer disabledAlpha;
        private final Float checkInset;
        private final RendererConfig renderer;

        public StyleConfig(
                Integer checkedFillOverride,
                Integer uncheckedFillOverride,
                Integer hoverFillOverride,
                Integer pressedFillOverride,
                Integer checkOverride,
                Integer strokeOverride,
                Integer boxColor,
                Integer boxHoverColor,
                Integer boxPressedColor,
                Integer checkColor,
                Integer borderColor,
                Float borderWidth,
                Float borderWidthHover,
                Float cornerRadius,
                Integer disabledAlpha,
                Float checkInset,
                RendererConfig renderer
        ) {
            this.checkedFillOverride = checkedFillOverride;
            this.uncheckedFillOverride = uncheckedFillOverride;
            this.hoverFillOverride = hoverFillOverride;
            this.pressedFillOverride = pressedFillOverride;
            this.checkOverride = checkOverride;
            this.strokeOverride = strokeOverride;
            this.boxColor = boxColor;
            this.boxHoverColor = boxHoverColor;
            this.boxPressedColor = boxPressedColor;
            this.checkColor = checkColor;
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
            this.borderWidthHover = borderWidthHover;
            this.cornerRadius = cornerRadius;
            this.disabledAlpha = disabledAlpha;
            this.checkInset = checkInset;
            this.renderer = renderer;
        }

        public Integer getCheckedFillOverride() {
            return this.checkedFillOverride;
        }

        public Integer getUncheckedFillOverride() {
            return this.uncheckedFillOverride;
        }

        public Integer getHoverFillOverride() {
            return this.hoverFillOverride;
        }

        public Integer getPressedFillOverride() {
            return this.pressedFillOverride;
        }

        public Integer getCheckOverride() {
            return this.checkOverride;
        }

        public Integer getStrokeOverride() {
            return this.strokeOverride;
        }

        public Integer getBoxColor() {
            return this.boxColor;
        }

        public Integer getBoxHoverColor() {
            return this.boxHoverColor;
        }

        public Integer getBoxPressedColor() {
            return this.boxPressedColor;
        }

        public Integer getCheckColor() {
            return this.checkColor;
        }

        public Integer getBorderColor() {
            return this.borderColor;
        }

        public Float getBorderWidth() {
            return this.borderWidth;
        }

        public Float getBorderWidthHover() {
            return this.borderWidthHover;
        }

        public Float getCornerRadius() {
            return this.cornerRadius;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }

        public Float getCheckInset() {
            return this.checkInset;
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
