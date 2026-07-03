package com.cpz.processing.controls.controls.radiogroup.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;

import java.util.List;
import java.util.Objects;

/**
 * Minimal config DTO for a single radio group created from external data.
 *
 * @author CPZ
 */
public final class RadioGroupConfig {
    private final String code;
    private final List<String> options;
    private final int selectedIndex;
    private final float x;
    private final float y;
    private final float width;
    private final ControlBounds bounds;
    private final boolean explicitBounds;
    private final ControlMeasure textSize;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;
    private final TooltipConfig tooltip;

    public RadioGroupConfig(String code, List<String> options, int selectedIndex, float x, float y, float width, boolean enabled, boolean visible, StyleConfig style) {
        this(code, options, selectedIndex, x, y, width, enabled, visible, style, null);
    }

    public RadioGroupConfig(String code, List<String> options, int selectedIndex, float x, float y, float width, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this(code, options, selectedIndex, ControlBounds.absolute(x, y, width, 0.0F), false, null, enabled, visible, style, tooltip);
    }

    public RadioGroupConfig(String code, List<String> options, int selectedIndex, ControlBounds bounds, boolean explicitBounds, ControlMeasure textSize, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this.code = Objects.requireNonNull(code, "code");
        this.options = List.copyOf(Objects.requireNonNull(options, "options"));
        this.selectedIndex = selectedIndex;
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.explicitBounds = explicitBounds;
        this.x = bounds.x().value();
        this.y = bounds.y().value();
        this.width = bounds.width().value();
        this.textSize = textSize;
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
        this.tooltip = tooltip;
    }

    public String getCode() {
        return this.code;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
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

    public ControlBounds getBounds() {
        return this.bounds;
    }

    public boolean hasExplicitBounds() {
        return this.explicitBounds;
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
        private final Integer textOverride;
        private final Integer indicatorOverride;
        private final Integer backgroundOverride;
        private final Integer hoveredBackgroundOverride;
        private final Integer pressedBackgroundOverride;
        private final Integer selectedDotOverride;
        private final Float itemHeight;
        private final Float itemSpacing;
        private final Float minimumItemHeight;
        private final Float indicatorOffsetX;
        private final Float textOffsetX;
        private final Float indicatorOuterDiameter;
        private final Float indicatorInnerDiameter;
        private final Float strokeWeight;
        private final Float textSize;
        private final Float cornerRadius;
        private final Integer disabledAlpha;
        private final String fontPath;
        private final String sourcePath;

        public StyleConfig(
                Integer textOverride,
                Integer indicatorOverride,
                Integer backgroundOverride,
                Integer hoveredBackgroundOverride,
                Integer pressedBackgroundOverride,
                Integer selectedDotOverride,
                Float itemHeight,
                Float itemSpacing,
                Float minimumItemHeight,
                Float indicatorOffsetX,
                Float textOffsetX,
                Float indicatorOuterDiameter,
                Float indicatorInnerDiameter,
                Float strokeWeight,
                Float textSize,
                Float cornerRadius,
                Integer disabledAlpha
        ) {
            this(
                    textOverride,
                    indicatorOverride,
                    backgroundOverride,
                    hoveredBackgroundOverride,
                    pressedBackgroundOverride,
                    selectedDotOverride,
                    itemHeight,
                    itemSpacing,
                    minimumItemHeight,
                    indicatorOffsetX,
                    textOffsetX,
                    indicatorOuterDiameter,
                    indicatorInnerDiameter,
                    strokeWeight,
                    textSize,
                    cornerRadius,
                    disabledAlpha,
                    null,
                    null
            );
        }

        public StyleConfig(
                Integer textOverride,
                Integer indicatorOverride,
                Integer backgroundOverride,
                Integer hoveredBackgroundOverride,
                Integer pressedBackgroundOverride,
                Integer selectedDotOverride,
                Float itemHeight,
                Float itemSpacing,
                Float minimumItemHeight,
                Float indicatorOffsetX,
                Float textOffsetX,
                Float indicatorOuterDiameter,
                Float indicatorInnerDiameter,
                Float strokeWeight,
                Float textSize,
                Float cornerRadius,
                Integer disabledAlpha,
                String fontPath,
                String sourcePath
        ) {
            this.textOverride = textOverride;
            this.indicatorOverride = indicatorOverride;
            this.backgroundOverride = backgroundOverride;
            this.hoveredBackgroundOverride = hoveredBackgroundOverride;
            this.pressedBackgroundOverride = pressedBackgroundOverride;
            this.selectedDotOverride = selectedDotOverride;
            this.itemHeight = itemHeight;
            this.itemSpacing = itemSpacing;
            this.minimumItemHeight = minimumItemHeight;
            this.indicatorOffsetX = indicatorOffsetX;
            this.textOffsetX = textOffsetX;
            this.indicatorOuterDiameter = indicatorOuterDiameter;
            this.indicatorInnerDiameter = indicatorInnerDiameter;
            this.strokeWeight = strokeWeight;
            this.textSize = textSize;
            this.cornerRadius = cornerRadius;
            this.disabledAlpha = disabledAlpha;
            this.fontPath = fontPath;
            this.sourcePath = sourcePath;
        }

        public Integer getTextOverride() {
            return this.textOverride;
        }

        public Integer getIndicatorOverride() {
            return this.indicatorOverride;
        }

        public Integer getBackgroundOverride() {
            return this.backgroundOverride;
        }

        public Integer getHoveredBackgroundOverride() {
            return this.hoveredBackgroundOverride;
        }

        public Integer getPressedBackgroundOverride() {
            return this.pressedBackgroundOverride;
        }

        public Integer getSelectedDotOverride() {
            return this.selectedDotOverride;
        }

        public Float getItemHeight() {
            return this.itemHeight;
        }

        public Float getItemSpacing() {
            return this.itemSpacing;
        }

        public Float getMinimumItemHeight() {
            return this.minimumItemHeight;
        }

        public Float getIndicatorOffsetX() {
            return this.indicatorOffsetX;
        }

        public Float getTextOffsetX() {
            return this.textOffsetX;
        }

        public Float getIndicatorOuterDiameter() {
            return this.indicatorOuterDiameter;
        }

        public Float getIndicatorInnerDiameter() {
            return this.indicatorInnerDiameter;
        }

        public Float getStrokeWeight() {
            return this.strokeWeight;
        }

        public Float getTextSize() {
            return this.textSize;
        }

        public Float getCornerRadius() {
            return this.cornerRadius;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }

        public String getFontPath() {
            return this.fontPath;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }
    }
}
