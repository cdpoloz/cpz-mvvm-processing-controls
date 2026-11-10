package com.cpz.processing.controls.core.overlay.tooltip;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.MeasureMode;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.model.TooltipModel;
import com.cpz.processing.controls.core.overlay.tooltip.style.DefaultTooltipStyle;
import com.cpz.processing.controls.core.overlay.tooltip.view.TooltipView;
import com.cpz.processing.controls.core.overlay.tooltip.viewmodel.TooltipViewModel;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

class TooltipRelativeStyleTest {
    @Test
    void relativeStyleResizesMeasurementPositionAndRenderingTogether() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.width = 1200;
        sketch.height = 600;
        ProcessingTestSupport.graphics(sketch);
        TooltipStyleConfig config = new TooltipStyleConfig()
                .setTextSize(ControlMeasure.relative(0.02F))
                .setPadding(ControlMeasure.relative(0.01F))
                .setMinHeight(ControlMeasure.relative(0.1F))
                .setMinWidth(ControlMeasure.relative(0.2F))
                .setOffset(ControlMeasure.relative(0.02F))
                .setCornerRadius(ControlMeasure.relative(0.01F))
                .setStrokeWeight(ControlMeasure.relative(0.002F));
        TooltipViewModel vm = new TooltipViewModel(new TooltipModel("Test"));
        vm.setVisible(true);
        TooltipView view = new TooltipView(sketch, vm);
        view.setStyle(new DefaultTooltipStyle(config));
        view.setTargetBounds(new TooltipBounds(500, 300, 100, 20));
        view.draw();
        assertEquals(120, sketch.boxWidth, 0.001F);
        assertEquals(60, sketch.boxHeight, 0.001F);
        assertEquals(228, sketch.boxY, 0.001F); // target top - offset - height
        assertEquals(sketch.boxX + 6, sketch.textX, 0.001F);
        assertEquals(12, sketch.measuredTextSize, 0.001F);
        assertEquals(12, sketch.drawnTextSize, 0.001F);
        assertEquals(6, sketch.radius, 0.001F);
        assertEquals(1.2F, sketch.stroke, 0.001F);

        sketch.height = 1000;
        view.draw();
        assertEquals(200, sketch.boxWidth, 0.001F);
        assertEquals(100, sketch.boxHeight, 0.001F);
        assertEquals(180, sketch.boxY, 0.001F);
        assertEquals(sketch.boxX + 10, sketch.textX, 0.001F);
        assertEquals(20, sketch.measuredTextSize, 0.001F);
        assertEquals(20, sketch.drawnTextSize, 0.001F);
        assertEquals(10, sketch.radius, 0.001F);
        assertEquals(2, sketch.stroke, 0.001F);
        assertEquals(0.02F, config.getTextSizeMeasure().value());
        assertEquals(MeasureMode.RELATIVE, config.getTextSizeMeasure().mode());
    }

    @Test
    void legacyAnchorAlsoRepositionsOnResizeUsingConfiguredOffset() {
        RecordingApplet sketch = new RecordingApplet();
        sketch.height = 500;
        ProcessingTestSupport.graphics(sketch);
        TooltipViewModel vm = new TooltipViewModel(new TooltipModel("Test"));
        vm.setVisible(true);
        TooltipView view = new TooltipView(sketch, vm);
        view.setStyle(new DefaultTooltipStyle(new TooltipStyleConfig()
                .setMinHeight(ControlMeasure.relative(0.1F))
                .setOffset(ControlMeasure.relative(0.02F))));
        view.setAnchorBounds(200, 300);
        view.draw();
        assertEquals(240, sketch.boxY, 0.001F);
        sketch.height = 1000;
        view.draw();
        assertEquals(180, sketch.boxY, 0.001F);
    }

    @Test
    void copiesAndResetPreserveIsolationAndFloatSettersReplaceRelativeMeasures() {
        TooltipStyleConfig shared = new TooltipStyleConfig().setTextSize(ControlMeasure.relative(0.02F));
        Tooltip first = new Tooltip("First", shared);
        Tooltip second = new Tooltip("Second", shared);
        first.setTextSize(25);
        shared.setTextSize(30);
        assertEquals(MeasureMode.ABSOLUTE, first.getStyleConfig().getTextSizeMeasure().mode());
        assertEquals(0.02F, second.getStyleConfig().getTextSizeMeasure().value());
        second.getStyleConfig().reset();
        assertEquals(14, second.getStyleConfig().getTextSizeMeasure().value());
        assertEquals(MeasureMode.ABSOLUTE, second.getStyleConfig().getTextSizeMeasure().mode());
        assertThrows(IllegalArgumentException.class, () -> ControlMeasure.relative(Float.NaN));
        assertThrows(NullPointerException.class, () -> first.setOffset((ControlMeasure) null));
    }

    private static final class RecordingApplet extends PApplet {
        float boxX, boxY, boxWidth, boxHeight, radius, stroke, textX, measuredTextSize, drawnTextSize;

        @Override
        public float textWidth(String text) {
            measuredTextSize = g.textSize;
            return text.length() * g.textSize * 0.5F;
        }

        @Override
        public void rect(float x, float y, float width, float height, float radius) {
            boxX = x; boxY = y; boxWidth = width; boxHeight = height; this.radius = radius;
        }

        @Override
        public void strokeWeight(float weight) {
            stroke = weight;
        }

        @Override
        public void text(String text, float x, float y) {
            textX = x;
            drawnTextSize = g.textSize;
        }
    }
}
