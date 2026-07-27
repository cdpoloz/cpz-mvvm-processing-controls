package com.cpz.processing.controls.controls.dropdown.util;

import com.cpz.processing.controls.core.input.PointerEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates sibling drop-down transfer within one input routing scope.
 *
 * <p>Each {@code InputManager} owns one coordinator. Controllers participate
 * only while their base input layer, or containing panel, is registered in
 * that manager.</p>
 *
 * @author CPZ
 */
public final class DropDownCoordinator {
    private final List<DropDownOverlayController> controllers = new ArrayList<>();

    /**
     * Creates an empty routing-scoped coordinator.
     */
    public DropDownCoordinator() {
    }

    void register(DropDownOverlayController controller) {
        if (controller != null && !this.controllers.contains(controller)) {
            this.controllers.add(controller);
        }
    }

    void unregister(DropDownOverlayController controller) {
        this.controllers.remove(controller);
    }

    boolean routePressToSibling(DropDownOverlayController source, PointerEvent event) {
        for (DropDownOverlayController candidate : List.copyOf(this.controllers)) {
            if (candidate != source && candidate.containsGlobalBase(event.getX(), event.getY())) {
                source.closeOverlay();
                candidate.handleTransferredPress(event.getX(), event.getY());
                return true;
            }
        }
        return false;
    }
}
