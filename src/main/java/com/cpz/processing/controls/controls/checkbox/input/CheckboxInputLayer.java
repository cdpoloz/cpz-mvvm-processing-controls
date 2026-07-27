package com.cpz.processing.controls.controls.checkbox.input;

import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single checkbox instance.
 *
 * <p>Events are forwarded so the facade can update transient state. The layer
 * reports consumption only while the checkbox is eligible for the event or
 * while completing a pointer interaction captured by an eligible press.</p>
 *
 * @author CPZ
 */
public final class CheckboxInputLayer extends DefaultInputLayer {
    private final Checkbox checkbox;
    private boolean pointerCaptured;

    public CheckboxInputLayer(int value, Checkbox checkbox) {
        super(value);
        this.checkbox = Objects.requireNonNull(checkbox, "checkbox");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }

        boolean eligible = this.checkbox.canConsumePointerEvent(event);
        boolean captured = this.pointerCaptured
                && (event.getType() == PointerEvent.Type.DRAG
                || event.getType() == PointerEvent.Type.RELEASE);
        if (event.getType() == PointerEvent.Type.PRESS) {
            this.pointerCaptured = eligible;
        }

        this.checkbox.handlePointerEvent(event);
        if (event.getType() == PointerEvent.Type.RELEASE) {
            this.pointerCaptured = false;
        }
        return eligible || captured;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
