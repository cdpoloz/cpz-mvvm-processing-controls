package com.cpz.processing.controls.core.overlay.tooltip.input;

import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;

import java.util.Objects;

/**
 * Passive pointer layer that updates a tooltip overlay controller.
 *
 * <p>The layer never consumes pointer or keyboard events.</p>
 *
 * @author CPZ
 */
public final class TooltipInputLayer extends DefaultInputLayer {
    private final TooltipOverlayController controller;

    public TooltipInputLayer(int priority, TooltipOverlayController controller) {
        super(priority);
        this.controller = Objects.requireNonNull(controller, "controller");
    }

    public boolean handlePointerEvent(PointerEvent event) {
        if (event != null) {
            switch (event.getType()) {
                case MOVE:
                case DRAG:
                case PRESS:
                case RELEASE:
                    this.controller.showIfMouseOver(event.getX(), event.getY());
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public boolean handleKeyboardEvent(KeyboardEvent event) {
        return false;
    }
}
