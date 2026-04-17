package com.cpz.processing.controls.controls.textfield.input;

import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single text field instance.
 */
public final class TextFieldInputLayer extends DefaultInputLayer {
    private final TextField textField;

    public TextFieldInputLayer(int priority, TextField textField) {
        super(priority);
        this.textField = Objects.requireNonNull(textField, "textField");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }
        this.textField.handlePointerEvent(event);
        return true;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (event == null || !this.textField.isFocused()) {
            return false;
        }
        this.textField.handleKeyboardEvent(event);
        return true;
    }
}
