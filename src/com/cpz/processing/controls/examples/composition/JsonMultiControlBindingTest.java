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
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

public class JsonMultiControlBindingTest extends PApplet {
    private static final String CONFIG_PATH = "data" + File.separator + "config" + File.separator + "json-multicontrol-binding-test.json";

    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private Map<String, Control> controls;

    private Slider sldValue;
    private NumericField numValue;
    private Label lblCurrentValue;
    private Label lblValidity;

    // Guards bidirectional updates triggered by programmatic synchronization.
    private boolean internalUpdate;

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
        sldValue = requireControl(controls, "sldValue", Slider.class);
        numValue = requireControl(controls, "numValue", NumericField.class);
        lblCurrentValue = requireControl(controls, "lblCurrentValue", Label.class);
        lblValidity = requireControl(controls, "lblValidity", Label.class);

        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, sldValue));
        inputManager.registerLayer(new NumericFieldInputLayer(1, numValue));
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);

        // Binding lives in the sketch, not in JSON.
        sldValue.setChangeListener(value -> syncFromSlider());
        numValue.setChangeListener(value -> syncFromNumericField());

        applyInitialState();
    }

    public void draw() {
        background(28);
        // The common Control contract lets the sketch draw the collection uniformly.
        for (Control control : controls.values()) {
            control.draw();
        }
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

    private void applyInitialState() {
        internalUpdate = true;
        try {
            numValue.setValue(sldValue.getValue());
            // Derived labels also belong to the sketch-side behavior layer.
            refreshDerivedLabels();
        } finally {
            internalUpdate = false;
        }
    }

    private void syncFromSlider() {
        if (internalUpdate) {
            return;
        }

        internalUpdate = true;
        try {
            numValue.setValue(sldValue.getValue());
            refreshDerivedLabels();
        } finally {
            internalUpdate = false;
        }
    }

    private void syncFromNumericField() {
        if (internalUpdate) {
            return;
        }

        internalUpdate = true;
        try {
            if (numValue.isValid()) {
                BigDecimal parsedValue = numValue.getValue();
                if (parsedValue != null) {
                    sldValue.setValue(parsedValue);
                    // Re-apply the normalized slider value so both controls show the same committed value.
                    numValue.setValue(sldValue.getValue());
                }
            }
            refreshDerivedLabels();
        } finally {
            internalUpdate = false;
        }
    }

    private void refreshDerivedLabels() {
        lblCurrentValue.setText("Current value: " + sldValue.getFormattedValue());
        lblValidity.setText(numValue.isValid() ? "Valid value" : "Invalid value");
    }

    private static <T extends Control> T requireControl(Map<String, Control> controls, String code, Class<T> type) {
        Control control = controls.get(code);
        if (control == null) {
            throw new IllegalStateException("Missing required control in example config: " + code);
        }
        if (!type.isInstance(control)) {
            throw new IllegalStateException(
                    "Invalid control type for code '" + code + "'. Expected " + type.getSimpleName()
                            + " but got " + control.getClass().getSimpleName() + "."
            );
        }
        return type.cast(control);
    }
}
