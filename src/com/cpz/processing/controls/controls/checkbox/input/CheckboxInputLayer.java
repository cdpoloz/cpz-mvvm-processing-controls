package com.cpz.processing.controls.controls.checkbox.input;

import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single checkbox instance.
 */
public final class CheckboxInputLayer extends DefaultInputLayer {
    private final Checkbox checkbox;

    public CheckboxInputLayer(int var1, Checkbox var2) {
        super(var1);
        this.checkbox = Objects.requireNonNull(var2, "checkbox");
    }

    public boolean handlePointerEvent(PointerEvent var1) {
        if (var1 == null || var1.getType() == PointerEvent.Type.WHEEL) {
            return false;
        } else {
            this.checkbox.handlePointerEvent(var1);
            return true;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent var1) {
        return false;
    }
}
