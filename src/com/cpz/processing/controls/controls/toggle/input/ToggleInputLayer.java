package com.cpz.processing.controls.controls.toggle.input;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single toggle instance.
 */
public final class ToggleInputLayer extends DefaultInputLayer {
    private final Toggle toggle;

    public ToggleInputLayer(int var1, Toggle var2) {
        super(var1);
        this.toggle = Objects.requireNonNull(var2, "toggle");
    }

    public boolean handlePointerEvent(PointerEvent var1) {
        if (var1 == null || var1.getType() == PointerEvent.Type.WHEEL) {
            return false;
        } else {
            this.toggle.handlePointerEvent(var1);
            return true;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent var1) {
        return false;
    }
}
