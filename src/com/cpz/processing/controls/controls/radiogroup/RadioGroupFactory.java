package com.cpz.processing.controls.controls.radiogroup;

import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfig;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupStyle;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public radio group facade from external config.
 */
public final class RadioGroupFactory {
    public static RadioGroup create(PApplet sketch, RadioGroupConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        RadioGroup radioGroup = new RadioGroup(
                sketch,
                config.getCode(),
                config.getOptions(),
                config.getSelectedIndex(),
                config.getX(),
                config.getY(),
                config.getWidth()
        );
        radioGroup.setEnabled(config.isEnabled());
        radioGroup.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            radioGroup.setStyle(new RadioGroupStyle(toStyleConfig(config.getStyle())));
        }

        return radioGroup;
    }

    private static RadioGroupStyleConfig toStyleConfig(RadioGroupConfig.StyleConfig style) {
        RadioGroupStyleConfig result = new RadioGroupStyleConfig();
        result.textOverride = style.getTextOverride();
        result.indicatorOverride = style.getIndicatorOverride();
        result.backgroundOverride = style.getBackgroundOverride();
        result.hoveredBackgroundOverride = style.getHoveredBackgroundOverride();
        result.pressedBackgroundOverride = style.getPressedBackgroundOverride();
        result.selectedDotOverride = style.getSelectedDotOverride();
        if (style.getItemHeight() != null) {
            result.itemHeight = style.getItemHeight();
        }
        if (style.getItemSpacing() != null) {
            result.itemSpacing = style.getItemSpacing();
        }
        if (style.getMinimumItemHeight() != null) {
            result.minimumItemHeight = style.getMinimumItemHeight();
        }
        if (style.getIndicatorOffsetX() != null) {
            result.indicatorOffsetX = style.getIndicatorOffsetX();
        }
        if (style.getTextOffsetX() != null) {
            result.textOffsetX = style.getTextOffsetX();
        }
        if (style.getIndicatorOuterDiameter() != null) {
            result.indicatorOuterDiameter = style.getIndicatorOuterDiameter();
        }
        if (style.getIndicatorInnerDiameter() != null) {
            result.indicatorInnerDiameter = style.getIndicatorInnerDiameter();
        }
        if (style.getStrokeWeight() != null) {
            result.strokeWeight = style.getStrokeWeight();
        }
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
        }
        if (style.getCornerRadius() != null) {
            result.cornerRadius = style.getCornerRadius();
        }
        result.disabledAlpha = style.getDisabledAlpha();
        return result;
    }
}
