package com.cpz.processing.controls.controls.radiogroup.input;

import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single radio group instance.
 *
 * @author CPZ
 */
public final class RadioGroupInputLayer extends DefaultInputLayer {
    private final RadioGroup radioGroup;

    public RadioGroupInputLayer(int priority, RadioGroup radioGroup) {
        super(priority);
        this.radioGroup = Objects.requireNonNull(radioGroup, "radioGroup");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }
        this.radioGroup.handlePointerEvent(event);
        return true;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (event == null) {
            return false;
        }
        this.radioGroup.handleKeyboardEvent(event);
        return true;
    }
}
