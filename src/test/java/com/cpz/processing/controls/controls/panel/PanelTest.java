package com.cpz.processing.controls.controls.panel;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PanelTest {
    @Test
    void panelImplementsControl() {
        Control panel = new Panel(new PApplet(), "panel", 100.0F, 80.0F, 240.0F, 160.0F);

        assertEquals("panel", panel.getCode());
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
        private float lastTranslateX;
        private float lastTranslateY;

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
