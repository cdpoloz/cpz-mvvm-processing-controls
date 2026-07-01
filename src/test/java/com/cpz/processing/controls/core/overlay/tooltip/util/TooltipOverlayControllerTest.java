package com.cpz.processing.controls.core.overlay.tooltip.util;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipArea;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void refreshUpdatesRuntimeTooltipText() {
        OverlayManager overlayManager = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(new PApplet(), overlayManager);
        TooltipArea area = new TooltipArea(0.0F, 0.0F, 100.0F, 100.0F).setTooltip("Initial");
        controller.registerTarget(area);
        controller.showIfMouseOver(50.0F, 50.0F);
        assertEquals("Initial", controller.getCurrentText());

        area.setTooltipText("Updated");
        controller.refresh();

        assertEquals("Updated", controller.getCurrentText());
        assertEquals(1, overlayManager.getActiveOverlays().size());
    }

    @Test
    void disabledControlCanStillShowTooltip() {
        OverlayManager overlayManager = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(new PApplet(), overlayManager);
        Button button = new Button(new PApplet(), "btnDisabled", "Disabled", 50.0F, 50.0F, 100.0F, 40.0F)
                .setTooltip("Disabled reason");
        button.setEnabled(false);

        controller.registerTarget(button);
        controller.showIfMouseOver(50.0F, 50.0F);

        assertSame(button, controller.getActiveTarget());
        assertEquals("Disabled reason", controller.getCurrentText());
        assertEquals(1, overlayManager.getActiveOverlays().size());
    }

    @Test
    void disabledControlDoesNotExecutePointerAction() {
        AtomicInteger clicks = new AtomicInteger();
        Button button = new Button(new PApplet(), "btnDisabled", "Disabled", 50.0F, 50.0F, 100.0F, 40.0F);
        button.setClickListener(clicks::incrementAndGet);
        button.setEnabled(false);

        button.handlePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 50.0F, 50.0F));
        button.handlePointerEvent(new PointerEvent(PointerEvent.Type.RELEASE, 50.0F, 50.0F));

        assertTrue(button.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 50.0F, 50.0F)));
        assertEquals(0, clicks.get());
    }

    @Test
    void disabledButtonBlocksLowerLayerClickThrough() {
        InputManager inputManager = new InputManager();
        AtomicInteger lowerClicks = new AtomicInteger();
        Button disabledTop = new Button(new PApplet(), "btnTop", "Top", 50.0F, 50.0F, 100.0F, 40.0F);
        Button enabledLower = new Button(new PApplet(), "btnLower", "Lower", 50.0F, 50.0F, 100.0F, 40.0F);
        disabledTop.setEnabled(false);
        enabledLower.setClickListener(lowerClicks::incrementAndGet);
        inputManager.registerLayer(new ButtonInputLayer(1, disabledTop));
        inputManager.registerLayer(new ButtonInputLayer(0, enabledLower));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 50.0F, 50.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 50.0F, 50.0F));

        assertEquals(0, lowerClicks.get());
    }

    @Test
    void disabledToggleConsumesPointerGeometry() {
        Toggle toggle = new Toggle(new PApplet(), "tglDisabled", 50.0F, 50.0F, 100.0F, 40.0F);
        toggle.setEnabled(false);

        assertTrue(toggle.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 50.0F, 50.0F)));
    }

    @Test
    void explicitlyDisabledTooltipDoesNotShow() {
        OverlayManager overlayManager = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(new PApplet(), overlayManager);
        Button button = new Button(new PApplet(), "btn", "Button", 50.0F, 50.0F, 100.0F, 40.0F)
                .setTooltip(new Tooltip("Hidden").setEnabled(false));
        button.setEnabled(false);

        controller.registerTarget(button);
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
