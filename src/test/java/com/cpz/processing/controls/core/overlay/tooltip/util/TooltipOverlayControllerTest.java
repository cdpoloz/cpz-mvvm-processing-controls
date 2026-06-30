package com.cpz.processing.controls.core.overlay.tooltip.util;

import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipArea;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TooltipOverlayControllerTest {
    @Test
    void choosesTheLastRegisteredMatchingTarget() {
        OverlayManager overlayManager = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(new PApplet(), overlayManager);
        TooltipArea first = new TooltipArea(0.0F, 0.0F, 100.0F, 100.0F).setTooltip("First");
        TooltipArea second = new TooltipArea(0.0F, 0.0F, 100.0F, 100.0F).setTooltip("Second");

        controller.registerTarget(first);
        controller.registerTarget(second);
        controller.showIfMouseOver(50.0F, 50.0F);

        assertSame(second, controller.getActiveTarget());
        assertEquals(1, overlayManager.getActiveOverlays().size());
    }

    @Test
    void ignoresTargetsThatCannotShowTooltip() {
        OverlayManager overlayManager = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(new PApplet(), overlayManager);
        TooltipArea disabled = new TooltipArea(0.0F, 0.0F, 100.0F, 100.0F)
                .setTooltip("Disabled")
                .setEnabled(false);
        TooltipArea empty = new TooltipArea(0.0F, 0.0F, 100.0F, 100.0F)
                .setTooltip("   ");

        controller.registerTarget(disabled);
        controller.registerTarget(empty);
        controller.showIfMouseOver(50.0F, 50.0F);

        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void legacySupplierConstructorStillSyncsWithoutPointerCoordinates() {
        OverlayManager overlayManager = new OverlayManager();
        AtomicBoolean hovered = new AtomicBoolean(true);
        TooltipOverlayController controller = new TooltipOverlayController(
                new PApplet(),
                overlayManager,
                hovered::get,
                () -> "Legacy",
                new TooltipOverlayController.AnchorBoundsProvider() {
                    public float getCenterX() {
                        return 20.0F;
                    }

                    public float getTopY() {
                        return 10.0F;
                    }
                }
        );

        controller.sync();
        assertEquals(1, overlayManager.getActiveOverlays().size());

        hovered.set(false);
        controller.sync();
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }
}
