package com.cpz.processing.controls.controls.geometry;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelativeTextSizeControlTest {
    @Test
    void labelTextSizeFloatIsAbsoluteAfterRelativeMeasure() {
        PApplet sketch = sketch(800, 600);
        Label label = new Label(sketch, "lbl", "Label", 100.0F, 80.0F, 120.0F, 30.0F);

        label.setTextSize(ControlMeasure.relative(0.05F));
        label.setTextSize(18.0F);

        assertEquals(18.0F, label.getStyleConfig().textSize);
    }

    @Test
    void relativeTextSizeResolvesAgainstCanvasForTextControls() {
        assertDrawAppliesTextSize(sketch -> {
            Button button = new Button(sketch, "btn", "Button", 100.0F, 80.0F, 120.0F, 30.0F);
            button.setTextSize(ControlMeasure.relative(0.05F));
            button.draw();
        }, 30.0F);

        assertDrawAppliesTextSize(sketch -> {
            Label label = new Label(sketch, "lbl", "Label", 100.0F, 80.0F, 120.0F, 30.0F);
            label.setTextSize(ControlMeasure.relative(0.05F));
            label.draw();
        }, 30.0F);

        assertDrawAppliesTextSize(sketch -> {
            TextField textField = new TextField(sketch, "txt", "Text", 100.0F, 80.0F, 120.0F, 30.0F);
            textField.setTextSize(ControlMeasure.relative(0.05F));
            textField.draw();
        }, 30.0F);

        assertDrawAppliesTextSize(sketch -> {
            NumericField numericField = new NumericField(sketch, "num", "12", 100.0F, 80.0F, 120.0F, 30.0F);
            numericField.setTextSize(ControlMeasure.relative(0.05F));
            numericField.draw();
        }, 30.0F);

        assertDrawAppliesTextSize(sketch -> {
            RadioGroup radioGroup = new RadioGroup(sketch, "radio", List.of("A"), 100.0F, 80.0F, 120.0F);
            radioGroup.setTextSize(ControlMeasure.relative(0.05F));
            radioGroup.draw();
        }, 30.0F);

        assertDrawAppliesTextSize(sketch -> {
            DropDown dropDown = new DropDown(
                    sketch,
                    new OverlayManager(),
                    new InputManager(),
                    "dd",
                    List.of("A", "B"),
                    0,
                    100.0F,
                    80.0F,
                    120.0F,
                    30.0F
            );
            dropDown.setTextSize(ControlMeasure.relative(0.05F));
            dropDown.draw();
        }, 30.0F);

        assertDrawAppliesTextSize(sketch -> {
            Slider slider = new Slider(sketch, "slider", 100.0F, 80.0F, 120.0F, 30.0F);
            slider.setTextSize(ControlMeasure.relative(0.05F));
            slider.draw();
        }, 30.0F);
    }

    @Test
    void relativeTextSizeInsidePanelUsesPanelHeight() {
        assertDrawAppliesTextSize(sketch -> {
            Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
            Button button = new Button(sketch, "btn", "Button", 40.0F, 40.0F, 120.0F, 30.0F);
            button.setTextSize(ControlMeasure.relative(0.1F));
            panel.add(button);

            panel.tooltipTarget(button).getTooltipBounds();
            button.draw();
        }, 20.0F);
    }

    @Test
    void setTextSizeFloatAfterRelativeMeasureMakesButtonTextSizeAbsolute() {
        assertDrawAppliesTextSize(sketch -> {
            Button button = new Button(sketch, "btn", "Button", 100.0F, 80.0F, 120.0F, 30.0F);
            button.setTextSize(ControlMeasure.relative(0.05F));
            button.setTextSize(18.0F);

            button.draw();
        }, 18.0F);
    }

    @Test
    void setTextSizeFloatDoesNotInferRelativeValues() {
        assertDrawAppliesTextSize(sketch -> {
            Button button = new Button(sketch, "btn", "Button", 100.0F, 80.0F, 120.0F, 30.0F);
            button.setTextSize(0.05F);

            button.draw();
        }, 0.05F);
    }

    @Test
    void absoluteTextSizeUsesGivenValue() {
        assertDrawAppliesTextSize(sketch -> {
            TextField textField = new TextField(sketch, "txt", "Text", 100.0F, 80.0F, 120.0F, 30.0F);
            textField.setTextSize(18.0F);

            textField.draw();
        }, 18.0F);
    }

    @Test
    void sharedButtonStyleConfigDoesNotReceiveResolvedTextSizeOverride() {
        PApplet sketch = sketch(800, 600);
        ButtonStyleConfig sharedConfig = new ButtonStyleConfig();
        sharedConfig.textSize = 14.0F;
        Button normal = new Button(sketch, "btnNormal", "Normal", 100.0F, 80.0F, 120.0F, 30.0F);
        Button scaled = new Button(sketch, "btnScaled", "Scaled", 260.0F, 80.0F, 120.0F, 30.0F);
        normal.setStyle(new DefaultButtonStyle(sharedConfig));
        scaled.setStyle(new DefaultButtonStyle(sharedConfig));
        scaled.setTextSize(ControlMeasure.relative(0.05F));

        assertDrawAppliesTextSize(sketch, normal::draw, 14.0F);
        assertDrawAppliesTextSize(sketch, scaled::draw, 30.0F);
        assertEquals(14.0F, sharedConfig.textSize);
    }

    @Test
    void resizingPanelUpdatesRelativeChildTextSize() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Button button = new Button(sketch, "btn", "Button", 40.0F, 40.0F, 120.0F, 30.0F);
        button.setTextSize(ControlMeasure.relative(0.1F));
        panel.add(button);

        panel.tooltipTarget(button).getTooltipBounds();
        assertDrawAppliesTextSize(sketch, button::draw, 20.0F);

        panel.setSize(400.0F, 120.0F);
        panel.tooltipTarget(button).getTooltipBounds();
        assertDrawAppliesTextSize(sketch, button::draw, 12.0F);
    }

    @Test
    void absoluteTextSizeIsStableWhenPanelHeightChanges() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Button button = new Button(sketch, "btn", "Button", 40.0F, 40.0F, 120.0F, 30.0F);
        button.setTextSize(18.0F);
        panel.add(button);

        panel.tooltipTarget(button).getTooltipBounds();
        assertDrawAppliesTextSize(sketch, button::draw, 18.0F);

        panel.setSize(400.0F, 120.0F);
        panel.tooltipTarget(button).getTooltipBounds();
        assertDrawAppliesTextSize(sketch, button::draw, 18.0F);
    }

    @Test
    void sliderValueTextRelativeUsesCanvasHeight() {
        assertDrawAppliesTextSize(sketch -> {
            Slider slider = new Slider(sketch, "slider", 100.0F, 80.0F, 120.0F, 30.0F);
            slider.setTextSize(ControlMeasure.relative(0.04F));

            slider.draw();
        }, 24.0F);
    }

    private static void assertDrawAppliesTextSize(Consumer<PApplet> draw, float expectedSize) {
        PApplet sketch = sketch(800, 600);
        assertDrawAppliesTextSize(sketch, () -> draw.accept(sketch), expectedSize);
    }

    private static void assertDrawAppliesTextSize(PApplet sketch, Runnable draw, float expectedSize) {
        ProcessingTestSupport.RecordingGraphics graphics = ProcessingTestSupport.graphics(sketch);
        graphics.textFont(ProcessingTestSupport.font("Dialog", 13), 13.0F);
        graphics.clearTypographyHistory();

        draw.run();

        assertTrue(
                graphics.appliedSizes().contains(expectedSize),
                () -> "Expected text size " + expectedSize + " in " + graphics.appliedSizes()
        );
    }

    private static PApplet sketch(int width, int height) {
        PApplet sketch = new PApplet();
        sketch.width = width;
        sketch.height = height;
        return sketch;
    }
}
