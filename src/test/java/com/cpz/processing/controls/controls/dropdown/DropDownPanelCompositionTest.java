package com.cpz.processing.controls.controls.dropdown;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.dropdown.util.DropDownOverlayController;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DropDownPanelCompositionTest {
    private final List<DropDown> dropDowns = new ArrayList<>();
    private final List<OverlayManager> overlayManagers = new ArrayList<>();

    @AfterEach
    void tearDown() {
        for (DropDown dropDown : this.dropDowns) {
            dropDown.dispose();
        }
        for (OverlayManager overlayManager : this.overlayManagers) {
            overlayManager.clearAll();
        }
        this.dropDowns.clear();
        this.overlayManagers.clear();
        clearDropDownControllers();
    }

    @Test
    void dropDownCanBeAddedToPanelAndDrawsAtPanelLocalPosition() {
        RecordingApplet sketch = sketch(640, 480);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, panel, 60.0F, 30.0F, 100.0F, 24.0F);

        assertSame(dropDown, panel.children().get(0));

        panel.draw();

        assertEquals(110.0F, sketch.lastRectX, 0.001F);
        assertEquals(98.0F, sketch.lastRectY, 0.001F);
        assertEquals(100.0F, sketch.lastRectWidth, 0.001F);
        assertEquals(24.0F, sketch.lastRectHeight, 0.001F);
    }

    @Test
    void clickAtEffectiveGlobalPositionOpensMenuAndOverlayUsesGlobalCoordinates() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 160.0F, 110.0F);

        assertTrue(dropDown.isExpanded());
        assertEquals(1, overlayManager.getActiveOverlays().size());

        panel.draw();
        renderOverlays(overlayManager);

        assertEquals(110.0F, sketch.lastRectX, 0.001F);
        assertEquals(122.0F, sketch.lastRectY, 0.001F);
        assertEquals(100.0F, sketch.lastRectWidth, 0.001F);
    }

    @Test
    void absolutePanelAndAbsoluteDropDownKeepLocalAndGlobalGeometryAndSelectThroughOverlay() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        assertBounds(dropDown.getTooltipBounds(), 10.0F, 18.0F, 100.0F, 24.0F);
        assertBounds(globalBounds(panel, dropDown), 110.0F, 98.0F, 100.0F, 24.0F);

        panel.draw();
        assertEquals(110.0F, sketch.lastRectX, 0.001F);
        assertEquals(98.0F, sketch.lastRectY, 0.001F);

        press(inputManager, 160.0F, 110.0F);

        assertTrue(dropDown.isExpanded());
        assertEquals(1, overlayManager.getActiveOverlays().size());

        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(100.0F, 80.0F);

        assertNotNull(listRect);
        assertRect(listRect, 110.0F, 122.0F, 100.0F, 80.0F);

        press(inputManager, 160.0F, 153.0F);

        assertEquals("Beta", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertBounds(globalBounds(panel, dropDown), 110.0F, 98.0F, 100.0F, 24.0F);
    }

    @Test
    void childRelativeXUsesPanelWidthWhileKeepingOtherAxesAbsolute() {
        RecordingApplet sketch = sketch(640, 480);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(
                sketch,
                panel,
                ControlBounds.of(
                        ControlMeasure.relative(0.2F),
                        ControlMeasure.absolute(30.0F),
                        ControlMeasure.absolute(100.0F),
                        ControlMeasure.absolute(24.0F)
                )
        );

        assertCenterAndSize(dropDown.getTooltipBounds(), 40.0F, 30.0F, 100.0F, 24.0F);
        assertCenterAndSize(globalBounds(panel, dropDown), 140.0F, 110.0F, 100.0F, 24.0F);
        assertMeasure(boundsOf(dropDown).x(), MeasureMode.RELATIVE, 0.2F);
    }

    @Test
    void childRelativeYUsesPanelHeightWhileKeepingOtherAxesAbsolute() {
        RecordingApplet sketch = sketch(640, 480);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(
                sketch,
                panel,
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.relative(0.25F),
                        ControlMeasure.absolute(100.0F),
                        ControlMeasure.absolute(24.0F)
                )
        );

        assertCenterAndSize(dropDown.getTooltipBounds(), 60.0F, 30.0F, 100.0F, 24.0F);
        assertCenterAndSize(globalBounds(panel, dropDown), 160.0F, 110.0F, 100.0F, 24.0F);
        assertMeasure(boundsOf(dropDown).y(), MeasureMode.RELATIVE, 0.25F);
    }

    @Test
    void childRelativeWidthUsesPanelHeightByContract() {
        RecordingApplet sketch = sketch(640, 480);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(
                sketch,
                panel,
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.absolute(30.0F),
                        ControlMeasure.relative(0.5F),
                        ControlMeasure.absolute(24.0F)
                )
        );

        assertCenterAndSize(dropDown.getTooltipBounds(), 60.0F, 30.0F, 60.0F, 24.0F);
        assertMeasure(boundsOf(dropDown).width(), MeasureMode.RELATIVE, 0.5F);
    }

    @Test
    void childRelativeHeightUsesPanelHeightByContract() {
        RecordingApplet sketch = sketch(640, 480);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(
                sketch,
                panel,
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.absolute(30.0F),
                        ControlMeasure.absolute(100.0F),
                        ControlMeasure.relative(0.1F)
                )
        );

        assertCenterAndSize(dropDown.getTooltipBounds(), 60.0F, 30.0F, 100.0F, 12.0F);
        assertMeasure(boundsOf(dropDown).height(), MeasureMode.RELATIVE, 0.1F);
    }

    @Test
    void mixedMeasureCombinationsResolvePerAxisAndPreserveOriginalMeasureModes() {
        RecordingApplet sketch = sketch(640, 480);

        assertMixedCase(sketch, new MixedBoundsCase(
                ControlBounds.of(
                        ControlMeasure.relative(0.2F),
                        ControlMeasure.relative(0.25F),
                        ControlMeasure.absolute(90.0F),
                        ControlMeasure.absolute(18.0F)
                ),
                40.0F, 30.0F, 90.0F, 18.0F,
                MeasureMode.RELATIVE, MeasureMode.RELATIVE, MeasureMode.ABSOLUTE, MeasureMode.ABSOLUTE
        ));
        assertMixedCase(sketch, new MixedBoundsCase(
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.absolute(30.0F),
                        ControlMeasure.relative(0.5F),
                        ControlMeasure.relative(0.1F)
                ),
                60.0F, 30.0F, 60.0F, 12.0F,
                MeasureMode.ABSOLUTE, MeasureMode.ABSOLUTE, MeasureMode.RELATIVE, MeasureMode.RELATIVE
        ));
        assertMixedCase(sketch, new MixedBoundsCase(
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.relative(0.25F),
                        ControlMeasure.absolute(90.0F),
                        ControlMeasure.absolute(18.0F)
                ),
                60.0F, 30.0F, 90.0F, 18.0F,
                MeasureMode.ABSOLUTE, MeasureMode.RELATIVE, MeasureMode.ABSOLUTE, MeasureMode.ABSOLUTE
        ));
        assertMixedCase(sketch, new MixedBoundsCase(
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.absolute(30.0F),
                        ControlMeasure.absolute(90.0F),
                        ControlMeasure.relative(0.1F)
                ),
                60.0F, 30.0F, 90.0F, 12.0F,
                MeasureMode.ABSOLUTE, MeasureMode.ABSOLUTE, MeasureMode.ABSOLUTE, MeasureMode.RELATIVE
        ));
        assertMixedCase(sketch, new MixedBoundsCase(
                ControlBounds.of(
                        ControlMeasure.absolute(60.0F),
                        ControlMeasure.absolute(30.0F),
                        ControlMeasure.relative(0.5F),
                        ControlMeasure.absolute(18.0F)
                ),
                60.0F, 30.0F, 60.0F, 18.0F,
                MeasureMode.ABSOLUTE, MeasureMode.ABSOLUTE, MeasureMode.RELATIVE, MeasureMode.ABSOLUTE
        ));
    }

    @Test
    void relativePanelAndAbsoluteChildResolvePanelAgainstCanvasAndOverlayAgainstPanelOffset() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        assertEquals(200.0F, panel.getX(), 0.001F);
        assertEquals(60.0F, panel.getY(), 0.001F);
        assertEquals(300.0F, panel.getWidth(), 0.001F);
        assertEquals(120.0F, panel.getHeight(), 0.001F);
        assertCenterAndSize(globalBounds(panel, dropDown), 260.0F, 90.0F, 100.0F, 24.0F);

        press(inputManager, 260.0F, 90.0F);

        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(100.0F, 80.0F);

        assertNotNull(listRect);
        assertRect(listRect, 210.0F, 102.0F, 100.0F, 80.0F);
    }

    @Test
    void relativePanelAndRelativeDropDownUsePanelDimensionsAndKeepOverlayGlobal() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));
        DropDown dropDown = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "panelRelativeChild",
                List.of("Alpha", "Beta", "Gamma"),
                ControlBounds.relative(0.2F, 0.25F, 0.5F, 0.1F)
        ));
        dropDown.setStyle(style());
        panel.add(dropDown);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        assertEquals(200.0F, panel.getX(), 0.001F);
        assertEquals(60.0F, panel.getY(), 0.001F);
        assertEquals(300.0F, panel.getWidth(), 0.001F);
        assertEquals(120.0F, panel.getHeight(), 0.001F);
        assertCenterAndSize(dropDown.getTooltipBounds(), 60.0F, 30.0F, 60.0F, 12.0F);
        assertCenterAndSize(globalBounds(panel, dropDown), 260.0F, 90.0F, 60.0F, 12.0F);

        press(inputManager, 260.0F, 90.0F);

        assertTrue(dropDown.isExpanded());
        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(60.0F, 60.0F);

        assertNotNull(listRect);
        assertRect(listRect, 230.0F, 96.0F, 60.0F, 60.0F);

        press(inputManager, 260.0F, 126.0F);

        assertEquals("Beta", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void styledPanelDoesNotAffectDropDownOverlayGeometryOrInputPriority() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 70.0F);
        panel.setStyle(new PanelStyle()
                .setBackgroundVisible(true)
                .setBackgroundColor(0xFF20242A)
                .setStrokeVisible(true)
                .setStrokeColor(0xFF6D7682)
                .setStrokeWeight(3.0F)
                .setCornerRadius(12.0F));
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        Button lowerButton = new Button(sketch, "lower", "Lower", 160.0F, 169.0F, 120.0F, 24.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lowerButton.setClickListener(lowerClicks::incrementAndGet);
        inputManager.registerLayer(new PanelInputLayer(0, panel));
        inputManager.registerLayer(new ButtonInputLayer(-1, lowerButton));

        press(inputManager, 160.0F, 110.0F);
        panel.draw();
        renderOverlays(overlayManager);

        assertTrue(dropDown.isExpanded());
        assertEquals(110.0F, sketch.lastRectX, 0.001F);
        assertEquals(122.0F, sketch.lastRectY, 0.001F);
        assertEquals(100.0F, sketch.lastRectWidth, 0.001F);

        press(inputManager, 160.0F, 169.0F);

        assertEquals("Gamma", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, lowerClicks.get());
    }

    @Test
    void optionOutsidePanelBoundsCanBeSelectedWithoutClickThrough() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 70.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        Button lowerButton = new Button(sketch, "lower", "Lower", 160.0F, 169.0F, 120.0F, 24.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lowerButton.setClickListener(lowerClicks::incrementAndGet);
        inputManager.registerLayer(new PanelInputLayer(0, panel));
        inputManager.registerLayer(new ButtonInputLayer(-1, lowerButton));

        press(inputManager, 160.0F, 110.0F);
        press(inputManager, 160.0F, 169.0F);

        assertEquals("Gamma", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertEquals(0, lowerClicks.get());
    }

    @Test
    void outsideClickAndTopOverlayClosePreserveExistingBehavior() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 160.0F, 110.0F);
        press(inputManager, 20.0F, 20.0F);

        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());

        press(inputManager, 160.0F, 110.0F);
        OverlayEntry topOverlay = overlayManager.getTopOverlay().orElseThrow();
        topOverlay.getOnClose().run();

        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void visibleStandaloneOptionTakesPriorityOverOverlappingSiblingBase() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        DropDown first = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "first",
                List.of("Alpha", "Beta", "Gamma"),
                2,
                160.0F,
                110.0F,
                100.0F,
                24.0F
        ));
        DropDown second = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "second",
                List.of("One", "Two"),
                0,
                160.0F,
                132.0F,
                100.0F,
                20.0F
        ));
        first.setStyle(style());
        second.setStyle(style());
        AtomicInteger selectionChanges = new AtomicInteger();
        first.setChangeListener(index -> selectionChanges.incrementAndGet());
        inputManager.registerLayer(new DropDownInputLayer(0, first));
        inputManager.registerLayer(new DropDownInputLayer(0, second));

        press(inputManager, 160.0F, 110.0F);
        press(inputManager, 160.0F, 132.0F);

        assertEquals("Alpha", first.getSelectedItem());
        assertEquals(1, selectionChanges.get());
        assertFalse(first.isExpanded());
        assertFalse(second.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 160.0F, 132.0F));

        assertEquals("Alpha", first.getSelectedItem());
        assertEquals(1, selectionChanges.get());
        assertFalse(second.isExpanded());
    }

    @Test
    void pressOutsideOpenMenuStillTransfersToUncoveredSiblingBase() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        DropDown first = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "first",
                List.of("Alpha", "Beta", "Gamma"),
                0,
                160.0F,
                110.0F,
                100.0F,
                24.0F
        ));
        DropDown second = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "second",
                List.of("One", "Two"),
                0,
                160.0F,
                250.0F,
                100.0F,
                24.0F
        ));
        first.setStyle(style());
        second.setStyle(style());
        inputManager.registerLayer(new DropDownInputLayer(0, first));
        inputManager.registerLayer(new DropDownInputLayer(0, second));

        press(inputManager, 160.0F, 110.0F);
        press(inputManager, 160.0F, 250.0F);

        assertFalse(first.isExpanded());
        assertTrue(second.isExpanded());
        assertEquals(1, overlayManager.getActiveOverlays().size());
    }

    @Test
    void visiblePanelOptionTakesPriorityOverOverlappingStandaloneBase() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown panelDropDown = this.panelDropDown(
                sketch,
                overlayManager,
                inputManager,
                panel,
                60.0F,
                30.0F,
                100.0F,
                24.0F
        );
        panelDropDown.setSelectedIndex(2);
        DropDown standalone = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "standalone",
                List.of("One", "Two"),
                0,
                160.0F,
                132.0F,
                100.0F,
                20.0F
        ));
        standalone.setStyle(style());
        inputManager.registerLayer(new PanelInputLayer(0, panel));
        inputManager.registerLayer(new DropDownInputLayer(-1, standalone));

        press(inputManager, 160.0F, 110.0F);
        press(inputManager, 160.0F, 132.0F);

        assertEquals("Alpha", panelDropDown.getSelectedItem());
        assertFalse(panelDropDown.isExpanded());
        assertFalse(standalone.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void movingPanelUpdatesEffectiveDropDownPositionWhileClosedAndOpen() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        panel.setPosition(140.0F, 110.0F);
        panel.draw();

        assertEquals(150.0F, sketch.lastRectX, 0.001F);
        assertEquals(128.0F, sketch.lastRectY, 0.001F);

        press(inputManager, 200.0F, 140.0F);
        panel.setPosition(180.0F, 140.0F);
        panel.draw();
        renderOverlays(overlayManager);

        assertTrue(dropDown.isExpanded());
        assertEquals(190.0F, sketch.lastRectX, 0.001F);
        assertEquals(182.0F, sketch.lastRectY, 0.001F);
    }

    @Test
    void movingPanelWhileExpandedUpdatesOverlayImmediatelyAndSelectionUsesNewGlobalPosition() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 160.0F, 110.0F);
        panel.setPosition(180.0F, 140.0F);

        assertCenterAndSize(globalBounds(panel, dropDown), 240.0F, 170.0F, 100.0F, 24.0F);
        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(100.0F, 80.0F);

        assertNotNull(listRect);
        assertRect(listRect, 190.0F, 182.0F, 100.0F, 80.0F);

        press(inputManager, 240.0F, 213.0F);

        assertEquals("Beta", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
    }

    @Test
    void resizingPanelRecomputesRelativeChildBoundsClosedAndOpenAndKeepsOverlayInputAligned() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(
                sketch,
                overlayManager,
                inputManager,
                panel,
                ControlBounds.relative(0.5F, 0.25F, 0.5F, 0.1F)
        );
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        assertCenterAndSize(globalBounds(panel, dropDown), 200.0F, 110.0F, 60.0F, 12.0F);

        panel.setSize(300.0F, 180.0F);
        assertCenterAndSize(dropDown.getTooltipBounds(), 150.0F, 45.0F, 90.0F, 18.0F);
        assertCenterAndSize(globalBounds(panel, dropDown), 250.0F, 125.0F, 90.0F, 18.0F);

        panel.draw();
        assertEquals(205.0F, sketch.lastRectX, 0.001F);
        assertEquals(116.0F, sketch.lastRectY, 0.001F);
        assertEquals(90.0F, sketch.lastRectWidth, 0.001F);
        assertEquals(18.0F, sketch.lastRectHeight, 0.001F);

        press(inputManager, 250.0F, 125.0F);
        panel.setSize(360.0F, 220.0F);

        assertCenterAndSize(globalBounds(panel, dropDown), 280.0F, 135.0F, 110.0F, 22.0F);
        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(110.0F, 80.0F);

        assertNotNull(listRect);
        assertRect(listRect, 225.0F, 146.0F, 110.0F, 80.0F);

        press(inputManager, 280.0F, 176.0F);

        assertEquals("Beta", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
    }

    @Test
    void canvasResizeUpdatesRelativeComposedOverlayWhenPanelSynchronizesBeforeOverlayRender() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));
        DropDown dropDown = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "ddResize",
                List.of("Alpha", "Beta", "Gamma"),
                ControlBounds.relative(0.2F, 0.25F, 0.5F, 0.1F)
        ));
        dropDown.setStyle(style());
        panel.add(dropDown);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 260.0F, 90.0F);
        sketch.width = 1000;
        sketch.height = 800;

        panel.draw();
        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(80.0F, 60.0F);

        assertNotNull(listRect);
        assertCenterAndSize(globalBounds(panel, dropDown), 330.0F, 120.0F, 80.0F, 16.0F);
        assertRect(listRect, 290.0F, 128.0F, 80.0F, 60.0F);
    }

    @Test
    void canvasResizeBeforePanelSynchronizationKeepsPreviousOverlaySnapshotByContract() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));
        DropDown dropDown = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "ddResizeContract",
                List.of("Alpha", "Beta", "Gamma"),
                ControlBounds.relative(0.2F, 0.25F, 0.5F, 0.1F)
        ));
        dropDown.setStyle(style());
        panel.add(dropDown);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 260.0F, 90.0F);
        sketch.width = 1000;
        sketch.height = 800;

        sketch.clearRectHistory();
        renderOverlays(overlayManager);
        RectSnapshot listRect = sketch.findRect(60.0F, 60.0F);

        assertNotNull(listRect);
        assertRect(listRect, 230.0F, 96.0F, 60.0F, 60.0F);
    }

    @Test
    void invisibleOrDisabledPanelPreventsInteractionAndClosesOpenOverlay() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        panel.setVisible(false);
        panel.draw();
        press(inputManager, 160.0F, 110.0F);

        assertEquals(0, sketch.rectCalls);
        assertFalse(dropDown.isExpanded());
        assertFalse(dropDown.isVisible());

        panel.setVisible(true);
        press(inputManager, 160.0F, 110.0F);
        assertTrue(dropDown.isExpanded());

        panel.setEnabled(false);

        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
        press(inputManager, 160.0F, 110.0F);
        assertFalse(dropDown.isExpanded());
        assertFalse(dropDown.isEnabled());
    }

    @Test
    void removeAndClearCloseOverlayWithoutLeavingResidualStateAndControlCanBeReused() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 160.0F, 110.0F);
        assertTrue(panel.remove(dropDown));

        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());

        panel.add(dropDown);
        press(inputManager, 160.0F, 110.0F);
        assertTrue(dropDown.isExpanded());

        panel.clear();

        assertEquals(0, panel.children().size());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void removingRelativeDropDownClearsPanelContextAndAllowsStandaloneReuseAgainstCanvas() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        DropDown dropDown = this.panelDropDown(
                sketch,
                overlayManager,
                inputManager,
                panel,
                ControlBounds.relative(0.25F, 0.5F, 0.5F, 0.1F)
        );
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        press(inputManager, 200.0F, 180.0F);
        assertTrue(panel.remove(dropDown));

        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
        assertCenterAndSize(dropDown.getTooltipBounds(), 200.0F, 300.0F, 300.0F, 60.0F);
        assertTrue(dropDown.canConsumePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 200.0F, 300.0F)));

        inputManager.registerLayer(new DropDownInputLayer(1, dropDown));
        press(inputManager, 200.0F, 300.0F);
        press(inputManager, 200.0F, 360.0F);

        assertEquals("Beta", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
    }

    @Test
    void standaloneDropDownContinuesWorking() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        DropDown dropDown = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "standalone",
                List.of("One", "Two", "Three"),
                160.0F,
                110.0F,
                120.0F,
                24.0F
        ));
        dropDown.setStyle(style());
        inputManager.registerLayer(new DropDownInputLayer(0, dropDown));

        press(inputManager, 160.0F, 110.0F);
        press(inputManager, 160.0F, 153.0F);

        assertEquals("Two", dropDown.getSelectedItem());
        assertFalse(dropDown.isExpanded());
        assertEquals(0, overlayManager.getActiveOverlays().size());
    }

    @Test
    void existingPanelCompatibleControlsStillRouteNormallyAlongsideDropDown() {
        RecordingApplet sketch = sketch(640, 480);
        InputManager inputManager = new InputManager();
        OverlayManager overlayManager = this.overlayManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 140.0F);
        DropDown dropDown = this.panelDropDown(sketch, overlayManager, inputManager, panel, 60.0F, 30.0F, 100.0F, 24.0F);
        Button button = new Button(sketch, "childButton", "Click", 170.0F, 90.0F, 100.0F, 30.0F);
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        panel.add(button);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        pressRelease(inputManager, 270.0F, 170.0F);

        assertEquals(1, clicks.get());
        assertFalse(dropDown.isExpanded());
    }

    private DropDown panelDropDown(PApplet sketch, Panel panel, float x, float y, float width, float height) {
        return this.panelDropDown(sketch, this.overlayManager(), new InputManager(), panel, x, y, width, height);
    }

    private DropDown panelDropDown(PApplet sketch, Panel panel, ControlBounds bounds) {
        return this.panelDropDown(sketch, this.overlayManager(), new InputManager(), panel, bounds);
    }

    private DropDown panelDropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, Panel panel, float x, float y, float width, float height) {
        DropDown dropDown = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "panelDropDown",
                List.of("Alpha", "Beta", "Gamma", "Delta"),
                x,
                y,
                width,
                height
        ));
        dropDown.setStyle(style());
        panel.add(dropDown);
        return dropDown;
    }

    private DropDown panelDropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, Panel panel, ControlBounds bounds) {
        DropDown dropDown = this.track(new DropDown(
                sketch,
                overlayManager,
                inputManager,
                "panelDropDown",
                List.of("Alpha", "Beta", "Gamma", "Delta"),
                bounds
        ));
        dropDown.setStyle(style());
        panel.add(dropDown);
        return dropDown;
    }

    private DropDown track(DropDown dropDown) {
        this.dropDowns.add(dropDown);
        return dropDown;
    }

    private OverlayManager overlayManager() {
        OverlayManager overlayManager = new OverlayManager();
        this.overlayManagers.add(overlayManager);
        return overlayManager;
    }

    private static DefaultDropDownStyle style() {
        com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig styleConfig =
                new com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig();
        styleConfig.itemHeight = 20.0F;
        styleConfig.maxVisibleItems = 8;
        styleConfig.textSize = 12.0F;
        return new DefaultDropDownStyle(styleConfig);
    }

    private static void renderOverlays(OverlayManager overlayManager) {
        for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private static void press(InputManager inputManager, float x, float y) {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, x, y));
    }

    private static void pressRelease(InputManager inputManager, float x, float y) {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, x, y));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, x, y));
    }

    private static RecordingApplet sketch(int width, int height) {
        RecordingApplet sketch = new RecordingApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static TooltipBounds globalBounds(Panel panel, DropDown dropDown) {
        return panel.tooltipTarget(dropDown).getTooltipBounds();
    }

    private static void assertBounds(TooltipBounds bounds, float x, float y, float width, float height) {
        assertEquals(x, bounds.x(), 0.001F);
        assertEquals(y, bounds.y(), 0.001F);
        assertEquals(width, bounds.width(), 0.001F);
        assertEquals(height, bounds.height(), 0.001F);
    }

    private static void assertCenterAndSize(TooltipBounds bounds, float centerX, float centerY, float width, float height) {
        assertEquals(centerX, bounds.x() + bounds.width() * 0.5F, 0.001F);
        assertEquals(centerY, bounds.y() + bounds.height() * 0.5F, 0.001F);
        assertEquals(width, bounds.width(), 0.001F);
        assertEquals(height, bounds.height(), 0.001F);
    }

    private static void assertRect(RectSnapshot rect, float x, float y, float width, float height) {
        assertEquals(x, rect.x, 0.001F);
        assertEquals(y, rect.y, 0.001F);
        assertEquals(width, rect.width, 0.001F);
        assertEquals(height, rect.height, 0.001F);
    }

    private static void assertMeasure(ControlMeasure measure, MeasureMode mode, float value) {
        assertEquals(mode, measure.mode());
        assertEquals(value, measure.value(), 0.001F);
    }

    private static ControlBounds boundsOf(DropDown dropDown) {
        try {
            Field field = DropDown.class.getDeclaredField("bounds");
            field.setAccessible(true);
            return (ControlBounds) field.get(dropDown);
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    private static void assertMixedCase(RecordingApplet sketch, MixedBoundsCase testCase) {
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 200.0F, 120.0F);
        DropDown dropDown = new DropDown(
                sketch,
                new OverlayManager(),
                new InputManager(),
                "mixed",
                List.of("Alpha", "Beta"),
                testCase.bounds
        );
        dropDown.setStyle(style());
        panel.add(dropDown);

        assertCenterAndSize(dropDown.getTooltipBounds(), testCase.localCenterX, testCase.localCenterY, testCase.width, testCase.height);
        ControlBounds bounds = boundsOf(dropDown);
        assertMeasure(bounds.x(), testCase.xMode, testCase.bounds.x().value());
        assertMeasure(bounds.y(), testCase.yMode, testCase.bounds.y().value());
        assertMeasure(bounds.width(), testCase.widthMode, testCase.bounds.width().value());
        assertMeasure(bounds.height(), testCase.heightMode, testCase.bounds.height().value());

        dropDown.dispose();
    }

    @SuppressWarnings("unchecked")
    private static void clearDropDownControllers() {
        try {
            Field field = DropDownOverlayController.class.getDeclaredField("CONTROLLERS");
            field.setAccessible(true);
            List<DropDownOverlayController> controllers = new ArrayList<>((List<DropDownOverlayController>) field.get(null));
            for (DropDownOverlayController controller : controllers) {
                controller.dispose();
            }
        } catch (ReflectiveOperationException ex) {
            throw new AssertionError(ex);
        }
    }

    private static final class MixedBoundsCase {
        private final ControlBounds bounds;
        private final float localCenterX;
        private final float localCenterY;
        private final float width;
        private final float height;
        private final MeasureMode xMode;
        private final MeasureMode yMode;
        private final MeasureMode widthMode;
        private final MeasureMode heightMode;

        private MixedBoundsCase(
                ControlBounds bounds,
                float localCenterX,
                float localCenterY,
                float width,
                float height,
                MeasureMode xMode,
                MeasureMode yMode,
                MeasureMode widthMode,
                MeasureMode heightMode
        ) {
            this.bounds = bounds;
            this.localCenterX = localCenterX;
            this.localCenterY = localCenterY;
            this.width = width;
            this.height = height;
            this.xMode = xMode;
            this.yMode = yMode;
            this.widthMode = widthMode;
            this.heightMode = heightMode;
        }
    }

    private static final class RectSnapshot {
        private final float x;
        private final float y;
        private final float width;
        private final float height;

        private RectSnapshot(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private static final class RecordingApplet extends PApplet {
        private final Deque<float[]> translationStack = new ArrayDeque<>();
        private final List<RectSnapshot> rectSnapshots = new ArrayList<>();
        private float translateX;
        private float translateY;
        private float lastRectX;
        private float lastRectY;
        private float lastRectWidth;
        private float lastRectHeight;
        private int rectCalls;

        @Override
        public void pushMatrix() {
            this.translationStack.push(new float[]{this.translateX, this.translateY});
        }

        @Override
        public void popMatrix() {
            float[] previous = this.translationStack.pop();
            this.translateX = previous[0];
            this.translateY = previous[1];
        }

        @Override
        public void translate(float x, float y) {
            this.translateX += x;
            this.translateY += y;
        }

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void rectMode(int mode) {
        }

        @Override
        public void stroke(int rgb) {
        }

        @Override
        public void strokeWeight(float weight) {
        }

        @Override
        public void fill(int rgb) {
        }

        @Override
        public void noStroke() {
        }

        @Override
        public void textAlign(int horiz, int vert) {
        }

        @Override
        public void text(String str, float x, float y) {
        }

        @Override
        public void beginShape() {
        }

        @Override
        public void vertex(float x, float y) {
        }

        @Override
        public void endShape(int mode) {
        }

        @Override
        public void textSize(float size) {
        }

        @Override
        public void rect(float a, float b, float c, float d, float r) {
            this.rectCalls++;
            this.lastRectX = a + this.translateX;
            this.lastRectY = b + this.translateY;
            this.lastRectWidth = c;
            this.lastRectHeight = d;
            this.rectSnapshots.add(new RectSnapshot(this.lastRectX, this.lastRectY, c, d));
        }

        private void clearRectHistory() {
            this.rectSnapshots.clear();
        }

        private RectSnapshot findRect(float width, float height) {
            for (RectSnapshot snapshot : this.rectSnapshots) {
                if (Math.abs(snapshot.width - width) <= 0.001F
                        && Math.abs(snapshot.height - height) <= 0.001F) {
                    return snapshot;
                }
            }
            return null;
        }
    }
}
