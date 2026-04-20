package com.cpz.processing.controls.controls.toggle.input;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single toggle instance.
 *
 * @author CPZ
 */
public final class ToggleInputLayer extends DefaultInputLayer {
    private final Toggle toggle;

    public ToggleInputLayer(int value, Toggle toggle) {
        super(value);
        this.toggle = Objects.requireNonNull(toggle, "toggle");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        } else {
            this.toggle.handlePointerEvent(event);
            return true;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
