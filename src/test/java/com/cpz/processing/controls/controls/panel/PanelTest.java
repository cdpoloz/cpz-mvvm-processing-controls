package com.cpz.processing.controls.controls.panel;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import com.cpz.processing.controls.core.theme.LightTheme;
import com.cpz.processing.controls.core.theme.ThemeSnapshot;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PanelTest {
    @Test
    void panelImplementsControl() {
        Control panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);

        assertEquals("panel", panel.getCode());
    }

    @Test
    void defaultStyleKeepsPanelVisuallyTransparent() {
        ThemeSnapshot light = new ThemeSnapshot(new LightTheme());
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);

        assertNotNull(panel.getStyle());
        assertFalse(panel.isBackgroundVisible());
        assertFalse(panel.isStrokeVisible());
        assertEquals(PanelStyle.DEFAULT_STROKE_WEIGHT, panel.getStrokeWeight());
        assertEquals(PanelStyle.DEFAULT_CORNER_RADIUS, panel.getCornerRadius());
        assertEquals(light.tokens.surface, panel.getBackgroundColor());
        assertEquals(light.tokens.border, panel.getStrokeColor());
    }

    @Test
    void directStyleSettersUpdateRuntimeStyle() {
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);

        panel.setBackgroundColor(0xFF20242A);
        panel.setBackgroundVisible(true);
        panel.setStrokeColor(0xFF6D7682);
        panel.setStrokeVisible(true);
        panel.setStrokeWeight(2.5F);
        panel.setCornerRadius(10.0F);

        assertEquals(0xFF20242A, panel.getBackgroundColor());
        assertTrue(panel.isBackgroundVisible());
        assertEquals(0xFF6D7682, panel.getStrokeColor());
        assertTrue(panel.isStrokeVisible());
        assertEquals(2.5F, panel.getStrokeWeight());
        assertEquals(10.0F, panel.getCornerRadius());
    }

    @Test
    void styleObjectCanBeAssignedAndNullResetsToDefaults() {
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        PanelStyle style = new PanelStyle()
                .setBackgroundColor(0xFF101820)
                .setBackgroundVisible(true)
                .setStrokeColor(0xFF80A0C0)
                .setStrokeVisible(true)
                .setStrokeWeight(3.0F)
                .setCornerRadius(12.0F);

        panel.setStyle(style);

        assertSame(style, panel.getStyle());
        assertEquals(0xFF101820, panel.getBackgroundColor());
        assertEquals(0xFF80A0C0, panel.getStrokeColor());
        assertEquals(3.0F, panel.getStrokeWeight());
        assertEquals(12.0F, panel.getCornerRadius());

        panel.setStyle(null);

        assertNotNull(panel.getStyle());
        assertFalse(panel.isBackgroundVisible());
        assertFalse(panel.isStrokeVisible());
        assertEquals(PanelStyle.DEFAULT_STROKE_WEIGHT, panel.getStrokeWeight());
        assertEquals(PanelStyle.DEFAULT_CORNER_RADIUS, panel.getCornerRadius());
    }

    @Test
    void invalidStyleNumbersAreHandledConsistently() {
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);

        panel.setStrokeWeight(-2.0F);
        panel.setCornerRadius(-4.0F);

        assertEquals(0.0F, panel.getStrokeWeight());
        assertEquals(0.0F, panel.getCornerRadius());
        assertThrows(IllegalArgumentException.class, () -> panel.setStrokeWeight(Float.NaN));
        assertThrows(IllegalArgumentException.class, () -> panel.setStrokeWeight(Float.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> panel.setCornerRadius(Float.NaN));
        assertThrows(IllegalArgumentException.class, () -> panel.setCornerRadius(Float.NEGATIVE_INFINITY));
    }

    @Test
    void drawUsesConfiguredBackgroundStrokeWeightAndCornerRadius() {
        RecordingApplet sketch = new RecordingApplet();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        panel.setBackgroundColor(0xFF20242A);
        panel.setBackgroundVisible(true);
        panel.setStrokeColor(0xFF6D7682);
        panel.setStrokeVisible(true);
        panel.setStrokeWeight(2.0F);
        panel.setCornerRadius(10.0F);

        panel.draw();

        assertEquals(1, sketch.pushStyleCalls);
        assertEquals(1, sketch.popStyleCalls);
        assertEquals(1, sketch.rectModeCalls);
        assertEquals(1, sketch.fillCalls);
        assertEquals(0xFF20242A, sketch.lastFillColor);
        assertEquals(1, sketch.strokeCalls);
        assertEquals(0xFF6D7682, sketch.lastStrokeColor);
        assertEquals(2.0F, sketch.lastStrokeWeight);
        assertEquals(1, sketch.rectCalls);
        assertEquals(100.0F, sketch.lastRectX);
        assertEquals(80.0F, sketch.lastRectY);
        assertEquals(240.0F, sketch.lastRectWidth);
        assertEquals(160.0F, sketch.lastRectHeight);
        assertEquals(10.0F, sketch.lastRectRadius);
        assertEquals(1, sketch.pushMatrixCalls);
        assertEquals(1, sketch.popMatrixCalls);
    }

    @Test
    void drawSupportsHiddenBackgroundOrHiddenStroke() {
        RecordingApplet sketch = new RecordingApplet();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 160.0F);

        panel.setBackgroundVisible(false);
        panel.setStrokeVisible(true);
        panel.draw();

        assertEquals(1, sketch.noFillCalls);
        assertEquals(1, sketch.strokeCalls);

        sketch.resetRenderCalls();
        panel.setBackgroundVisible(true);
        panel.setStrokeVisible(false);
        panel.draw();

        assertEquals(1, sketch.fillCalls);
        assertEquals(1, sketch.noStrokeCalls);
        assertEquals(0, sketch.strokeCalls);
    }

    @Test
    void childrenDrawWhenBackgroundAndStrokeAreHidden() {
        RecordingApplet sketch = new RecordingApplet();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        RecordingControl child = new RecordingControl("child");
        panel.add(child);

        panel.setBackgroundVisible(false);
        panel.setStrokeVisible(false);
        panel.draw();

        assertEquals(0, sketch.rectCalls);
        assertEquals(0, sketch.pushStyleCalls);
        assertEquals(1, child.drawCalls);
        assertEquals(100.0F, sketch.lastTranslateX);
        assertEquals(80.0F, sketch.lastTranslateY);
    }

    @Test
    void styleChangesDoNotChangeBoundsOrChildCoordinates() {
        RecordingApplet sketch = new RecordingApplet();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        RecordingControl child = new RecordingControl("child");
        panel.add(child);

        panel.setBackgroundVisible(true);
        panel.setStrokeVisible(true);
        panel.setStrokeWeight(4.0F);
        panel.setCornerRadius(16.0F);
        panel.draw();

        assertEquals(100.0F, panel.getX());
        assertEquals(80.0F, panel.getY());
        assertEquals(240.0F, panel.getWidth());
        assertEquals(160.0F, panel.getHeight());
        assertEquals(0, child.setPositionCalls);
        assertEquals(1, child.drawCalls);
        assertEquals(100.0F, sketch.lastTranslateX);
        assertEquals(80.0F, sketch.lastTranslateY);
    }

    @Test
    void clickAtGlobalCoordinatesActivatesLocalChild() {
        InputManager inputManager = new InputManager();
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        Button button = new Button(new PApplet(), "btnChild", "Child", 10.0F, 20.0F, 60.0F, 30.0F);
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        panel.add(button);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 110.0F, 100.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 110.0F, 100.0F));

        assertEquals(1, clicks.get());
    }

    @Test
    void clickOutsidePanelDoesNotConsumeOrActivateChild() {
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        Button button = new Button(new PApplet(), "btnChild", "Child", -90.0F, -60.0F, 60.0F, 30.0F);
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        panel.add(button);
        PanelInputLayer layer = new PanelInputLayer(0, panel);

        boolean consumed = layer.handlePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 10.0F, 20.0F));
        layer.handlePointerEvent(new PointerEvent(PointerEvent.Type.RELEASE, 10.0F, 20.0F));

        assertFalse(consumed);
        assertEquals(0, clicks.get());
    }

    @Test
    void invisiblePanelDoesNotDrawOrRouteInput() {
        RecordingApplet sketch = new RecordingApplet();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        RecordingControl recording = new RecordingControl("recording");
        Button button = new Button(new PApplet(), "btnChild", "Child", 10.0F, 20.0F, 60.0F, 30.0F);
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        panel.add(recording);
        panel.add(button);
        panel.setVisible(false);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        panel.draw();
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 110.0F, 100.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 110.0F, 100.0F));

        assertEquals(0, recording.drawCalls);
        assertEquals(0, sketch.pushMatrixCalls);
        assertEquals(0, clicks.get());
        assertFalse(button.isVisible());
    }

    @Test
    void disabledPanelDoesNotActivateChildrenAndBlocksLowerLayers() {
        InputManager inputManager = new InputManager();
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        Button child = new Button(new PApplet(), "btnChild", "Child", 10.0F, 20.0F, 60.0F, 30.0F);
        Button lower = new Button(new PApplet(), "btnLower", "Lower", 110.0F, 100.0F, 60.0F, 30.0F);
        AtomicInteger childClicks = new AtomicInteger();
        AtomicInteger lowerClicks = new AtomicInteger();
        child.setClickListener(childClicks::incrementAndGet);
        lower.setClickListener(lowerClicks::incrementAndGet);
        panel.add(child);
        panel.setEnabled(false);
        inputManager.registerLayer(new PanelInputLayer(1, panel));
        inputManager.registerLayer(new ButtonInputLayer(0, lower));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 110.0F, 100.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 110.0F, 100.0F));

        assertEquals(0, childClicks.get());
        assertEquals(0, lowerClicks.get());
        assertFalse(child.isEnabled());
    }

    @Test
    void setPositionMovesPanelWithoutRepositioningChildren() {
        RecordingApplet sketch = new RecordingApplet();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        RecordingControl child = new RecordingControl("child");
        panel.add(child);

        panel.setPosition(150.0F, 120.0F);
        panel.draw();

        assertEquals(0, child.setPositionCalls);
        assertEquals(1, child.drawCalls);
        assertEquals(150.0F, sketch.lastTranslateX);
        assertEquals(120.0F, sketch.lastTranslateY);
    }

    @Test
    void childTooltipTargetReportsGlobalBounds() {
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        Button button = new Button(new PApplet(), "btnChild", "Child", 10.0F, 20.0F, 40.0F, 20.0F)
                .setTooltip("Child tooltip");
        panel.add(button);
        TooltipTarget target = panel.tooltipTarget(button);

        TooltipBounds initialBounds = target.getTooltipBounds();
        panel.setPosition(150.0F, 120.0F);
        TooltipBounds movedBounds = target.getTooltipBounds();

        assertEquals(90.0F, initialBounds.x());
        assertEquals(90.0F, initialBounds.y());
        assertEquals(140.0F, movedBounds.x());
        assertEquals(130.0F, movedBounds.y());
        assertSame(button.getTooltip(), target.getTooltip());
    }

    @Test
    void keyboardRoutesToFocusedChild() {
        InputManager inputManager = new InputManager();
        Panel panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);
        TextField textField = new TextField(new PApplet(), "txtChild", "", 10.0F, 20.0F, 100.0F, 30.0F);
        panel.add(textField);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 110.0F, 100.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 110.0F, 100.0F));
        inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, 'a', 65, false, false, false));

        assertTrue(textField.isFocused());
        assertEquals("a", textField.getText());
    }

    private static final class RecordingApplet extends PApplet {
        private int pushMatrixCalls;
        private int popMatrixCalls;
        private int pushStyleCalls;
        private int popStyleCalls;
        private int rectModeCalls;
        private int rectCalls;
        private int fillCalls;
        private int noFillCalls;
        private int strokeCalls;
        private int noStrokeCalls;
        private float lastTranslateX;
        private float lastTranslateY;
        private float lastRectX;
        private float lastRectY;
        private float lastRectWidth;
        private float lastRectHeight;
        private float lastRectRadius;
        private int lastFillColor;
        private int lastStrokeColor;
        private float lastStrokeWeight;

        @Override
        public void pushMatrix() {
            this.pushMatrixCalls++;
        }

        @Override
        public void popMatrix() {
            this.popMatrixCalls++;
        }

        @Override
        public void translate(float x, float y) {
            this.lastTranslateX = x;
            this.lastTranslateY = y;
        }

        @Override
        public void pushStyle() {
            this.pushStyleCalls++;
        }

        @Override
        public void popStyle() {
            this.popStyleCalls++;
        }

        @Override
        public void rectMode(int mode) {
            this.rectModeCalls++;
        }

        @Override
        public void fill(int rgb) {
            this.fillCalls++;
            this.lastFillColor = rgb;
        }

        @Override
        public void noFill() {
            this.noFillCalls++;
        }

        @Override
        public void stroke(int rgb) {
            this.strokeCalls++;
            this.lastStrokeColor = rgb;
        }

        @Override
        public void strokeWeight(float weight) {
            this.lastStrokeWeight = weight;
        }

        @Override
        public void noStroke() {
            this.noStrokeCalls++;
        }

        @Override
        public void rect(float a, float b, float c, float d, float r) {
            this.rectCalls++;
            this.lastRectX = a;
            this.lastRectY = b;
            this.lastRectWidth = c;
            this.lastRectHeight = d;
            this.lastRectRadius = r;
        }

        private void resetRenderCalls() {
            this.pushStyleCalls = 0;
            this.popStyleCalls = 0;
            this.rectModeCalls = 0;
            this.rectCalls = 0;
            this.fillCalls = 0;
            this.noFillCalls = 0;
            this.strokeCalls = 0;
            this.noStrokeCalls = 0;
            this.lastFillColor = 0;
            this.lastStrokeColor = 0;
            this.lastStrokeWeight = 0.0F;
        }
    }

    private static final class RecordingControl implements Control {
        private final String code;
        private int drawCalls;
        private int setPositionCalls;
        private boolean enabled = true;
        private boolean visible = true;

        private RecordingControl(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

        public void draw() {
            this.drawCalls++;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isVisible() {
            return this.visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void setPosition(float x, float y) {
            this.setPositionCalls++;
        }
    }
}
