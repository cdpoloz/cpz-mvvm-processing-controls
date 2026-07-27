package com.cpz.processing.controls.controls.textfield.input;

import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.focus.FocusManagerAware;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single text field instance.
 *
 * <p>Pointer events outside the field are still forwarded so focus and
 * selection state can be cleared, but they are not consumed unless an
 * eligible press previously started a pointer capture. Keyboard consumption
 * follows the facade focus and availability contract.</p>
 *
 * @author CPZ
 */
public final class TextFieldInputLayer extends DefaultInputLayer implements FocusManagerAware {
    private final TextField textField;
    private boolean pointerCaptured;

    public TextFieldInputLayer(int priority, TextField textField) {
        super(priority);
        this.textField = Objects.requireNonNull(textField, "textField");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }

        boolean eligible = this.textField.canConsumePointerEvent(event);
        boolean captured = this.pointerCaptured
                && (event.getType() == PointerEvent.Type.DRAG
                || event.getType() == PointerEvent.Type.RELEASE);
        if (event.getType() == PointerEvent.Type.PRESS) {
            this.pointerCaptured = eligible;
        }

        this.textField.handlePointerEvent(event);
        if (event.getType() == PointerEvent.Type.RELEASE) {
            this.pointerCaptured = false;
        }
        return eligible || captured;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (!this.textField.canConsumeKeyboardEvent(event)) {
            return false;
        }
        this.textField.handleKeyboardEvent(event);
        return true;
    }

    @Override
    public void attachFocusManager(FocusManager focusManager) {
        this.textField.attachFocusManager(focusManager);
    }

    @Override
    public void detachFocusManager(FocusManager focusManager) {
        this.textField.detachFocusManager(focusManager);
    }
}
