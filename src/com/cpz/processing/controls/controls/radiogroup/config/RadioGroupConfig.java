package com.cpz.processing.controls.controls.radiogroup.config;

import java.util.List;
import java.util.Objects;

/**
 * Minimal config DTO for a single radio group created from external data.
 */
public final class RadioGroupConfig {
    private final String code;
    private final List<String> options;
    private final int selectedIndex;
    private final float x;
    private final float y;
    private final float width;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public RadioGroupConfig(String code, List<String> options, int selectedIndex, float x, float y, float width, boolean enabled, boolean visible, StyleConfig style) {
        this.code = Objects.requireNonNull(code, "code");
        this.options = List.copyOf(Objects.requireNonNull(options, "options"));
        this.selectedIndex = selectedIndex;
        this.x = x;
        this.y = y;
        this.width = width;
        this.enabled = enabled;
        this.visible = visible;
        this.style = style;
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
    }
}
