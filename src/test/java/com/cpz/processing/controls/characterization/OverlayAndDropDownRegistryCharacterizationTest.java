package com.cpz.processing.controls.characterization;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.notification.NotificationManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipArea;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterizes producer/OverlayManager synchronization and DropDown
 * coordination boundaries.
 */
class OverlayAndDropDownRegistryCharacterizationTest {
    @Test
    void openDropDownVisibleOptionHasPriorityOverOverlappingLowerControl() {
        Host host = new Host();
        DropDown dropDown = host.dropDown("dropDown", 120.0F, 80.0F);
        Button lower = new Button(host.sketch, "lower", "Lower", 120.0F, 146.0F, 120.0F, 30.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lower.setClickListener(lowerClicks::incrementAndGet);
        host.input.registerLayer(new DropDownInputLayer(0, dropDown));
        host.input.registerLayer(new ButtonInputLayer(-1, lower));
        try {
            click(host.input, 120.0F, 80.0F);
            assertTrue(dropDown.isExpanded());

            click(host.input, 120.0F, 146.0F);

            assertEquals("Two", dropDown.getSelectedItem());
            assertFalse(dropDown.isExpanded());
            assertEquals(0, lowerClicks.get());
        } finally {
            dropDown.dispose();
        }
    }

    @Test
    void normalDropDownCloseSynchronizesProducerOverlayAndAllowsReopen() {
        Host host = new Host();
        DropDown dropDown = host.dropDown("dropDown", 120.0F, 80.0F);
        host.input.registerLayer(new DropDownInputLayer(0, dropDown));
        try {
            click(host.input, 120.0F, 80.0F);
            assertTrue(dropDown.isExpanded());
            assertEquals(1, host.overlays.getActiveOverlays().size());

            OverlayEntry entry = host.overlays.getTopOverlay().orElseThrow();
            entry.getOnClose().run();
            assertFalse(dropDown.isExpanded());
            assertEquals(0, host.overlays.getActiveOverlays().size());

            click(host.input, 120.0F, 80.0F);
            assertTrue(dropDown.isExpanded());
            assertEquals(1, host.overlays.getActiveOverlays().size());
        } finally {
            dropDown.dispose();
        }
    }

    @Test
    void clearAllClosesDropDownWithoutInvisibleCaptureAndAllowsReuse() {
        Host host = new Host();
        DropDown dropDown = host.dropDown("dropDown", 120.0F, 80.0F);
        Button lower = new Button(host.sketch, "lower", "Lower", 320.0F, 220.0F, 120.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lower.setClickListener(lowerClicks::incrementAndGet);
        host.input.registerLayer(new DropDownInputLayer(0, dropDown));
        host.input.registerLayer(new ButtonInputLayer(-1, lower));
        try {
            click(host.input, 120.0F, 80.0F);
            assertTrue(dropDown.isExpanded());
            assertEquals(1, host.overlays.getActiveOverlays().size());

            host.overlays.clearAll();

            assertEquals(0, host.overlays.getActiveOverlays().size());
            assertFalse(dropDown.isExpanded());
            assertFalse(dropDown.isFocused());

            click(host.input, 320.0F, 220.0F);

            assertEquals(1, lowerClicks.get(),
                    "a globally closed DropDown must not retain an invisible capture layer");

            click(host.input, 120.0F, 80.0F);
            assertTrue(dropDown.isExpanded());
            assertEquals(1, host.overlays.getActiveOverlays().size());
        } finally {
            dropDown.dispose();
        }
    }

    @Test
    void clearAllHidesTooltipAndAllowsRegistrationAgain() {
        PApplet sketch = sketch();
        OverlayManager overlays = new OverlayManager();
        TooltipOverlayController controller = new TooltipOverlayController(sketch, overlays);
        TooltipArea area = new TooltipArea(0.0F, 0.0F, 100.0F, 100.0F).setTooltip("Tooltip");
        controller.registerTarget(area);

        controller.showIfMouseOver(50.0F, 50.0F);
        assertEquals(1, overlays.getActiveOverlays().size());

        overlays.clearAll();
        assertEquals(0, overlays.getActiveOverlays().size());

        controller.showIfMouseOver(50.0F, 50.0F);
        assertEquals(1, overlays.getActiveOverlays().size());

        overlays.clearAll();
        overlays.clearAll();
        controller.showIfMouseOver(50.0F, 50.0F);

        assertEquals(1, overlays.getActiveOverlays().size(),
                "repeated global closure must leave the tooltip reusable");
        controller.dispose();
    }

    @Test
    void clearAllClearsNotificationsAndAllowsNewNotification() {
        OverlayManager overlays = new OverlayManager();
        NotificationManager notifications = new NotificationManager(sketch(), overlays);

        notifications.info("Before clearAll");
        assertEquals(1, notifications.size());
        assertEquals(1, overlays.getActiveOverlays().size());

        overlays.clearAll();
        assertEquals(0, notifications.size());
        assertEquals(0, overlays.getActiveOverlays().size(),
                "producer state and overlay registry must be empty together");

        overlays.clearAll();
        notifications.success("After clearAll");

        assertEquals(1, notifications.size());
        assertEquals(1, overlays.getActiveOverlays().size());
        notifications.dispose();
    }

    @Test
    void clearAllCoordinatesMixedProducersAndLeavesAllReusable() {
        Host host = new Host();
        DropDown dropDown = host.dropDown("dropDown", 120.0F, 80.0F);
        TooltipOverlayController tooltips = new TooltipOverlayController(host.sketch, host.overlays);
        TooltipArea area = new TooltipArea(300.0F, 80.0F, 100.0F, 100.0F).setTooltip("Tooltip");
        NotificationManager notifications = new NotificationManager(host.sketch, host.overlays);
        host.input.registerLayer(new DropDownInputLayer(0, dropDown));
        tooltips.registerTarget(area);
        try {
            click(host.input, 120.0F, 80.0F);
            tooltips.showIfMouseOver(320.0F, 100.0F);
            notifications.info("Before clearAll");
            assertEquals(3, host.overlays.getActiveOverlays().size());

            host.overlays.clearAll();

            assertTrue(host.overlays.getActiveOverlays().isEmpty());
            assertFalse(dropDown.isExpanded());
            assertFalse(dropDown.isFocused());
            assertTrue(notifications.isEmpty());

            tooltips.showIfMouseOver(320.0F, 100.0F);
            notifications.success("After clearAll");
            click(host.input, 120.0F, 80.0F);

            assertTrue(dropDown.isExpanded());
            assertEquals(1, notifications.size());
            assertEquals(3, host.overlays.getActiveOverlays().size());
        } finally {
            host.overlays.clearAll();
            dropDown.dispose();
            tooltips.dispose();
            notifications.dispose();
        }
    }

    @Test
    void eventFromOneHostDoesNotTransferToDropDownInAnotherHost() {
        Host firstHost = new Host();
        Host secondHost = new Host();
        DropDown first = firstHost.dropDown("first", 100.0F, 80.0F);
        DropDown second = secondHost.dropDown("second", 320.0F, 220.0F);
        firstHost.input.registerLayer(new DropDownInputLayer(0, first));
        secondHost.input.registerLayer(new DropDownInputLayer(0, second));
        try {
            click(firstHost.input, 100.0F, 80.0F);
            assertTrue(first.isExpanded());
            assertFalse(second.isExpanded());

            firstHost.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 320.0F, 220.0F));

            assertFalse(first.isExpanded(),
                    "the outside press still closes the dropdown in its own routing scope");
            assertFalse(second.isExpanded());
            assertEquals(0, firstHost.overlays.getActiveOverlays().size());
            assertEquals(0, secondHost.overlays.getActiveOverlays().size());
        } finally {
            first.dispose();
            second.dispose();
        }
    }

    @Test
    void differentHostsCanKeepDropdownsOpenAndClearIndependently() {
        Host firstHost = new Host();
        Host secondHost = new Host();
        DropDown first = firstHost.dropDown("first", 100.0F, 80.0F);
        DropDown second = secondHost.dropDown("second", 320.0F, 220.0F);
        firstHost.input.registerLayer(new DropDownInputLayer(0, first));
        secondHost.input.registerLayer(new DropDownInputLayer(0, second));
        try {
            click(firstHost.input, 100.0F, 80.0F);
            click(secondHost.input, 320.0F, 220.0F);
            assertTrue(first.isExpanded());
            assertTrue(second.isExpanded());

            firstHost.overlays.clearAll();

            assertFalse(first.isExpanded());
            assertTrue(second.isExpanded());
            assertTrue(firstHost.overlays.getActiveOverlays().isEmpty());
            assertEquals(1, secondHost.overlays.getActiveOverlays().size());
        } finally {
            first.dispose();
            second.dispose();
        }
    }

    @Test
    void sameInputManagerTransfersOpenStateAndFocusBetweenSiblings() {
        Host host = new Host();
        DropDown first = host.dropDown("first", 100.0F, 80.0F);
        DropDown second = host.dropDown("second", 320.0F, 220.0F);
        host.input.registerLayer(new DropDownInputLayer(0, first));
        host.input.registerLayer(new DropDownInputLayer(0, second));
        try {
            click(host.input, 100.0F, 80.0F);
            assertTrue(first.isExpanded());
            assertTrue(first.isFocused());

            host.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 320.0F, 220.0F));

            assertFalse(first.isExpanded());
            assertFalse(first.isFocused());
            assertTrue(second.isExpanded());
            assertTrue(second.isFocused());
            assertEquals(1, host.overlays.getActiveOverlays().size());
        } finally {
            first.dispose();
            second.dispose();
        }
    }

    @Test
    void disposeRemovesDropDownFromSameHostCoordination() {
        Host host = new Host();
        DropDown first = host.dropDown("first", 100.0F, 80.0F);
        DropDown second = host.dropDown("second", 320.0F, 220.0F);
        host.input.registerLayer(new DropDownInputLayer(0, first));
        host.input.registerLayer(new DropDownInputLayer(0, second));
        try {
            second.dispose();
            click(host.input, 100.0F, 80.0F);
            host.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 320.0F, 220.0F));

            assertFalse(first.isExpanded());
            assertFalse(second.isExpanded(),
                    "a disposed sibling must not participate in same-host transfer");
        } finally {
            first.dispose();
            second.dispose();
        }
    }

    @Test
    void unregisteredLayerLeavesCoordinationAndCanRejoinItsOriginalManager() {
        Host host = new Host();
        DropDown first = host.dropDown("first", 100.0F, 80.0F);
        DropDown second = host.dropDown("second", 320.0F, 220.0F);
        DropDownInputLayer firstLayer = new DropDownInputLayer(0, first);
        DropDownInputLayer secondLayer = new DropDownInputLayer(0, second);
        host.input.registerLayer(firstLayer);
        host.input.registerLayer(secondLayer);
        try {
            host.input.unregisterLayer(secondLayer);
            click(host.input, 100.0F, 80.0F);
            host.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 320.0F, 220.0F));

            assertFalse(first.isExpanded());
            assertFalse(second.isExpanded());

            host.input.registerLayer(secondLayer);
            click(host.input, 100.0F, 80.0F);
            host.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 320.0F, 220.0F));

            assertFalse(first.isExpanded());
            assertTrue(second.isExpanded(),
                    "re-registering the layer in its constructor-owned manager restores coordination");
        } finally {
            first.dispose();
            second.dispose();
        }
    }

    @Test
    void panelDescendantCoordinatesOnlyInsideItsInputManager() {
        Host panelHost = new Host();
        Host foreignHost = new Host();
        Panel panel = new Panel(panelHost.sketch, "panel", 50.0F, 40.0F, 220.0F, 140.0F);
        DropDown panelDropDown = panelHost.dropDown("panelDropDown", 60.0F, 30.0F);
        DropDown foreign = foreignHost.dropDown("foreign", 360.0F, 240.0F);
        panel.add(panelDropDown);
        panelHost.input.registerLayer(new PanelInputLayer(0, panel));
        foreignHost.input.registerLayer(new DropDownInputLayer(0, foreign));
        try {
            click(panelHost.input, 110.0F, 70.0F);
            assertTrue(panelDropDown.isExpanded());

            panelHost.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 360.0F, 240.0F));

            assertFalse(panelDropDown.isExpanded(),
                    "the outside press still closes the panel descendant in its own scope");
            assertFalse(foreign.isExpanded());
        } finally {
            panelDropDown.dispose();
            foreign.dispose();
        }
    }

    @Test
    void removingPanelDescendantReleasesItFromSameHostCoordination() {
        Host host = new Host();
        Panel panel = new Panel(host.sketch, "panel", 50.0F, 40.0F, 220.0F, 140.0F);
        DropDown removed = host.dropDown("removed", 60.0F, 30.0F);
        DropDown remaining = host.dropDown("remaining", 360.0F, 240.0F);
        panel.add(removed);
        host.input.registerLayer(new PanelInputLayer(0, panel));
        host.input.registerLayer(new DropDownInputLayer(0, remaining));
        try {
            assertTrue(panel.remove(removed));
            click(host.input, 360.0F, 240.0F);
            assertTrue(remaining.isExpanded());

            host.input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 110.0F, 70.0F));

            assertFalse(remaining.isExpanded());
            assertFalse(removed.isExpanded(),
                    "a child removed from a routed panel must no longer receive sibling transfers");
        } finally {
            removed.dispose();
            remaining.dispose();
        }
    }

    @Test
    void constructorOwnedInputManagerRejectsPartialMigrationWithoutContamination() {
        Host host = new Host();
        InputManager foreignInput = new InputManager();
        DropDown dropDown = host.dropDown("dropDown", 120.0F, 80.0F);
        DropDownInputLayer layer = new DropDownInputLayer(0, dropDown);
        try {
            assertThrows(IllegalStateException.class, () -> foreignInput.registerLayer(layer));

            foreignInput.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 120.0F, 80.0F));
            assertFalse(dropDown.isExpanded());

            host.input.registerLayer(layer);
            click(host.input, 120.0F, 80.0F);

            assertTrue(dropDown.isExpanded(),
                    "a failed foreign registration must not prevent registration in the constructor-owned manager");
        } finally {
            dropDown.dispose();
        }
    }

    @Test
    void disposingOpenDropDownRemovesRegistrationsButCurrentlyRetainsExpandedFlag() {
        Host host = new Host();
        DropDown dropDown = host.dropDown("dropDown", 120.0F, 80.0F);
        host.input.registerLayer(new DropDownInputLayer(0, dropDown));

        click(host.input, 120.0F, 80.0F);
        assertTrue(dropDown.isExpanded());
        assertEquals(1, host.overlays.getActiveOverlays().size());

        dropDown.dispose();

        assertEquals(0, host.overlays.getActiveOverlays().size());
        assertTrue(dropDown.isExpanded(),
                "characterization only: dispose is terminal cleanup, not a reusable close operation");
    }

    private static PApplet sketch() {
        PApplet sketch = new PApplet();
        sketch.width = 640;
        sketch.height = 480;
        return sketch;
    }

    private static void click(InputManager input, float x, float y) {
        input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, x, y));
        input.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, x, y));
    }

    private static final class Host {
        private final PApplet sketch = sketch();
        private final InputManager input = new InputManager();
        private final OverlayManager overlays = new OverlayManager();

        private DropDown dropDown(String code, float x, float y) {
            return new DropDown(
                    this.sketch,
                    this.overlays,
                    this.input,
                    code,
                    List.of("One", "Two", "Three"),
                    0,
                    x,
                    y,
                    120.0F,
                    30.0F
            );
        }
    }
}
