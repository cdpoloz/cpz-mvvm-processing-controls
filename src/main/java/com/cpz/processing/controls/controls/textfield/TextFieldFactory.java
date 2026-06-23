package com.cpz.processing.controls.controls.textfield;

import com.cpz.processing.controls.controls.textfield.config.TextFieldConfig;
import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.controls.textfield.style.DefaultTextFieldStyle;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Factory for creating the public text field facade from external config.
 *
 * @author CPZ
 */
public final class TextFieldFactory {
    public static TextField create(PApplet sketch, TextFieldConfig config) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(config, "config");

        TextField textField = new TextField(
                sketch,
                config.getCode(),
                config.getText(),
                config.getX(),
                config.getY(),
                config.getWidth(),
                config.getHeight()
        );
        textField.setEnabled(config.isEnabled());
        textField.setVisible(config.isVisible());

        if (config.getStyle() != null) {
            textField.setStyle(new DefaultTextFieldStyle(toStyleConfig(config.getStyle())));
        }

        return textField;
    }

    private static TextFieldStyleConfig toStyleConfig(TextFieldConfig.StyleConfig style) {
        TextFieldStyleConfig result = new TextFieldStyleConfig();
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
