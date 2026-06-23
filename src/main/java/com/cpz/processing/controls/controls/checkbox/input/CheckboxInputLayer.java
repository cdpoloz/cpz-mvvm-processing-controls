package com.cpz.processing.controls.controls.checkbox.input;

import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single checkbox instance.
 *
 * @author CPZ
 */
public final class CheckboxInputLayer extends DefaultInputLayer {
    private final Checkbox checkbox;

    public CheckboxInputLayer(int value, Checkbox checkbox) {
        super(value);
        this.checkbox = Objects.requireNonNull(checkbox, "checkbox");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        } else {
            this.checkbox.handlePointerEvent(event);
            return true;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
