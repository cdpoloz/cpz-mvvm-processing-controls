package com.cpz.processing.controls.controls.panel.input;

import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reusable input layer for one or more panel instances.
 *
 * @author CPZ
 */
public final class PanelInputLayer extends DefaultInputLayer {
    private final List<Panel> panels = new ArrayList<>();

    public PanelInputLayer(int priority, Panel... panels) {
        super(priority);
        if (panels != null) {
            for (Panel panel : panels) {
                addPanel(panel);
            }
        }
    }

    public void addPanel(Panel panel) {
        this.panels.add(Objects.requireNonNull(panel, "panel"));
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event == null || event.getType() == PointerEvent.Type.WHEEL) {
            return false;
        }

        boolean consumed = false;
        for (Panel panel : this.panels) {
            panel.handlePointerEvent(event);
            consumed = panel.canConsumePointerEvent(event) || consumed;
        }
        return consumed;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        if (event == null) {
            return false;
        }

        for (int i = this.panels.size() - 1; i >= 0; i--) {
            Panel panel = this.panels.get(i);
            if (panel.canConsumeKeyboardEvent(event)) {
                panel.handleKeyboardEvent(event);
                return true;
            }
        }
        return false;
    }
}
