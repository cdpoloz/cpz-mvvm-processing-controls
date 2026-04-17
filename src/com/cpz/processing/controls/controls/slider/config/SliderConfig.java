package com.cpz.processing.controls.controls.slider.config;

import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Minimal config DTO for a single slider created from external data.
 */
public final class SliderConfig {
    private final String code;
    private final BigDecimal min;
    private final BigDecimal max;
    private final BigDecimal step;
    private final BigDecimal value;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final SliderOrientation orientation;
    private final SnapMode snapMode;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public SliderConfig(
            String code,
            BigDecimal min,
            BigDecimal max,
            BigDecimal step,
            BigDecimal value,
            float x,
            float y,
            float width,
            float height,
            SliderOrientation orientation,
            SnapMode snapMode,
            boolean enabled,
            boolean visible,
            StyleConfig style
    ) {
        this.code = Objects.requireNonNull(code, "code");
        this.min = Objects.requireNonNull(min, "min");
        this.max = Objects.requireNonNull(max, "max");
        this.step = Objects.requireNonNull(step, "step");
        this.value = Objects.requireNonNull(value, "value");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orientation = Objects.requireNonNull(orientation, "orientation");
        this.snapMode = Objects.requireNonNull(snapMode, "snapMode");
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
    }

    public String getCode() {
        return this.code;
    }

    public BigDecimal getMin() {
        return this.min;
    }

    public BigDecimal getMax() {
        return this.max;
    }

    public BigDecimal getStep() {
        return this.step;
    }

    public BigDecimal getValue() {
        return this.value;
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

    public SliderOrientation getOrientation() {
        return this.orientation;
    }

    public SnapMode getSnapMode() {
        return this.snapMode;
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
        private final Integer trackOverride;
        private final Integer trackHoverOverride;
        private final Integer trackPressedOverride;
        private final Integer progressOverride;
        private final Integer progressHoverOverride;
        private final Integer progressPressedOverride;
        private final Integer thumbOverride;
        private final Integer thumbHoverOverride;
        private final Integer thumbPressedOverride;
        private final Integer trackStrokeOverride;
        private final Integer thumbStrokeOverride;
        private final Integer textOverride;
        private final Integer trackColor;
        private final Integer trackHoverColor;
        private final Integer trackPressedColor;
        private final Integer trackStrokeColor;
        private final Float trackStrokeWeight;
        private final Float trackStrokeWeightHover;
        private final Float trackThickness;
        private final Integer activeTrackColor;
        private final Integer activeTrackHoverColor;
        private final Integer activeTrackPressedColor;
        private final Integer thumbColor;
        private final Integer thumbHoverColor;
        private final Integer thumbPressedColor;
        private final Integer thumbStrokeColor;
        private final Float thumbStrokeWeight;
        private final Float thumbStrokeWeightHover;
        private final Float thumbSize;
        private final Integer textColor;
        private final Integer disabledAlpha;
        private final Boolean showValueText;
        private final SvgColorMode svgColorMode;
        private final RendererConfig renderer;

        public StyleConfig(
                Integer trackOverride,
                Integer trackHoverOverride,
                Integer trackPressedOverride,
                Integer progressOverride,
                Integer progressHoverOverride,
                Integer progressPressedOverride,
                Integer thumbOverride,
                Integer thumbHoverOverride,
                Integer thumbPressedOverride,
                Integer trackStrokeOverride,
                Integer thumbStrokeOverride,
                Integer textOverride,
                Integer trackColor,
                Integer trackHoverColor,
                Integer trackPressedColor,
                Integer trackStrokeColor,
                Float trackStrokeWeight,
                Float trackStrokeWeightHover,
                Float trackThickness,
                Integer activeTrackColor,
                Integer activeTrackHoverColor,
                Integer activeTrackPressedColor,
                Integer thumbColor,
                Integer thumbHoverColor,
                Integer thumbPressedColor,
                Integer thumbStrokeColor,
                Float thumbStrokeWeight,
                Float thumbStrokeWeightHover,
                Float thumbSize,
                Integer textColor,
                Integer disabledAlpha,
                Boolean showValueText,
                SvgColorMode svgColorMode,
                RendererConfig renderer
        ) {
            this.trackOverride = trackOverride;
            this.trackHoverOverride = trackHoverOverride;
            this.trackPressedOverride = trackPressedOverride;
            this.progressOverride = progressOverride;
            this.progressHoverOverride = progressHoverOverride;
            this.progressPressedOverride = progressPressedOverride;
            this.thumbOverride = thumbOverride;
            this.thumbHoverOverride = thumbHoverOverride;
            this.thumbPressedOverride = thumbPressedOverride;
            this.trackStrokeOverride = trackStrokeOverride;
            this.thumbStrokeOverride = thumbStrokeOverride;
            this.textOverride = textOverride;
            this.trackColor = trackColor;
            this.trackHoverColor = trackHoverColor;
            this.trackPressedColor = trackPressedColor;
            this.trackStrokeColor = trackStrokeColor;
            this.trackStrokeWeight = trackStrokeWeight;
            this.trackStrokeWeightHover = trackStrokeWeightHover;
            this.trackThickness = trackThickness;
            this.activeTrackColor = activeTrackColor;
            this.activeTrackHoverColor = activeTrackHoverColor;
            this.activeTrackPressedColor = activeTrackPressedColor;
            this.thumbColor = thumbColor;
            this.thumbHoverColor = thumbHoverColor;
            this.thumbPressedColor = thumbPressedColor;
            this.thumbStrokeColor = thumbStrokeColor;
            this.thumbStrokeWeight = thumbStrokeWeight;
            this.thumbStrokeWeightHover = thumbStrokeWeightHover;
            this.thumbSize = thumbSize;
            this.textColor = textColor;
            this.disabledAlpha = disabledAlpha;
            this.showValueText = showValueText;
            this.svgColorMode = svgColorMode;
            this.renderer = renderer;
        }

        public Integer getTrackOverride() {
            return this.trackOverride;
        }

        public Integer getTrackHoverOverride() {
            return this.trackHoverOverride;
        }

        public Integer getTrackPressedOverride() {
            return this.trackPressedOverride;
        }

        public Integer getProgressOverride() {
            return this.progressOverride;
        }

        public Integer getProgressHoverOverride() {
            return this.progressHoverOverride;
        }

        public Integer getProgressPressedOverride() {
            return this.progressPressedOverride;
        }

        public Integer getThumbOverride() {
            return this.thumbOverride;
        }

        public Integer getThumbHoverOverride() {
            return this.thumbHoverOverride;
        }

        public Integer getThumbPressedOverride() {
            return this.thumbPressedOverride;
        }

        public Integer getTrackStrokeOverride() {
            return this.trackStrokeOverride;
        }

        public Integer getThumbStrokeOverride() {
            return this.thumbStrokeOverride;
        }

        public Integer getTextOverride() {
            return this.textOverride;
        }

        public Integer getTrackColor() {
            return this.trackColor;
        }

        public Integer getTrackHoverColor() {
            return this.trackHoverColor;
        }

        public Integer getTrackPressedColor() {
            return this.trackPressedColor;
        }

        public Integer getTrackStrokeColor() {
            return this.trackStrokeColor;
        }

        public Float getTrackStrokeWeight() {
            return this.trackStrokeWeight;
        }

        public Float getTrackStrokeWeightHover() {
            return this.trackStrokeWeightHover;
        }

        public Float getTrackThickness() {
            return this.trackThickness;
        }

        public Integer getActiveTrackColor() {
            return this.activeTrackColor;
        }

        public Integer getActiveTrackHoverColor() {
            return this.activeTrackHoverColor;
        }

        public Integer getActiveTrackPressedColor() {
            return this.activeTrackPressedColor;
        }

        public Integer getThumbColor() {
            return this.thumbColor;
        }

        public Integer getThumbHoverColor() {
            return this.thumbHoverColor;
        }

        public Integer getThumbPressedColor() {
            return this.thumbPressedColor;
        }

        public Integer getThumbStrokeColor() {
            return this.thumbStrokeColor;
        }

        public Float getThumbStrokeWeight() {
            return this.thumbStrokeWeight;
        }

        public Float getThumbStrokeWeightHover() {
            return this.thumbStrokeWeightHover;
        }

        public Float getThumbSize() {
            return this.thumbSize;
        }

        public Integer getTextColor() {
            return this.textColor;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }

        public Boolean getShowValueText() {
            return this.showValueText;
        }

        public SvgColorMode getSvgColorMode() {
            return this.svgColorMode;
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
