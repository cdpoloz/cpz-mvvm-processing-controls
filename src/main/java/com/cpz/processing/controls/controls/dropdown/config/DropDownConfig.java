package com.cpz.processing.controls.controls.dropdown.config;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipConfig;
import com.cpz.processing.controls.core.util.ControlCode;

import java.util.List;
import java.util.Objects;

/**
 * Minimal config DTO for a single drop down created from external data.
 *
 * @author CPZ
 */
public final class DropDownConfig {
    private final String code;
    private final List<String> items;
    private final int selectedIndex;
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

    public DropDownConfig(String code, List<String> items, int selectedIndex, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style) {
        this(code, items, selectedIndex, x, y, width, height, enabled, visible, style, null);
    }

    public DropDownConfig(String code, List<String> items, int selectedIndex, float x, float y, float width, float height, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this(code, items, selectedIndex, ControlBounds.absolute(x, y, width, height), null, enabled, visible, style, tooltip);
    }

    public DropDownConfig(String code, List<String> items, int selectedIndex, ControlBounds bounds, ControlMeasure textSize, boolean enabled, boolean visible, StyleConfig style, TooltipConfig tooltip) {
        this.code = ControlCode.requireNonBlank(code);
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        this.selectedIndex = selectedIndex;
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
        private final String fontPath;
        private final String sourcePath;

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
            this(
                    baseFillOverride,
                    listFillOverride,
                    textOverride,
                    borderOverride,
                    hoverItemOverlayOverride,
                    selectedItemOverlayOverride,
                    focusedBorderOverride,
                    cornerRadius,
                    listCornerRadius,
                    strokeWeight,
                    focusedStrokeWeight,
                    textSize,
                    itemHeight,
                    textPadding,
                    arrowPadding,
                    maxVisibleItems,
                    disabledAlpha,
                    null,
                    null
            );
        }

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
                Integer disabledAlpha,
                String fontPath,
                String sourcePath
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
            this.fontPath = fontPath;
            this.sourcePath = sourcePath;
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

        public String getFontPath() {
            return this.fontPath;
        }

        public String getSourcePath() {
            return this.sourcePath;
        }
    }
}
