package com.cpz.processing.controls.controls.numericfield.input;

import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single numeric field instance.
 *
 * <p>Pointer consumption follows facade eligibility plus capture initiated by
 * an eligible press; focus by itself does not consume unrelated pointer
 * events. Keyboard consumption follows the facade focus and availability
 * contract.</p>
 *
 * @author CPZ
 */
public final class NumericFieldInputLayer extends DefaultInputLayer {
    private final NumericField numericField;
    private boolean pointerCaptured;

    public NumericFieldInputLayer(int priority, NumericField numericField) {
        super(priority);
        this.numericField = Objects.requireNonNull(numericField, "numericField");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }

        boolean eligible = this.numericField.canConsumePointerEvent(event);
        boolean captured = this.pointerCaptured
                && (event.getType() == PointerEvent.Type.DRAG
                || event.getType() == PointerEvent.Type.RELEASE);
        if (event.getType() == PointerEvent.Type.PRESS) {
            this.pointerCaptured = eligible;
        }

        this.numericField.handlePointerEvent(event);
        if (event.getType() == PointerEvent.Type.RELEASE) {
            this.pointerCaptured = false;
        }
        return eligible || captured;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (!this.numericField.canConsumeKeyboardEvent(event)) {
            return false;
        }
        this.numericField.handleKeyboardEvent(event);
        return true;
    }
}
