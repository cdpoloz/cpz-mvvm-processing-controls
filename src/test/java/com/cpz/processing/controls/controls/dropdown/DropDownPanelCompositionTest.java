package com.cpz.processing.controls.controls.dropdown;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    private static final class RecordingApplet extends PApplet {
        private final Deque<float[]> translationStack = new ArrayDeque<>();
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
        }
    }
}
