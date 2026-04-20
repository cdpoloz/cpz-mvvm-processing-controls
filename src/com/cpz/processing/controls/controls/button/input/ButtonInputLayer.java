package com.cpz.processing.controls.controls.button.input;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single button instance.
 * <p>
 * Responsibilities:
 * - Bridge a top-level input pipeline to a composed {@link Button}.
 * - Keep the single-button case explicit without embedding global infrastructure in the control.
 * <p>
 * Behavior:
 * - Forwards supported pointer events to the button facade.
 * - Does not consume wheel or keyboard input.
 *
 * @author CPZ
 */
public final class ButtonInputLayer extends DefaultInputLayer {
    private final Button button;

    /**
     * Creates an input layer for a single button.
     *
     * @param value dispatch priority
     * @param button button that receives normalized pointer events
     */
    public ButtonInputLayer(int value, Button button) {
        super(value);
        this.button = Objects.requireNonNull(button, "button");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        } else {
            this.button.handlePointerEvent(event);
            return true;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
