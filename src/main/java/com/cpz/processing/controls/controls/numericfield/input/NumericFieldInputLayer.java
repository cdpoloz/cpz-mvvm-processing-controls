package com.cpz.processing.controls.controls.numericfield.input;

import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single numeric field instance.
 *
 * @author CPZ
 */
public final class NumericFieldInputLayer extends DefaultInputLayer {
    private final NumericField numericField;

    public NumericFieldInputLayer(int priority, NumericField numericField) {
        super(priority);
        this.numericField = Objects.requireNonNull(numericField, "numericField");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }
        this.numericField.handlePointerEvent(event);
        if (event.getType() == PointerEvent.Type.MOVE) {
            return false;
        }
        return this.numericField.isFocused();
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (event == null || !this.numericField.isFocused() || !this.numericField.isEnabled() || !this.numericField.isVisible()) {
            return false;
        }
        this.numericField.handleKeyboardEvent(event);
        return true;
    }
}
