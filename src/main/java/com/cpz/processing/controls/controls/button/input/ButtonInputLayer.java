package com.cpz.processing.controls.controls.button.input;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reusable input layer for one or more button instances.
 * <p>
 * Responsibilities:
 * - Bridge a top-level input pipeline to one or more composed {@link Button} instances.
 * <p>
 * Behavior:
 * - Forwards supported pointer events to each registered button facade.
 * - Does not consume wheel or keyboard input.
 *
 * @author CPZ
 */
public final class ButtonInputLayer extends DefaultInputLayer {
    private final List<Button> buttons = new ArrayList<>();

    /**
     * Creates an input layer for one or more buttons.
     *
     * @param value dispatch priority
     * @param buttons buttons that receive normalized pointer events
     */
    public ButtonInputLayer(int value, Button... buttons) {
        super(value);
        if (buttons != null) {
            for (Button button : buttons) {
                addButton(button);
            }
        }
    }

    public void addButton(Button button) {
        this.buttons.add(Objects.requireNonNull(button, "button"));
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }
        boolean consumed = false;
        for (Button button : this.buttons) {
            consumed = button.canConsumePointerEvent(event) || consumed;
            button.handlePointerEvent(event);
        }
        return consumed;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
