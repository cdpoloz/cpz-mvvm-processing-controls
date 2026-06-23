package com.cpz.processing.controls.controls.dropdown.input;

import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.Objects;

/**
 * Reusable input layer for a single drop down instance.
 *
 * @author CPZ
 */
public final class DropDownInputLayer extends DefaultInputLayer {
    private final DropDown dropDown;

    public DropDownInputLayer(int priority, DropDown dropDown) {
        super(priority);
        this.dropDown = Objects.requireNonNull(dropDown, "dropDown");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }
        boolean wasExpanded = this.dropDown.isExpanded();
        this.dropDown.handlePointerEvent(event);
        if (event.getType() == PointerEvent.Type.MOVE || event.getType() == PointerEvent.Type.DRAG) {
            return false;
        }
        return wasExpanded || this.dropDown.isExpanded();
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
