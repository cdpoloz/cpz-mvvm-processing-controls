package com.cpz.processing.controls.examples.composition;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.numericfield.input.NumericFieldInputLayer;
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import com.cpz.processing.controls.util.Util;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.io.File;
import java.util.Map;

/**
 * @author CPZ
 */
public class JsonMultiControlUnidirectionalBindingTest extends PApplet {
    private static final String CONFIG_PATH = "data" + File.separator + "config" + File.separator + "json-multicontrol-binding-test.json";

    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private Map<String, Control> controls;

    private Slider sldValue;
    private NumericField numValue;
    private Label lblCurrentValue;

    public void settings() {
        size(860, 360);
        smooth(8);
    }

    public void setup() {
        // JSON defines the control set, layout, style, and base text.
        ControlConfigLoader loader = new ControlConfigLoader(this);
        controls = loader.load(CONFIG_PATH);

        // Not every control loaded from JSON needs a typed reference here.
        // The sketch resolves only the controls that participate in behavior and binding.
        sldValue = Util.getControl(controls, "sldValue", Slider.class);
        numValue = Util.getControl(controls, "numValue", NumericField.class);
        lblCurrentValue = Util.getControl(controls, "lblCurrentValue", Label.class);

        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, sldValue));
        inputManager.registerLayer(new NumericFieldInputLayer(1, numValue));
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);

        // Binding lives in the sketch, not in JSON.
        // Single source of truth: Slider drives the UI.
        sldValue.setChangeListener(value -> syncFromSlider());

        // initial state
        numValue.setValue(sldValue.getValue());
        refreshDerivedLabels();
    }

    public void draw() {
        background(28);
        // The common Control contract lets the sketch draw the collection uniformly.
        controls.values().forEach(Control::draw);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }

    public void keyPressed() {
        processingKeyboardAdapter.keyPressed(key, keyCode);
    }

    public void keyReleased() {
        processingKeyboardAdapter.keyReleased(key, keyCode);
    }

    public void keyTyped() {
        processingKeyboardAdapter.keyTyped(key, keyCode);
    }

    private void syncFromSlider() {
        numValue.setValue(sldValue.getValue());
        refreshDerivedLabels();
    }

    private void refreshDerivedLabels() {
        lblCurrentValue.setText("Current value: " + sldValue.getFormattedValue());
    }

}
