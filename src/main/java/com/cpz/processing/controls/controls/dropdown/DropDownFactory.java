package com.cpz.processing.controls.controls.dropdown;

import com.cpz.processing.controls.controls.dropdown.config.DropDownConfig;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public drop down facade from external config.
 *
 * @author CPZ
 */
public final class DropDownFactory {
    public static DropDown create(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, DropDownConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(overlayManager, "overlayManager");
        Objects.requireNonNull(inputManager, "inputManager");
        Objects.requireNonNull(config, "config");

        DropDown dropDown = new DropDown(
                sketch,
                overlayManager,
                inputManager,
                config.getCode(),
                config.getItems(),
                config.getSelectedIndex(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
        dropDown.setEnabled(config.isEnabled());
        dropDown.setVisible(config.isVisible());
        if (config.getStyle() != null) {
            dropDown.setStyle(new DefaultDropDownStyle(toStyleConfig(config.getStyle())));
        }
        return dropDown;
    }

    private static DropDownStyleConfig toStyleConfig(DropDownConfig.StyleConfig style) {
        DropDownStyleConfig result = new DropDownStyleConfig();
        result.baseFillOverride = style.getBaseFillOverride();
        result.listFillOverride = style.getListFillOverride();
        result.textOverride = style.getTextOverride();
        result.borderOverride = style.getBorderOverride();
        result.hoverItemOverlayOverride = style.getHoverItemOverlayOverride();
        result.selectedItemOverlayOverride = style.getSelectedItemOverlayOverride();
        result.focusedBorderOverride = style.getFocusedBorderOverride();
        if (style.getCornerRadius() != null) {
            result.cornerRadius = style.getCornerRadius();
        }
        if (style.getListCornerRadius() != null) {
            result.listCornerRadius = style.getListCornerRadius();
        }
        if (style.getStrokeWeight() != null) {
            result.strokeWeight = style.getStrokeWeight();
        }
        if (style.getFocusedStrokeWeight() != null) {
            result.focusedStrokeWeight = style.getFocusedStrokeWeight();
        }
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
        }
        if (style.getItemHeight() != null) {
            result.itemHeight = style.getItemHeight();
        }
        if (style.getTextPadding() != null) {
            result.textPadding = style.getTextPadding();
        }
        if (style.getArrowPadding() != null) {
            result.arrowPadding = style.getArrowPadding();
        }
        if (style.getMaxVisibleItems() != null) {
            result.maxVisibleItems = style.getMaxVisibleItems();
        }
        result.disabledAlpha = style.getDisabledAlpha();
        result.font = null;
        result.themeProvider = null;
        return result;
    }
}
