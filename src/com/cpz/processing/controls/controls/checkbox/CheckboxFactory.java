package com.cpz.processing.controls.controls.checkbox;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxConfig;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.controls.checkbox.style.DefaultCheckboxStyle;
import com.cpz.processing.controls.controls.checkbox.style.render.SvgCheckboxRenderer;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public checkbox facade from external config.
 *
 * @author CPZ
 */
public final class CheckboxFactory {
    public static Checkbox create(PApplet sketch, CheckboxConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        Checkbox checkbox = new Checkbox(sketch, config.getCode(), config.isChecked(), config.getX(), config.getY(), config.getWidth(), config.getHeight());
        checkbox.setEnabled(config.isEnabled());
        checkbox.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            checkbox.setStyle(new DefaultCheckboxStyle(toStyleConfig(sketch, config.getStyle())));
        }

        return checkbox;
    }

    private static CheckboxStyleConfig toStyleConfig(PApplet sketch, CheckboxConfig.StyleConfig style) {
        CheckboxStyleConfig result = new CheckboxStyleConfig();
        if (style.getCheckedFillOverride() != null) {
            result.checkedFillOverride = style.getCheckedFillOverride();
        }
        if (style.getUncheckedFillOverride() != null) {
            result.uncheckedFillOverride = style.getUncheckedFillOverride();
        }
        if (style.getHoverFillOverride() != null) {
            result.hoverFillOverride = style.getHoverFillOverride();
        }
        if (style.getPressedFillOverride() != null) {
            result.pressedFillOverride = style.getPressedFillOverride();
        }
        if (style.getCheckOverride() != null) {
            result.checkOverride = style.getCheckOverride();
        }
        if (style.getStrokeOverride() != null) {
            result.strokeOverride = style.getStrokeOverride();
        }
        if (style.getBoxColor() != null) {
            result.boxColor = style.getBoxColor();
        }
        if (style.getBoxHoverColor() != null) {
            result.boxHoverColor = style.getBoxHoverColor();
        }
        if (style.getBoxPressedColor() != null) {
            result.boxPressedColor = style.getBoxPressedColor();
        }
        if (style.getCheckColor() != null) {
            result.checkColor = style.getCheckColor();
        }
        if (style.getBorderColor() != null) {
            result.borderColor = style.getBorderColor();
        }
        if (style.getBorderWidth() != null) {
            result.borderWidth = style.getBorderWidth();
        }
        if (style.getBorderWidthHover() != null) {
            result.borderWidthHover = style.getBorderWidthHover();
        }
        if (style.getCornerRadius() != null) {
            result.cornerRadius = style.getCornerRadius();
        }
        if (style.getDisabledAlpha() != null) {
            result.disabledAlpha = style.getDisabledAlpha();
        }
        if (style.getCheckInset() != null) {
            result.checkInset = style.getCheckInset();
        }
        if (style.getRenderer() != null && "svg".equals(style.getRenderer().getType())) {
            result.setRenderer(new SvgCheckboxRenderer(sketch, style.getRenderer().getPath()));
        }
        return result;
    }
}
