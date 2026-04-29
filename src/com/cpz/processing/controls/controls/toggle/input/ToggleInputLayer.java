package com.cpz.processing.controls.controls.toggle.input;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reusable input layer for one or more toggle instances.
 *
 * @author CPZ
 */
public final class ToggleInputLayer extends DefaultInputLayer {
    private final List<Toggle> toggles = new ArrayList<>();

    public ToggleInputLayer(int value, Toggle... toggles) {
        super(value);
        if (toggles != null) {
            for (Toggle toggle : toggles) {
                addToggle(toggle);
            }
        }
    }

    public void addToggle(Toggle toggle) {
        this.toggles.add(Objects.requireNonNull(toggle, "toggle"));
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }
        boolean consumed = false;
        for (Toggle toggle : this.toggles) {
            consumed = toggle.canConsumePointerEvent(event) || consumed;
            toggle.handlePointerEvent(event);
        }
        return consumed;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
