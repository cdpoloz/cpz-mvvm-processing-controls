package com.cpz.processing.controls.controls.indicator;

import com.cpz.processing.controls.controls.KeyboardRoutableControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PShape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicatorTest {
    @Test
    void defaultsToOff() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertFalse(indicator.isOn());
    }

    @Test
    void setOnUpdatesLogicalState() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOn(true);
        assertTrue(indicator.isOn());

        indicator.setOn(false);
        assertFalse(indicator.isOn());
    }

    @Test
    void colorSettersUpdateRuntimeColors() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOnColor(0xFF00AA00);
        indicator.setOffColor(0xFF111111);

        assertEquals(0xFF00AA00, indicator.getOnColor());
        assertEquals(0xFF111111, indicator.getOffColor());
    }

    @Test
    void defaultsToConfiguredStrokeDefaults() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertEquals(Indicator.DEFAULT_BORDER_COLOR, indicator.getStrokeColor());
        assertEquals(1.0F, indicator.getStrokeWeight());
    }

    @Test
    void strokeSettersUpdateRuntimeStroke() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.5F);

        assertEquals(0xFFFFFFFF, indicator.getStrokeColor());
        assertEquals(2.5F, indicator.getStrokeWeight());
    }

    @Test
    void negativeStrokeWeightIsClampedToZero() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setStrokeWeight(-2.0F);

        assertEquals(0.0F, indicator.getStrokeWeight());
    }

    @Test
    void drawUsesOffColorWhenOff() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setOffColor(0xFF111111);

        indicator.draw();

        assertEquals(0xFF111111, sketch.lastFillColor);
        assertEquals(1, sketch.circleCalls);
        assertEquals(52.0F, sketch.lastCircleX);
        assertEquals(65.0F, sketch.lastCircleY);
        assertEquals(24.0F, sketch.lastCircleDiameter);
    }

    @Test
    void drawUsesConfiguredStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.5F);

        indicator.draw();

        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(2.5F, sketch.lastStrokeWeight);
        assertEquals(1, sketch.strokeCalls);
        assertEquals(0, sketch.noStrokeCalls);
    }

    @Test
    void zeroStrokeWeightDisablesStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setStrokeWeight(0.0F);

        indicator.draw();

        assertEquals(1, sketch.noStrokeCalls);
        assertEquals(0, sketch.strokeCalls);
    }

    @Test
    void drawUsesOnColorWhenOn() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);
        indicator.setOnColor(0xFF00AA00);
        indicator.setOn(true);

        indicator.draw();

        assertEquals(0xFF00AA00, sketch.lastFillColor);
    }

    @Test
    void svgConstructorLoadsAndDrawsTintedShape() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F, "data/img/test.svg");
        indicator.setOnColor(0xFF00AA00);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(2.0F);
        indicator.setOn(true);

        indicator.draw();

        assertEquals(1, sketch.loadShapeCalls);
        assertEquals("data/img/test.svg", sketch.lastLoadShapePath);
        assertEquals(0xFF00AA00, sketch.lastFillColor);
        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(2.0F, sketch.lastStrokeWeight);
        assertEquals(0, sketch.circleCalls);
        assertEquals(1, sketch.shapeCalls);
        assertEquals(52.0F, sketch.lastShapeX);
        assertEquals(65.0F, sketch.lastShapeY);
        assertEquals(24.0F, sketch.lastShapeWidth);
        assertEquals(30.0F, sketch.lastShapeHeight);
    }

    @Test
    void svgConstructorWithRelativeBoundsResolvesAgainstCanvas() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(
                sketch,
                "ind",
                ControlBounds.relative(0.1F, 0.2F, 0.05F, 0.1F),
                "data/img/test.svg"
        );

        indicator.draw();

        assertBounds(indicator.getTooltipBounds(), 80.0F, 120.0F, 30.0F, 60.0F);
        assertEquals(1, sketch.shapeCalls);
        assertEquals(95.0F, sketch.lastShapeX);
        assertEquals(150.0F, sketch.lastShapeY);
        assertEquals(30.0F, sketch.lastShapeWidth);
        assertEquals(60.0F, sketch.lastShapeHeight);
    }

    @Test
    void missingSvgUsesExistingRendererBehaviorAndDrawsNothing() {
        RecordingApplet sketch = recordingSketch(800, 600);
        sketch.returnNullShape = true;
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F, "data/img/missing.svg");

        indicator.draw();

        assertEquals(2, sketch.loadShapeCalls);
        assertEquals("img/missing.svg", sketch.lastLoadShapePath);
        assertEquals(0, sketch.shapeCalls);
        assertEquals(0, sketch.circleCalls);
    }

    @Test
    void invisibleIndicatorDoesNotDraw() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 30.0F);

        indicator.setVisible(false);
        indicator.draw();

        assertEquals(0, sketch.circleCalls);
        assertEquals(0, sketch.strokeCalls);
        assertEquals(0, sketch.noStrokeCalls);
    }

    @Test
    void disabledIndicatorDoesNotChangeLogicalState() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setOn(true);
        indicator.setEnabled(false);

        assertTrue(indicator.isOn());
    }

    @Test
    void disabledIndicatorDoesNotChangeStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        Indicator indicator = new Indicator(sketch, 40.0F, 50.0F, 24.0F, 24.0F);
        indicator.setStrokeColor(0xFFFFFFFF);
        indicator.setStrokeWeight(3.0F);

        indicator.setEnabled(false);
        indicator.draw();

        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(3.0F, sketch.lastStrokeWeight);
    }

    @Test
    void absoluteBoundsAreTopLeftLogicalBounds() {
        Indicator indicator = new Indicator(sketch(800, 600), "ind", 40.0F, 50.0F, 24.0F, 30.0F);

        assertBounds(indicator.getTooltipBounds(), 40.0F, 50.0F, 24.0F, 30.0F);
    }

    @Test
    void relativeBoundsResolveAgainstCanvas() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.relative(0.1F, 0.2F, 0.05F, 0.1F)
        );

        assertBounds(indicator.getTooltipBounds(), 80.0F, 120.0F, 30.0F, 60.0F);
    }

    @Test
    void controlBoundsOfCanMixRelativeAndAbsoluteMeasures() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.of(
                        ControlMeasure.relative(0.1F),
                        ControlMeasure.relative(0.2F),
                        ControlMeasure.absolute(24.0F),
                        ControlMeasure.absolute(30.0F)
                )
        );

        assertBounds(indicator.getTooltipBounds(), 80.0F, 120.0F, 24.0F, 30.0F);
    }

    @Test
    void relativeBoundsInsidePanelResolveAgainstPanel() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Indicator indicator = new Indicator(sketch, "ind", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        panel.add(indicator);

        TooltipBounds bounds = panel.tooltipTarget(indicator).getTooltipBounds();

        assertBounds(bounds, 200.0F, 180.0F, 50.0F, 20.0F);
    }

    @Test
    void setPositionAfterRelativeBoundsMakesPositionAbsoluteAndKeepsRelativeSize() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );

        indicator.setPosition(300.0F, 240.0F);

        assertBounds(indicator.getTooltipBounds(), 300.0F, 240.0F, 300.0F, 120.0F);
    }

    @Test
    void setSizeAfterRelativeBoundsMakesSizeAbsoluteAndKeepsRelativePosition() {
        Indicator indicator = new Indicator(
                sketch(800, 600),
                "ind",
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );

        indicator.setSize(160.0F, 40.0F);

        assertBounds(indicator.getTooltipBounds(), 200.0F, 60.0F, 160.0F, 40.0F);
    }

    @Test
    void implementsTooltipAttachable() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertInstanceOf(TooltipAttachable.class, indicator);
    }

    @Test
    void setTooltipTextUpdatesTooltipAtRuntime() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        indicator.setTooltipText("initial");
        indicator.setTooltipText("runtime");

        assertEquals("runtime", indicator.getTooltip().getText());
    }

    @Test
    void invisibleIndicatorIsNotATooltipTarget() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F)
                .setTooltip("status");

        indicator.setVisible(false);

        assertFalse(indicator.isTooltipTargetVisible());
    }

    @Test
    void disabledIndicatorStillAllowsTooltipTarget() {
        Indicator indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F)
                .setTooltip("status");

        indicator.setEnabled(false);

        assertTrue(indicator.isTooltipTargetVisible());
        assertTrue(indicator.isTooltipTargetEnabled());
        assertFalse(indicator.isEnabled());
    }

    @Test
    void indicatorIsNotInteractive() {
        Object indicator = new Indicator(sketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F);

        assertFalse(indicator instanceof PointerRoutableControl);
        assertFalse(indicator instanceof KeyboardRoutableControl);
    }

    @Test
    void svgIndicatorIsNotInteractive() {
        Object indicator = new Indicator(recordingSketch(800, 600), 40.0F, 50.0F, 24.0F, 24.0F, "data/img/test.svg");

        assertFalse(indicator instanceof PointerRoutableControl);
        assertFalse(indicator instanceof KeyboardRoutableControl);
    }

    private static void assertBounds(TooltipBounds bounds, float x, float y, float width, float height) {
        assertEquals(x, bounds.x());
        assertEquals(y, bounds.y());
        assertEquals(width, bounds.width());
        assertEquals(height, bounds.height());
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static RecordingApplet recordingSketch(int width, int height) {
        RecordingApplet sketch = new RecordingApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }

    private static final class RecordingApplet extends PApplet {
        private int lastFillColor;
        private int lastStrokeColor;
        private int circleCalls;
        private int shapeCalls;
        private int loadShapeCalls;
        private int strokeCalls;
        private int noStrokeCalls;
        private float lastCircleX;
        private float lastCircleY;
        private float lastCircleDiameter;
        private float lastShapeX;
        private float lastShapeY;
        private float lastShapeWidth;
        private float lastShapeHeight;
        private float lastStrokeWeight;
        private String lastLoadShapePath;
        private boolean returnNullShape;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void ellipseMode(int mode) {
        }

        @Override
        public void shapeMode(int mode) {
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
        public void fill(int rgb) {
            this.lastFillColor = rgb;
        }

        @Override
        public void circle(float x, float y, float extent) {
            this.circleCalls++;
            this.lastCircleX = x;
            this.lastCircleY = y;
            this.lastCircleDiameter = extent;
        }

        @Override
        public void shape(PShape shape, float x, float y, float c, float d) {
            this.shapeCalls++;
            this.lastShapeX = x;
            this.lastShapeY = y;
            this.lastShapeWidth = c;
            this.lastShapeHeight = d;
        }

        @Override
        public PShape loadShape(String filename) {
            this.loadShapeCalls++;
            this.lastLoadShapePath = filename;
            return this.returnNullShape ? null : new PShape();
        }
    }
}
