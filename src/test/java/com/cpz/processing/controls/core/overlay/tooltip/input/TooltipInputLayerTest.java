package com.cpz.processing.controls.core.overlay.tooltip.input;

import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipArea;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TooltipInputLayerTest {
    @Test
    void pointerEventsRefreshTooltipButAreNeverConsumed() {
        OverlayManager overlayManager = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(new PApplet(), overlayManager);
        controller.registerTarget(new TooltipArea(10.0F, 10.0F, 40.0F, 20.0F).setTooltip("Area"));
        TooltipInputLayer layer = new TooltipInputLayer(100, controller);

        boolean consumed = layer.handlePointerEvent(new PointerEvent(PointerEvent.Type.MOVE, 20.0F, 20.0F));

        assertFalse(consumed);
        assertEquals(1, overlayManager.getActiveOverlays().size());
    }
}
