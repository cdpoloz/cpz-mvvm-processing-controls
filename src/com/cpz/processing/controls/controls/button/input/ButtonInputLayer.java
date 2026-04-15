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
 */
public final class ButtonInputLayer extends DefaultInputLayer {
    private final Button button;

    /**
     * Creates an input layer for a single button.
     *
     * @param var1 dispatch priority
     * @param var2 button that receives normalized pointer events
     */
    public ButtonInputLayer(int var1, Button var2) {
        super(var1);
        this.button = Objects.requireNonNull(var2, "button");
    }

    public boolean handlePointerEvent(PointerEvent var1) {
        if (var1 == null || var1.getType() == PointerEvent.Type.WHEEL) {
            return false;
        } else {
            this.button.handlePointerEvent(var1);
            return true;
        }
    }

    public boolean handleKeyboardEvent(KeyboardEvent var1) {
        return false;
    }
}
