package com.cpz.processing.controls.controls.dropdown.config;

import java.util.List;
import java.util.Objects;

/**
 * Minimal config DTO for a single drop down created from external data.
 */
public final class DropDownConfig {
    private final String code;
    private final List<String> items;
    private final int selectedIndex;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final boolean enabled;
    private final boolean visible;
    private final StyleConfig style;

    public DropDownConfig(String code, List<String> items, int selectedIndex, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this.code = Objects.requireNonNull(code, "code");
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        this.selectedIndex = selectedIndex;
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

    public List<String> getItems() {
        return this.items;
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
        private final Integer baseFillOverride;
        private final Integer listFillOverride;
        private final Integer textOverride;
        private final Integer borderOverride;
        private final Integer hoverItemOverlayOverride;
        private final Integer selectedItemOverlayOverride;
        private final Integer focusedBorderOverride;
        private final Float cornerRadius;
        private final Float listCornerRadius;
        private final Float strokeWeight;
        private final Float focusedStrokeWeight;
        private final Float textSize;
        private final Float itemHeight;
        private final Float textPadding;
        private final Float arrowPadding;
        private final Integer maxVisibleItems;
        private final Integer disabledAlpha;

        public StyleConfig(
                Integer baseFillOverride,
                Integer listFillOverride,
                Integer textOverride,
                Integer borderOverride,
                Integer hoverItemOverlayOverride,
                Integer selectedItemOverlayOverride,
                Integer focusedBorderOverride,
                Float cornerRadius,
                Float listCornerRadius,
                Float strokeWeight,
                Float focusedStrokeWeight,
                Float textSize,
                Float itemHeight,
                Float textPadding,
                Float arrowPadding,
                Integer maxVisibleItems,
                Integer disabledAlpha
        ) {
            this.baseFillOverride = baseFillOverride;
            this.listFillOverride = listFillOverride;
            this.textOverride = textOverride;
            this.borderOverride = borderOverride;
            this.hoverItemOverlayOverride = hoverItemOverlayOverride;
            this.selectedItemOverlayOverride = selectedItemOverlayOverride;
            this.focusedBorderOverride = focusedBorderOverride;
            this.cornerRadius = cornerRadius;
            this.listCornerRadius = listCornerRadius;
            this.strokeWeight = strokeWeight;
            this.focusedStrokeWeight = focusedStrokeWeight;
            this.textSize = textSize;
            this.itemHeight = itemHeight;
            this.textPadding = textPadding;
            this.arrowPadding = arrowPadding;
            this.maxVisibleItems = maxVisibleItems;
            this.disabledAlpha = disabledAlpha;
        }

        public Integer getBaseFillOverride() {
            return this.baseFillOverride;
        }

        public Integer getListFillOverride() {
            return this.listFillOverride;
        }

        public Integer getTextOverride() {
            return this.textOverride;
        }

        public Integer getBorderOverride() {
            return this.borderOverride;
        }

        public Integer getHoverItemOverlayOverride() {
            return this.hoverItemOverlayOverride;
        }

        public Integer getSelectedItemOverlayOverride() {
            return this.selectedItemOverlayOverride;
        }

        public Integer getFocusedBorderOverride() {
            return this.focusedBorderOverride;
        }

        public Float getCornerRadius() {
            return this.cornerRadius;
        }

        public Float getListCornerRadius() {
            return this.listCornerRadius;
        }

        public Float getStrokeWeight() {
            return this.strokeWeight;
        }

        public Float getFocusedStrokeWeight() {
            return this.focusedStrokeWeight;
        }

        public Float getTextSize() {
            return this.textSize;
        }

        public Float getItemHeight() {
            return this.itemHeight;
        }

        public Float getTextPadding() {
            return this.textPadding;
        }

        public Float getArrowPadding() {
            return this.arrowPadding;
        }

        public Integer getMaxVisibleItems() {
            return this.maxVisibleItems;
        }

        public Integer getDisabledAlpha() {
            return this.disabledAlpha;
        }
    }
}
