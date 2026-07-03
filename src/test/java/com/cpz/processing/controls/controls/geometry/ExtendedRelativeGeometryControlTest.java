package com.cpz.processing.controls.controls.geometry;

import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtendedRelativeGeometryControlTest {
    @Test
    void absoluteBoundsKeepExistingCenterBasedTooltipBehavior() {
        PApplet sketch = sketch(800, 600);

        assertBounds(new Checkbox(sketch, true, 120.0F, 90.0F, 30.0F).getTooltipBounds(),
                105.0F, 75.0F, 30.0F, 30.0F);
        assertBounds(new Toggle(sketch, "toggle", 120.0F, 90.0F, 80.0F, 30.0F).getTooltipBounds(),
                80.0F, 75.0F, 80.0F, 30.0F);
        assertBounds(new Slider(sketch, "slider", 120.0F, 90.0F, 80.0F, 30.0F).getTooltipBounds(),
                80.0F, 75.0F, 80.0F, 30.0F);
        assertBounds(new TextField(sketch, "text", "Text", 120.0F, 90.0F, 80.0F, 30.0F).getTooltipBounds(),
                80.0F, 75.0F, 80.0F, 30.0F);
        assertBounds(new NumericField(sketch, "num", "12", 120.0F, 90.0F, 80.0F, 30.0F).getTooltipBounds(),
                80.0F, 75.0F, 80.0F, 30.0F);
        assertBounds(new DropDown(sketch, new OverlayManager(), new InputManager(), "dd", List.of("A"), 120.0F, 90.0F, 80.0F, 30.0F).getTooltipBounds(),
                80.0F, 75.0F, 80.0F, 30.0F);
    }

    @Test
    void relativeBoundsResolveAgainstCanvasForExtendedControls() {
        PApplet sketch = sketch(800, 600);
        ControlBounds bounds = ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F);

        assertBounds(new Checkbox(sketch, true, bounds).getTooltipBounds(),
                50.0F, 0.0F, 300.0F, 120.0F);
        assertBounds(new Toggle(sketch, "toggle", bounds).getTooltipBounds(),
                50.0F, 0.0F, 300.0F, 120.0F);
        assertBounds(new Slider(sketch, "slider", bounds).getTooltipBounds(),
                50.0F, 0.0F, 300.0F, 120.0F);
        assertBounds(new TextField(sketch, "text", "Text", bounds).getTooltipBounds(),
                50.0F, 0.0F, 300.0F, 120.0F);
        assertBounds(new NumericField(sketch, "num", "12", bounds).getTooltipBounds(),
                50.0F, 0.0F, 300.0F, 120.0F);
        assertBounds(new DropDown(sketch, new OverlayManager(), new InputManager(), "dd", List.of("A"), bounds).getTooltipBounds(),
                50.0F, 0.0F, 300.0F, 120.0F);
    }

    @Test
    void checkboxRelativeInsidePanelRoutesInputAndTooltip() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Checkbox checkbox = new Checkbox(sketch, "check", false, ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        panel.add(checkbox);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 200.0F, 180.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 200.0F, 180.0F));
        TooltipBounds bounds = panel.tooltipTarget(checkbox).getTooltipBounds();

        assertTrue(checkbox.isChecked());
        assertBounds(bounds, 175.0F, 170.0F, 50.0F, 20.0F);
    }

    @Test
    void toggleRelativeInsidePanelRoutesInputAndTooltip() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Toggle toggle = new Toggle(sketch, "toggle", ControlBounds.relative(0.25F, 0.5F, 0.25F, 0.1F));
        panel.add(toggle);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 200.0F, 180.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 200.0F, 180.0F));
        TooltipBounds bounds = panel.tooltipTarget(toggle).getTooltipBounds();

        assertEquals(1, toggle.getState());
        assertBounds(bounds, 175.0F, 170.0F, 50.0F, 20.0F);
    }

    @Test
    void sliderRelativeInsidePanelRoutesInput() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        Slider slider = new Slider(sketch, "slider", ControlBounds.relative(0.5F, 0.5F, 0.5F, 0.1F));
        panel.add(slider);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 300.0F, 180.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 300.0F, 180.0F));

        assertEquals("0.50", slider.getFormattedValue());
    }

    @Test
    void textFieldRelativeInsidePanelKeepsKeyboardRouting() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        TextField textField = new TextField(sketch, "text", "", ControlBounds.relative(0.5F, 0.5F, 0.5F, 0.2F));
        panel.add(textField);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 300.0F, 180.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 300.0F, 180.0F));
        inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, 'a', 65, false, false, false));

        assertTrue(textField.isFocused());
        assertEquals("a", textField.getText());
    }

    @Test
    void numericFieldRelativeInsidePanelKeepsKeyboardRouting() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        NumericField numericField = new NumericField(sketch, "num", "", ControlBounds.relative(0.5F, 0.5F, 0.5F, 0.2F));
        panel.add(numericField);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 300.0F, 180.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 300.0F, 180.0F));
        inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, '7', 55, false, false, false));

        assertTrue(numericField.isFocused());
        assertEquals("7", numericField.getText());
    }

    @Test
    void radioGroupRelativeInsidePanelRoutesSelectionAndTooltip() {
        PApplet sketch = sketch(800, 600);
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        RadioGroup radioGroup = new RadioGroup(sketch, "radio", List.of("A", "B"), ControlBounds.relative(0.5F, 0.25F, 0.5F, 0.2F));
        panel.add(radioGroup);
        InputManager inputManager = new InputManager();
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 300.0F, 130.0F));
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, 300.0F, 130.0F));
        TooltipBounds bounds = panel.tooltipTarget(radioGroup).getTooltipBounds();

        assertEquals(0, radioGroup.getSelectedIndex());
        assertEquals(250.0F, bounds.x());
        assertEquals(110.0F, bounds.y());
        assertEquals(100.0F, bounds.width());
        assertEquals(88.0F, bounds.height());
    }

    @Test
    void setPositionAfterRelativeBoundsMakesExtendedControlPositionAbsoluteAndKeepsRelativeSize() {
        PApplet sketch = sketch(800, 600);
        Checkbox checkbox = new Checkbox(sketch, false, ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));

        checkbox.setPosition(300.0F, 240.0F);
        TooltipBounds bounds = checkbox.getTooltipBounds();

        assertBounds(bounds, 150.0F, 180.0F, 300.0F, 120.0F);
    }

    @Test
    void setSizeAfterRelativeBoundsMakesExtendedControlSizeAbsoluteAndKeepsRelativePosition() {
        PApplet sketch = sketch(800, 600);
        Checkbox checkbox = new Checkbox(sketch, false, ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F));

        checkbox.setSize(160.0F, 40.0F);
        TooltipBounds bounds = checkbox.getTooltipBounds();

        assertBounds(bounds, 120.0F, 40.0F, 160.0F, 40.0F);
    }

    @Test
    void dropDownRelativeRootRoutesInput() {
        PApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        DropDown dropDown = new DropDown(
                sketch,
                new OverlayManager(),
                inputManager,
                "dd",
                List.of("A", "B"),
                ControlBounds.relative(0.25F, 0.1F, 0.5F, 0.2F)
        );
        DropDownInputLayer layer = new DropDownInputLayer(0, dropDown);

        boolean consumed = layer.handlePointerEvent(new PointerEvent(PointerEvent.Type.PRESS, 200.0F, 60.0F));

        assertTrue(consumed);
        assertTrue(dropDown.isExpanded());
    }

    @Test
    void dropDownInsidePanelIsNotRoutedByPanelInputLayerYet() {
        PApplet sketch = sketch(800, 600);
        InputManager inputManager = new InputManager();
        Panel panel = new Panel(sketch, "panel", 100.0F, 80.0F, 400.0F, 200.0F);
        DropDown dropDown = new DropDown(
                sketch,
                new OverlayManager(),
                inputManager,
                "dd",
                List.of("A", "B"),
                ControlBounds.relative(0.5F, 0.5F, 0.5F, 0.2F)
        );
        panel.add(dropDown);
        inputManager.registerLayer(new PanelInputLayer(0, panel));

        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, 300.0F, 180.0F));

        assertFalse(dropDown.isExpanded());
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
}
