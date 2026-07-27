package com.cpz.processing.controls.characterization;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterizes nested Panel coordinate propagation. Closed controls are
 * rendered through Processing matrix translations; open DropDown content is
 * rendered in global overlay coordinates.
 */
class NestedPanelCharacterizationTest {
    @Test
    void directPanelDropDownEstablishesSingleParentGlobalOverlayBaseline() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager input = new InputManager();
        OverlayManager overlays = new OverlayManager();
        Panel panel = new Panel(sketch, "root", 100.0F, 80.0F, 300.0F, 180.0F);
        DropDown dropDown = dropDown(sketch, overlays, input, "direct", 60.0F, 30.0F, 100.0F, 24.0F);
        panel.add(dropDown);
        input.registerLayer(new PanelInputLayer(0, panel));
        try {
            panel.draw();
            RectSnapshot closed = sketch.findRect(100.0F, 24.0F);
            assertRect(closed, 110.0F, 98.0F, 100.0F, 24.0F);

            press(input, 160.0F, 110.0F);
            sketch.clearRectHistory();
            renderOverlays(overlays);

            RectSnapshot openBase = sketch.findRect(100.0F, 24.0F);
            RectSnapshot list = sketch.findRect(100.0F, 60.0F);
            assertRect(openBase, 110.0F, 98.0F, 100.0F, 24.0F);
            assertRect(list, 110.0F, 122.0F, 100.0F, 60.0F);
        } finally {
            dropDown.dispose();
        }
    }

    @Test
    void nestedPanelsRenderAndHitTestOrdinaryDescendantAtSumOfOffsets() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager input = new InputManager();
        Panel root = new Panel(sketch, "root", 100.0F, 80.0F, 300.0F, 180.0F);
        Panel nested = new Panel(sketch, "nested", 40.0F, 30.0F, 200.0F, 120.0F);
        Button button = new Button(sketch, "button", "Button", 60.0F, 25.0F, 80.0F, 30.0F);
        AtomicInteger clicks = new AtomicInteger();
        button.setClickListener(clicks::incrementAndGet);
        nested.add(button);
        root.add(nested);
        input.registerLayer(new PanelInputLayer(0, root));

        root.draw();
        RectSnapshot rendered = sketch.findRect(80.0F, 30.0F);
        assertRect(rendered, 160.0F, 120.0F, 80.0F, 30.0F);

        click(input, 200.0F, 135.0F);

        assertEquals(1, clicks.get(),
                "nested non-overlay hit testing uses the same summed position as matrix rendering");
    }

    @Test
    void nestedAbsoluteDropDownCurrentlyLosesRootOffsetOnlyWhenItBecomesGlobalOverlay() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager input = new InputManager();
        OverlayManager overlays = new OverlayManager();
        Panel root = new Panel(sketch, "root", 100.0F, 80.0F, 300.0F, 180.0F);
        Panel nested = new Panel(sketch, "nested", 40.0F, 30.0F, 200.0F, 120.0F);
        DropDown dropDown = dropDown(sketch, overlays, input, "nestedDropDown", 60.0F, 25.0F, 100.0F, 24.0F);
        dropDown.setSelectedIndex(2);
        nested.add(dropDown);
        root.add(nested);
        input.registerLayer(new PanelInputLayer(0, root));
        try {
            root.draw();
            RectSnapshot closed = sketch.findRect(100.0F, 24.0F);
            assertRect(closed, 150.0F, 123.0F, 100.0F, 24.0F);

            press(input, 200.0F, 135.0F);
            assertTrue(dropDown.isExpanded(),
                    "closed-field nested hit testing correctly follows both Panel transforms");

            sketch.clearRectHistory();
            renderOverlays(overlays);
            RectSnapshot currentBase = sketch.findRect(100.0F, 24.0F);
            RectSnapshot currentList = sketch.findRect(100.0F, 60.0F);

            assertRect(currentBase, 50.0F, 43.0F, 100.0F, 24.0F);
            assertRect(currentList, 50.0F, 67.0F, 100.0F, 60.0F);
            assertEquals(100.0F, 150.0F - currentBase.x, 0.001F,
                    "characterization: exactly the root x offset is missing, not applied twice");
            assertEquals(80.0F, 123.0F - currentBase.y, 0.001F,
                    "characterization: exactly the root y offset is missing, not applied twice");

            press(input, currentList.x + 50.0F, currentList.y + 10.0F);

            assertEquals("One", dropDown.getSelectedItem(),
                    "overlay hit testing is aligned with the currently rendered, but globally misplaced, list");
            assertFalse(dropDown.isExpanded());
        } finally {
            dropDown.dispose();
        }
    }

    @Test
    void nestedRelativeMeasuresResolvePerImmediateParentButOverlayStillMissesRootOffset() {
        RecordingApplet sketch = sketch(800, 600);
        InputManager input = new InputManager();
        OverlayManager overlays = new OverlayManager();
        Panel root = new Panel(
                sketch,
                "root",
                ControlBounds.relative(0.1F, 0.1F, 0.5F, 0.3F)
        );
        Panel nested = new Panel(
                sketch,
                "nested",
                ControlBounds.relative(0.1F, 0.2F, 0.5F, 0.5F)
        );
        DropDown dropDown = dropDown(
                sketch,
                overlays,
                input,
                "relativeDropDown",
                ControlBounds.relative(0.5F, 0.5F, 0.5F, 0.2F)
        );
        nested.add(dropDown);
        root.add(nested);
        input.registerLayer(new PanelInputLayer(0, root));
        try {
            root.draw();

            // Canvas 800x600 -> root (80,60,300,180) -> nested
            // (30,36,90,90) -> DropDown center (45,45), size (45,18).
            RectSnapshot closed = sketch.findRect(45.0F, 18.0F);
            assertRect(closed, 132.5F, 132.0F, 45.0F, 18.0F);

            press(input, 155.0F, 141.0F);
            assertTrue(dropDown.isExpanded());

            sketch.clearRectHistory();
            renderOverlays(overlays);
            RectSnapshot currentBase = sketch.findRect(45.0F, 18.0F);
            RectSnapshot currentList = sketch.findRect(45.0F, 60.0F);

            assertRect(currentBase, 52.5F, 72.0F, 45.0F, 18.0F);
            assertRect(currentList, 52.5F, 90.0F, 45.0F, 60.0F);
            assertEquals(80.0F, 132.5F - currentBase.x, 0.001F);
            assertEquals(60.0F, 132.0F - currentBase.y, 0.001F);
        } finally {
            dropDown.dispose();
        }
    }

    private static DropDown dropDown(
            PApplet sketch,
            OverlayManager overlays,
            InputManager input,
            String code,
            float x,
            float y,
            float width,
            float height
    ) {
        return styled(new DropDown(
                sketch,
                overlays,
                input,
                code,
                List.of("One", "Two", "Three"),
                0,
                x,
                y,
                width,
                height
        ));
    }

    private static DropDown dropDown(
            PApplet sketch,
            OverlayManager overlays,
            InputManager input,
            String code,
            ControlBounds bounds
    ) {
        return styled(new DropDown(
                sketch,
                overlays,
                input,
                code,
                List.of("One", "Two", "Three"),
                0,
                bounds
        ));
    }

    private static DropDown styled(DropDown dropDown) {
        DropDownStyleConfig config = new DropDownStyleConfig();
        config.itemHeight = 20.0F;
        config.maxVisibleItems = 3;
        config.textSize = 12.0F;
        dropDown.setStyle(new DefaultDropDownStyle(config));
        return dropDown;
    }

    private static RecordingApplet sketch(int width, int height) {
        RecordingApplet sketch = new RecordingApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static void press(InputManager input, float x, float y) {
        input.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, x, y));
    }

    private static void click(InputManager input, float x, float y) {
        press(input, x, y);
        input.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, x, y));
    }

    private static void renderOverlays(OverlayManager overlays) {
        for (OverlayEntry entry : overlays.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private static void assertRect(
            RectSnapshot rect,
            float x,
            float y,
            float width,
            float height
    ) {
        assertNotNull(rect);
        assertEquals(x, rect.x, 0.001F);
        assertEquals(y, rect.y, 0.001F);
        assertEquals(width, rect.width, 0.001F);
        assertEquals(height, rect.height, 0.001F);
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
        private final List<RectSnapshot> rects = new ArrayList<>();
        private float translateX;
        private float translateY;

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
        public void rect(float x, float y, float width, float height, float radius) {
            this.rects.add(new RectSnapshot(
                    x + this.translateX,
                    y + this.translateY,
                    width,
                    height
            ));
        }

        private void clearRectHistory() {
            this.rects.clear();
        }

        private RectSnapshot findRect(float width, float height) {
            for (RectSnapshot rect : this.rects) {
                if (Math.abs(rect.width - width) <= 0.001F
                        && Math.abs(rect.height - height) <= 0.001F) {
                    return rect;
                }
            }
            return null;
        }
    }
}
