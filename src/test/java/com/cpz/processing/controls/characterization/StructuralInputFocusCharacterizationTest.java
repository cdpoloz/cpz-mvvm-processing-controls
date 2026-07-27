package com.cpz.processing.controls.characterization;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputLayer;
import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.numericfield.input.NumericFieldInputLayer;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.radiogroup.input.RadioGroupInputLayer;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.textfield.input.TextFieldInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.testsupport.ProcessingTestSupport;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Holds input-consumption regressions alongside the still-pending focus
 * characterizations. Methods prefixed with {@code current} describe behavior
 * that is intentionally not promoted to the desired contract.
 */
class StructuralInputFocusCharacterizationTest {
    @Test
    void checkboxPressOutsideItsBoundsContinuesToEligibleLowerLayer() {
        PApplet sketch = sketch();
        Checkbox unrelatedTop = new Checkbox(sketch, "top", false, 40.0F, 40.0F, 20.0F);
        Button lower = new Button(sketch, "lower", "Lower", 240.0F, 160.0F, 100.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lower.setClickListener(lowerClicks::incrementAndGet);
        CheckboxInputLayer topLayer = new CheckboxInputLayer(10, unrelatedTop);
        InputManager input = new InputManager();
        input.registerLayer(topLayer);
        input.registerLayer(new ButtonInputLayer(0, lower));

        PointerEvent outsideTopButInsideLower = pressEvent(240.0F, 160.0F);
        assertFalse(unrelatedTop.canConsumePointerEvent(outsideTopButInsideLower));
        assertFalse(topLayer.handlePointerEvent(outsideTopButInsideLower),
                "receiving an out-of-bounds event must not report consumption");

        click(input, 240.0F, 160.0F);

        assertFalse(unrelatedTop.isChecked());
        assertEquals(1, lowerClicks.get(),
                "the geometrically eligible lower control receives the event");
    }

    @Test
    void invisibleCheckboxDoesNotConsumeOutOfBoundsPress() {
        Checkbox checkbox = new Checkbox(sketch(), "hidden", false, 40.0F, 40.0F, 20.0F);
        checkbox.setVisible(false);
        CheckboxInputLayer layer = new CheckboxInputLayer(0, checkbox);
        PointerEvent event = pressEvent(300.0F, 200.0F);

        assertFalse(checkbox.canConsumePointerEvent(event));
        assertFalse(layer.handlePointerEvent(event));
        assertFalse(checkbox.isChecked());
    }

    @Test
    void textFieldOutsidePressReleasesFocusWithoutBlockingLowerLayer() {
        PApplet sketch = sketch();
        TextField field = new TextField(sketch, "field", "", 80.0F, 60.0F, 120.0F, 36.0F);
        Button lower = new Button(sketch, "lower", "Lower", 300.0F, 220.0F, 100.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lower.setClickListener(lowerClicks::incrementAndGet);
        TextFieldInputLayer layer = new TextFieldInputLayer(0, field);
        InputManager input = new InputManager();
        input.registerLayer(layer);
        input.registerLayer(new ButtonInputLayer(-1, lower));

        assertTrue(layer.handlePointerEvent(pressEvent(80.0F, 60.0F)));
        assertTrue(field.isFocused());

        PointerEvent outside = pressEvent(300.0F, 220.0F);
        assertFalse(field.canConsumePointerEvent(outside));
        assertFalse(layer.handlePointerEvent(outside),
                "the facade still receives the press to clear focus, but the layer does not consume it");
        assertFalse(field.isFocused());

        click(input, 300.0F, 220.0F);

        assertEquals(1, lowerClicks.get());
    }

    @Test
    void checkboxInsidePressAndReleaseAreConsumedAndActivateControl() {
        Checkbox checkbox = new Checkbox(sketch(), "checkbox", false, 80.0F, 60.0F, 20.0F);
        CheckboxInputLayer layer = new CheckboxInputLayer(0, checkbox);

        assertTrue(layer.handlePointerEvent(pressEvent(80.0F, 60.0F)));
        assertTrue(layer.handlePointerEvent(new PointerEvent(PointerEvent.Type.RELEASE, 80.0F, 60.0F)));

        assertTrue(checkbox.isChecked());
    }

    @Test
    void textFieldCaptureConsumesDragAndReleaseOutsideAfterEligiblePress() {
        TextField field = new TextField(sketch(), "field", "Selection", 80.0F, 60.0F, 120.0F, 36.0F);
        TextFieldInputLayer layer = new TextFieldInputLayer(0, field);
        PointerEvent outsideDrag = new PointerEvent(PointerEvent.Type.DRAG, 300.0F, 220.0F);
        PointerEvent outsideRelease = new PointerEvent(PointerEvent.Type.RELEASE, 300.0F, 220.0F);

        assertTrue(layer.handlePointerEvent(pressEvent(80.0F, 60.0F)));
        assertTrue(layer.handlePointerEvent(outsideDrag));
        assertTrue(layer.handlePointerEvent(outsideRelease));
        assertFalse(layer.handlePointerEvent(outsideDrag),
                "capture ends with release, so an unrelated later drag is not consumed");
    }

    @Test
    void currentIndependentManagersAllowTwoTextFieldsInOnePanelToRemainFocused() {
        InputManager input = new InputManager();
        Panel panel = new Panel(sketch(), "panel", 0.0F, 0.0F, 400.0F, 220.0F);
        TextField first = new TextField(sketch(), "first", "", 80.0F, 60.0F, 120.0F, 36.0F);
        TextField second = new TextField(sketch(), "second", "", 250.0F, 60.0F, 120.0F, 36.0F);
        panel.add(first).add(second);
        input.registerLayer(new PanelInputLayer(0, panel));

        click(input, 80.0F, 60.0F);
        click(input, 250.0F, 60.0F);

        assertTrue(first.isFocused(),
                "characterization only: routing stops at the second child before the first can clear its local manager");
        assertTrue(second.isFocused(),
                "characterization only: each facade owns an independent FocusManager");
    }

    @Test
    void currentIndependentManagersAllowTextAndNumericFamiliesToRemainFocused() {
        InputManager input = new InputManager();
        Panel panel = new Panel(sketch(), "panel", 0.0F, 0.0F, 400.0F, 220.0F);
        TextField text = new TextField(sketch(), "text", "", 80.0F, 60.0F, 120.0F, 36.0F);
        NumericField number = new NumericField(sketch(), "number", "1", 250.0F, 60.0F, 120.0F, 36.0F);
        panel.add(text).add(number);
        input.registerLayer(new PanelInputLayer(0, panel));

        click(input, 80.0F, 60.0F);
        click(input, 250.0F, 60.0F);

        assertTrue(text.isFocused(),
                "characterization only: focus is not exclusive across facade-local managers");
        assertTrue(number.isFocused());
    }

    @Test
    void clickingEmptyPanelAreaCurrentlyClearsEachVisitedLocalFocusManager() {
        InputManager input = new InputManager();
        Panel panel = new Panel(sketch(), "panel", 0.0F, 0.0F, 400.0F, 220.0F);
        TextField first = new TextField(sketch(), "first", "", 80.0F, 60.0F, 120.0F, 36.0F);
        TextField second = new TextField(sketch(), "second", "", 250.0F, 60.0F, 120.0F, 36.0F);
        panel.add(first).add(second);
        input.registerLayer(new PanelInputLayer(0, panel));

        click(input, 80.0F, 60.0F);
        click(input, 250.0F, 60.0F);
        assertTrue(first.isFocused());
        assertTrue(second.isFocused());

        click(input, 350.0F, 180.0F);

        assertFalse(first.isFocused());
        assertFalse(second.isFocused());
    }

    @Test
    void overlappingControlsInDifferentLayersUseDescendingLayerPriority() {
        InputManager input = new InputManager();
        Button high = new Button(sketch(), "high", "High", 160.0F, 100.0F, 120.0F, 40.0F);
        Button low = new Button(sketch(), "low", "Low", 160.0F, 100.0F, 120.0F, 40.0F);
        AtomicInteger highClicks = new AtomicInteger();
        AtomicInteger lowClicks = new AtomicInteger();
        high.setClickListener(highClicks::incrementAndGet);
        low.setClickListener(lowClicks::incrementAndGet);
        input.registerLayer(new ButtonInputLayer(10, high));
        input.registerLayer(new ButtonInputLayer(0, low));

        click(input, 160.0F, 100.0F);

        assertEquals(1, highClicks.get());
        assertEquals(0, lowClicks.get());
    }

    @Test
    void disabledVisibleTopControlPreservesIntentionalOcclusion() {
        InputManager input = new InputManager();
        Button disabledTop = new Button(sketch(), "top", "Top", 160.0F, 100.0F, 120.0F, 40.0F);
        Button lower = new Button(sketch(), "lower", "Lower", 160.0F, 100.0F, 120.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        disabledTop.setEnabled(false);
        lower.setClickListener(lowerClicks::incrementAndGet);
        input.registerLayer(new ButtonInputLayer(10, disabledTop));
        input.registerLayer(new ButtonInputLayer(0, lower));

        click(input, 160.0F, 100.0F);

        assertEquals(0, lowerClicks.get());
    }

    @Test
    void disabledVisibleCheckboxPreservesIntentionalOcclusion() {
        PApplet sketch = sketch();
        InputManager input = new InputManager();
        Checkbox disabledTop = new Checkbox(sketch, "top", false, 160.0F, 100.0F, 30.0F);
        Button lower = new Button(sketch, "lower", "Lower", 160.0F, 100.0F, 120.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        disabledTop.setEnabled(false);
        lower.setClickListener(lowerClicks::incrementAndGet);
        input.registerLayer(new CheckboxInputLayer(10, disabledTop));
        input.registerLayer(new ButtonInputLayer(0, lower));

        click(input, 160.0F, 100.0F);

        assertFalse(disabledTop.isChecked());
        assertEquals(0, lowerClicks.get());
    }

    @Test
    void hiddenButtonLayerDoesNotBlockOverlappingLowerAction() {
        InputManager input = new InputManager();
        Button hiddenTop = new Button(sketch(), "top", "Top", 160.0F, 100.0F, 120.0F, 40.0F);
        Button lower = new Button(sketch(), "lower", "Lower", 160.0F, 100.0F, 120.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        hiddenTop.setVisible(false);
        lower.setClickListener(lowerClicks::incrementAndGet);
        input.registerLayer(new ButtonInputLayer(10, hiddenTop));
        input.registerLayer(new ButtonInputLayer(0, lower));

        click(input, 160.0F, 100.0F);

        assertEquals(1, lowerClicks.get());
    }

    @Test
    void unfocusedRadioGroupDoesNotConsumeKeyboardEvent() {
        RadioGroup group = new RadioGroup(
                sketch(),
                "group",
                List.of("One", "Two"),
                0,
                80.0F,
                60.0F,
                160.0F
        );
        RadioGroupInputLayer layer = new RadioGroupInputLayer(0, group);
        KeyboardEvent key = new KeyboardEvent(KeyboardEvent.Type.PRESS, '\0', PApplet.DOWN, false, false, false);

        assertFalse(group.canConsumeKeyboardEvent(key));
        assertFalse(layer.handleKeyboardEvent(key));
        assertEquals(0, group.getSelectedIndex());
    }

    @Test
    void focusedRadioGroupConsumesKeyboardEventAndMovesSelection() {
        RadioGroup group = new RadioGroup(
                sketch(),
                "group",
                List.of("One", "Two"),
                0,
                80.0F,
                60.0F,
                160.0F
        );
        RadioGroupInputLayer layer = new RadioGroupInputLayer(0, group);
        KeyboardEvent key = new KeyboardEvent(KeyboardEvent.Type.PRESS, '\0', PApplet.DOWN, false, false, false);
        KeyboardEvent commit = new KeyboardEvent(KeyboardEvent.Type.PRESS, '\n', PApplet.ENTER, false, false, false);

        assertTrue(layer.handlePointerEvent(pressEvent(80.0F, 60.0F)));
        assertTrue(group.canConsumeKeyboardEvent(key));
        assertTrue(layer.handleKeyboardEvent(key));
        assertTrue(layer.handleKeyboardEvent(commit));

        assertEquals(1, group.getSelectedIndex());
    }

    @Test
    void numericFieldReleaseOutsideWithoutCaptureDoesNotBlockLowerLayer() {
        PApplet sketch = sketch();
        NumericField field = new NumericField(sketch, "number", "1", 80.0F, 60.0F, 120.0F, 36.0F);
        Button lower = new Button(sketch, "lower", "Lower", 300.0F, 220.0F, 100.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lower.setClickListener(lowerClicks::incrementAndGet);
        InputManager input = new InputManager();
        input.registerLayer(new NumericFieldInputLayer(10, field));
        input.registerLayer(new ButtonInputLayer(0, lower));

        click(input, 300.0F, 220.0F);

        assertEquals(1, lowerClicks.get());
        assertFalse(field.isFocused());
    }

    @Test
    void radioGroupPressOutsideItsBoundsContinuesToEligibleLowerLayer() {
        PApplet sketch = sketch();
        RadioGroup group = new RadioGroup(
                sketch,
                "group",
                List.of("One", "Two"),
                0,
                80.0F,
                60.0F,
                160.0F
        );
        Button lower = new Button(sketch, "lower", "Lower", 300.0F, 220.0F, 100.0F, 40.0F);
        AtomicInteger lowerClicks = new AtomicInteger();
        lower.setClickListener(lowerClicks::incrementAndGet);
        InputManager input = new InputManager();
        input.registerLayer(new RadioGroupInputLayer(10, group));
        input.registerLayer(new ButtonInputLayer(0, lower));

        click(input, 300.0F, 220.0F);

        assertEquals(1, lowerClicks.get());
        assertFalse(group.canConsumePointerEvent(pressEvent(300.0F, 220.0F)));
    }

    private static PApplet sketch() {
        PApplet sketch = new PApplet();
        sketch.width = 640;
        sketch.height = 480;
        ProcessingTestSupport.graphics(sketch);
        return sketch;
    }

    private static PointerEvent pressEvent(float x, float y) {
        return new PointerEvent(PointerEvent.Type.PRESS, x, y);
    }

    private static void click(InputManager input, float x, float y) {
        input.dispatchPointer(pressEvent(x, y));
        input.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, x, y));
    }
}
