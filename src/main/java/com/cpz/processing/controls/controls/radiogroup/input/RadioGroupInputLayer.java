package com.cpz.processing.controls.controls.radiogroup.input;

import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single radio group instance.
 *
 * <p>Consumption follows the facade eligibility contracts. An eligible
 * pointer press captures its drag/release sequence, and keyboard events are
 * consumed only while the group has usable focus.</p>
 *
 * @author CPZ
 */
public final class RadioGroupInputLayer extends DefaultInputLayer {
    private final RadioGroup radioGroup;
    private boolean pointerCaptured;

    public RadioGroupInputLayer(int priority, RadioGroup radioGroup) {
        super(priority);
        this.radioGroup = Objects.requireNonNull(radioGroup, "radioGroup");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }

        boolean eligible = this.radioGroup.canConsumePointerEvent(event);
        boolean captured = this.pointerCaptured
                && (event.getType() == PointerEvent.Type.DRAG
                || event.getType() == PointerEvent.Type.RELEASE);
        if (event.getType() == PointerEvent.Type.PRESS) {
            this.pointerCaptured = eligible;
        }

        this.radioGroup.handlePointerEvent(event);
        if (event.getType() == PointerEvent.Type.RELEASE) {
            this.pointerCaptured = false;
        }
        return eligible || captured;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (!this.radioGroup.canConsumeKeyboardEvent(event)) {
            return false;
        }
        this.radioGroup.handleKeyboardEvent(event);
        return true;
    }
}
