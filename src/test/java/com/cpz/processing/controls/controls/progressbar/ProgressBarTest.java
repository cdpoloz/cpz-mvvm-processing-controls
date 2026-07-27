package com.cpz.processing.controls.controls.progressbar;

import com.cpz.processing.controls.controls.KeyboardRoutableControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.progressbar.style.ProgressBarStyle;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressBarTest {
    @ParameterizedTest(name = "setValue({0})")
    @MethodSource("nonFiniteValues")
    void setValueRejectsNonFiniteValuesWithoutChangingState(String label, float invalid) {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);
        progressBar.setValue(0.4F);

        assertThrows(IllegalArgumentException.class, () -> progressBar.setValue(invalid));
        assertEquals(0.4F, progressBar.getValue());
        assertEquals(0.4F, progressBar.getProgress());
    }

    @ParameterizedTest(name = "setRange({0}, 1)")
    @MethodSource("nonFiniteValues")
    void setRangeRejectsNonFiniteMinimumWithoutChangingState(String label, float invalid) {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);
        progressBar.setValue(0.4F);

        assertThrows(IllegalArgumentException.class, () -> progressBar.setRange(invalid, 1.0F));
        assertEquals(0.0F, progressBar.getMin());
        assertEquals(1.0F, progressBar.getMax());
        assertEquals(0.4F, progressBar.getValue());
    }

    @ParameterizedTest(name = "setRange(0, {0})")
    @MethodSource("nonFiniteValues")
    void setRangeRejectsNonFiniteMaximumWithoutChangingState(String label, float invalid) {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);
        progressBar.setValue(0.4F);

        assertThrows(IllegalArgumentException.class, () -> progressBar.setRange(0.0F, invalid));
        assertEquals(0.0F, progressBar.getMin());
        assertEquals(1.0F, progressBar.getMax());
        assertEquals(0.4F, progressBar.getValue());
    }

    private static Stream<Arguments> nonFiniteValues() {
        return Stream.of(
                Arguments.of("NaN", Float.NaN),
                Arguments.of("+Infinity", Float.POSITIVE_INFINITY),
                Arguments.of("-Infinity", Float.NEGATIVE_INFINITY)
        );
    }

    @Test
    void defaultsToZeroToOneRangeAndZeroValue() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        assertEquals(0.0F, progressBar.getMin());
        assertEquals(1.0F, progressBar.getMax());
        assertEquals(0.0F, progressBar.getValue());
        assertEquals(0.0F, progressBar.getProgress());
    }

    @Test
    void setValueClampsToRange() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setValue(2.0F);
        assertEquals(1.0F, progressBar.getValue());

        progressBar.setValue(-1.0F);
        assertEquals(0.0F, progressBar.getValue());
    }

    @Test
    void progressNormalizesValue() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setValue(0.25F);

        assertEquals(0.25F, progressBar.getProgress());
    }

    @Test
    void customRangeNormalizesValue() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setRange(10.0F, 30.0F);
        progressBar.setValue(20.0F);

        assertEquals(10.0F, progressBar.getMin());
        assertEquals(30.0F, progressBar.getMax());
        assertEquals(20.0F, progressBar.getValue());
        assertEquals(0.5F, progressBar.getProgress());
    }

    @Test
    void equalRangeAvoidsDivisionByZeroAndReportsFullProgressAfterClamping() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setRange(5.0F, 5.0F);

        assertEquals(5.0F, progressBar.getValue());
        assertEquals(1.0F, progressBar.getProgress());
    }

    @Test
    void invertedRangeIsSortedAndValueIsClamped() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setRange(30.0F, 10.0F);

        assertEquals(10.0F, progressBar.getMin());
        assertEquals(30.0F, progressBar.getMax());
        assertEquals(10.0F, progressBar.getValue());
        assertEquals(0.0F, progressBar.getProgress());
    }

    @Test
    void negativeStrokeWeightIsClampedToZero() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setStrokeWeight(-2.0F);

        assertEquals(0.0F, progressBar.getStrokeWeight());
    }

    @Test
    void hasDefaultStyle() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        assertNotNull(progressBar.getStyle());
        assertEquals(ProgressBar.DEFAULT_TRACK_COLOR, progressBar.getStyle().getTrackColor());
        assertEquals(ProgressBar.DEFAULT_FILL_COLOR, progressBar.getStyle().getFillColor());
        assertEquals(ProgressBar.DEFAULT_STROKE_COLOR, progressBar.getStyle().getStrokeColor());
        assertEquals(1.0F, progressBar.getStyle().getStrokeWeight());
        assertEquals(ProgressBarFillDirection.LEFT_TO_RIGHT, progressBar.getStyle().getFillDirection());
    }

    @Test
    void setStyleUpdatesVisualProperties() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);
        ProgressBarStyle style = new ProgressBarStyle()
                .setTrackColor(0xFF111111)
                .setFillColor(0xFF00AA00)
                .setStrokeColor(0xFFFFFFFF)
                .setStrokeWeight(2.5F)
                .setFillDirection(ProgressBarFillDirection.BOTTOM_TO_TOP);

        progressBar.setStyle(style);

        assertSame(style, progressBar.getStyle());
        assertEquals(0xFF111111, progressBar.getTrackColor());
        assertEquals(0xFF00AA00, progressBar.getFillColor());
        assertEquals(0xFFFFFFFF, progressBar.getStrokeColor());
        assertEquals(2.5F, progressBar.getStrokeWeight());
        assertEquals(ProgressBarFillDirection.BOTTOM_TO_TOP, progressBar.getFillDirection());
    }

    @Test
    void nullStyleResetsToDefaultStyle() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setStyle(null);

        assertNotNull(progressBar.getStyle());
        assertEquals(ProgressBar.DEFAULT_TRACK_COLOR, progressBar.getTrackColor());
        assertEquals(ProgressBar.DEFAULT_FILL_COLOR, progressBar.getFillColor());
        assertEquals(ProgressBar.DEFAULT_STROKE_COLOR, progressBar.getStrokeColor());
        assertEquals(1.0F, progressBar.getStrokeWeight());
        assertEquals(ProgressBarFillDirection.LEFT_TO_RIGHT, progressBar.getFillDirection());
    }

    @Test
    void directSettersDelegateToStyle() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setTrackColor(0xFF111111);
        progressBar.setFillColor(0xFF00AA00);
        progressBar.setStrokeColor(0xFFFFFFFF);
        progressBar.setStrokeWeight(2.5F);
        progressBar.setFillDirection(ProgressBarFillDirection.RIGHT_TO_LEFT);

        assertEquals(0xFF111111, progressBar.getStyle().getTrackColor());
        assertEquals(0xFF00AA00, progressBar.getStyle().getFillColor());
        assertEquals(0xFFFFFFFF, progressBar.getStyle().getStrokeColor());
        assertEquals(2.5F, progressBar.getStyle().getStrokeWeight());
        assertEquals(ProgressBarFillDirection.RIGHT_TO_LEFT, progressBar.getStyle().getFillDirection());
    }

    @Test
    void nullFillDirectionResetsToDefaultDirection() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setFillDirection(ProgressBarFillDirection.TOP_TO_BOTTOM);
        progressBar.setFillDirection(null);

        assertEquals(ProgressBarFillDirection.LEFT_TO_RIGHT, progressBar.getFillDirection());
    }

    @Test
    void zeroStrokeWeightDisablesBorderStroke() {
        RecordingApplet sketch = recordingSketch(800, 600);
        ProgressBar progressBar = new ProgressBar(sketch, 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setStrokeWeight(0.0F);
        progressBar.setValue(0.5F);
        progressBar.draw();

        assertEquals(0, sketch.strokeCalls);
        assertEquals(0, sketch.noFillCalls);
        assertTrue(sketch.noStrokeCalls > 0);
    }

    @Test
    void drawUsesTrackFillAndBorder() {
        RecordingApplet sketch = recordingSketch(800, 600);
        ProgressBar progressBar = new ProgressBar(sketch, 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setValue(0.25F);
        progressBar.setTrackColor(0xFF111111);
        progressBar.setFillColor(0xFF00AA00);
        progressBar.setStrokeColor(0xFFFFFFFF);
        progressBar.setStrokeWeight(2.5F);
        progressBar.draw();

        assertEquals(3, sketch.rectCalls);
        assertEquals(0xFF111111, sketch.fillColors[0]);
        assertEquals(0xFF00AA00, sketch.fillColors[1]);
        assertEquals(40.0F, sketch.rectX[0]);
        assertEquals(50.0F, sketch.rectY[0]);
        assertEquals(200.0F, sketch.rectWidth[0]);
        assertEquals(20.0F, sketch.rectHeight[0]);
        assertEquals(40.0F, sketch.rectX[1]);
        assertEquals(50.0F, sketch.rectY[1]);
        assertEquals(50.0F, sketch.rectWidth[1]);
        assertEquals(20.0F, sketch.rectHeight[1]);
        assertEquals(0xFFFFFFFF, sketch.lastStrokeColor);
        assertEquals(2.5F, sketch.lastStrokeWeight);
    }

    @Test
    void drawUsesRightToLeftFillDirection() {
        RecordingApplet sketch = recordingSketch(800, 600);
        ProgressBar progressBar = new ProgressBar(sketch, 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setValue(0.25F);
        progressBar.setFillDirection(ProgressBarFillDirection.RIGHT_TO_LEFT);
        progressBar.draw();

        assertEquals(3, sketch.rectCalls);
        assertEquals(190.0F, sketch.rectX[1]);
        assertEquals(50.0F, sketch.rectY[1]);
        assertEquals(50.0F, sketch.rectWidth[1]);
        assertEquals(20.0F, sketch.rectHeight[1]);
    }

    @Test
    void drawUsesBottomToTopFillDirection() {
        RecordingApplet sketch = recordingSketch(800, 600);
        ProgressBar progressBar = new ProgressBar(sketch, 40.0F, 50.0F, 20.0F, 200.0F);

        progressBar.setValue(0.25F);
        progressBar.setFillDirection(ProgressBarFillDirection.BOTTOM_TO_TOP);
        progressBar.draw();

        assertEquals(3, sketch.rectCalls);
        assertEquals(40.0F, sketch.rectX[1]);
        assertEquals(200.0F, sketch.rectY[1]);
        assertEquals(20.0F, sketch.rectWidth[1]);
        assertEquals(50.0F, sketch.rectHeight[1]);
    }

    @Test
    void drawUsesTopToBottomFillDirection() {
        RecordingApplet sketch = recordingSketch(800, 600);
        ProgressBar progressBar = new ProgressBar(sketch, 40.0F, 50.0F, 20.0F, 200.0F);

        progressBar.setValue(0.25F);
        progressBar.setFillDirection(ProgressBarFillDirection.TOP_TO_BOTTOM);
        progressBar.draw();

        assertEquals(3, sketch.rectCalls);
        assertEquals(40.0F, sketch.rectX[1]);
        assertEquals(50.0F, sketch.rectY[1]);
        assertEquals(20.0F, sketch.rectWidth[1]);
        assertEquals(50.0F, sketch.rectHeight[1]);
    }

    @Test
    void invisibleProgressBarDoesNotDraw() {
        RecordingApplet sketch = recordingSketch(800, 600);
        ProgressBar progressBar = new ProgressBar(sketch, 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setVisible(false);
        progressBar.draw();

        assertEquals(0, sketch.rectCalls);
    }

    @Test
    void disabledProgressBarDoesNotChangeLogicalValueOrTooltipAvailability() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F)
                .setTooltip("progress");

        progressBar.setValue(0.75F);
        progressBar.setEnabled(false);

        assertEquals(0.75F, progressBar.getValue());
        assertFalse(progressBar.isEnabled());
        assertTrue(progressBar.isTooltipTargetVisible());
        assertTrue(progressBar.isTooltipTargetEnabled());
    }

    @Test
    void disabledProgressBarStillAcceptsProgrammaticValueUpdates() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        progressBar.setRange(10.0F, 30.0F);
        progressBar.setValue(12.0F);
        progressBar.setEnabled(false);
        progressBar.setValue(20.0F);

        assertFalse(progressBar.isEnabled());
        assertEquals(20.0F, progressBar.getValue());
        assertEquals(0.5F, progressBar.getProgress());
    }

    @Test
    void disabledProgressBarKeepsRangeAndVisualConfiguration() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F)
                .setTooltip("progress");

        progressBar.setRange(10.0F, 30.0F);
        progressBar.setTrackColor(0xFF111111);
        progressBar.setFillColor(0xFF00AA00);
        progressBar.setStrokeColor(0xFFFFFFFF);
        progressBar.setStrokeWeight(2.5F);
        progressBar.setEnabled(false);

        assertEquals(10.0F, progressBar.getMin());
        assertEquals(30.0F, progressBar.getMax());
        assertEquals(0xFF111111, progressBar.getTrackColor());
        assertEquals(0xFF00AA00, progressBar.getFillColor());
        assertEquals(0xFFFFFFFF, progressBar.getStrokeColor());
        assertEquals(2.5F, progressBar.getStrokeWeight());
        assertEquals("progress", progressBar.getTooltip().getText());
        assertTrue(progressBar.isTooltipTargetVisible());
        assertTrue(progressBar.isTooltipTargetEnabled());
    }

    @Test
    void visibleFalseIsNotATooltipTarget() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F)
                .setTooltip("progress");

        progressBar.setVisible(false);

        assertFalse(progressBar.isTooltipTargetVisible());
    }

    @Test
    void absoluteBoundsAreTopLeftLogicalBounds() {
        ProgressBar progressBar = new ProgressBar(sketch(800, 600), "bar", 40.0F, 50.0F, 200.0F, 20.0F);

        assertBounds(progressBar.getTooltipBounds(), 40.0F, 50.0F, 200.0F, 20.0F);
    }

    @Test
    void relativeBoundsResolveAgainstCanvas() {
        ProgressBar progressBar = new ProgressBar(
                sketch(800, 600),
                "bar",
                ControlBounds.relative(0.1F, 0.2F, 0.5F, 0.05F)
        );

        assertBounds(progressBar.getTooltipBounds(), 80.0F, 120.0F, 300.0F, 30.0F);
    }

    @Test
    void controlBoundsOfCanMixRelativeAndAbsoluteMeasures() {
        ProgressBar progressBar = new ProgressBar(
                sketch(800, 600),
                "bar",
                ControlBounds.of(
                        ControlMeasure.relative(0.1F),
                        ControlMeasure.relative(0.2F),
                        ControlMeasure.absolute(200.0F),
                        ControlMeasure.absolute(20.0F)
                )
        );

        assertBounds(progressBar.getTooltipBounds(), 80.0F, 120.0F, 200.0F, 20.0F);
    }

    @Test
    void relativeBoundsInsidePanelResolveAgainstPanel() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        ProgressBar progressBar = new ProgressBar(sketch, "bar", ControlBounds.relative(0.25F, 0.5F, 0.5F, 0.1F));
        panel.add(progressBar);

        TooltipBounds bounds = panel.tooltipTarget(progressBar).getTooltipBounds();

        assertBounds(bounds, 200.0F, 180.0F, 100.0F, 20.0F);
    }

    @Test
    void setPositionAfterRelativeBoundsMakesPositionAbsoluteAndKeepsRelativeSize() {
        ProgressBar progressBar = new ProgressBar(
                sketch(800, 600),
                "bar",
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );

        progressBar.setPosition(300.0F, 240.0F);

        assertBounds(progressBar.getTooltipBounds(), 300.0F, 240.0F, 300.0F, 120.0F);
    }

    @Test
    void setSizeAfterRelativeBoundsMakesSizeAbsoluteAndKeepsRelativePosition() {
        ProgressBar progressBar = new ProgressBar(
                sketch(800, 600),
                "bar",
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );

        progressBar.setSize(160.0F, 40.0F);

        assertBounds(progressBar.getTooltipBounds(), 200.0F, 60.0F, 160.0F, 40.0F);
    }

    @Test
    void implementsTooltipAttachableAndIsNotInteractive() {
        Object progressBar = new ProgressBar(sketch(800, 600), 40.0F, 50.0F, 200.0F, 20.0F);

        assertInstanceOf(TooltipAttachable.class, progressBar);
        assertFalse(progressBar instanceof PointerRoutableControl);
        assertFalse(progressBar instanceof KeyboardRoutableControl);
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
        private final int[] fillColors = new int[8];
        private final float[] rectX = new float[8];
        private final float[] rectY = new float[8];
        private final float[] rectWidth = new float[8];
        private final float[] rectHeight = new float[8];
        private int rectCalls;
        private int fillCalls;
        private int strokeCalls;
        private int noStrokeCalls;
        private int noFillCalls;
        private int lastStrokeColor;
        private float lastStrokeWeight;

        @Override
        public void pushStyle() {
        }

        @Override
        public void popStyle() {
        }

        @Override
        public void fill(int rgb) {
            this.fillColors[this.fillCalls] = rgb;
            this.fillCalls++;
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
        public void rect(float a, float b, float c, float d) {
            this.rectX[this.rectCalls] = a;
            this.rectY[this.rectCalls] = b;
            this.rectWidth[this.rectCalls] = c;
            this.rectHeight[this.rectCalls] = d;
            this.rectCalls++;
        }
    }
}
