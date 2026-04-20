package com.cpz.processing.controls.controls.numericfield;

import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfig;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldStyle;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public numeric field facade from external config.
 *
 * @author CPZ
 */
public final class NumericFieldFactory {
    public static NumericField create(PApplet sketch, NumericFieldConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        NumericField numericField = new NumericField(
                sketch,
                config.getCode(),
                config.getText(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
        numericField.setEnabled(config.isEnabled());
        numericField.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            numericField.setStyle(new NumericFieldStyle(toStyleConfig(config.getStyle())));
        }

        return numericField;
    }

    private static NumericFieldStyleConfig toStyleConfig(NumericFieldConfig.StyleConfig style) {
        NumericFieldStyleConfig result = new NumericFieldStyleConfig();
        result.backgroundColor = style.getBackgroundColor();
        result.borderColor = style.getBorderColor();
        result.textColor = style.getTextColor();
        result.cursorColor = style.getCursorColor();
        result.selectionColor = style.getSelectionColor();
        result.selectionTextColor = style.getSelectionTextColor();
        if (style.getTextSize() != null) {
            result.textSize = style.getTextSize();
        }
        result.font = null;
        return result;
    }
}
