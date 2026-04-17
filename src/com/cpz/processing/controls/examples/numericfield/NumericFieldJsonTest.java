package com.cpz.processing.controls.examples.numericfield;

import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.numericfield.NumericFieldFactory;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfig;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldConfigLoader;
import com.cpz.processing.controls.controls.numericfield.input.NumericFieldInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;

import java.io.File;
import java.math.BigDecimal;

public class NumericFieldJsonTest extends PApplet {
    private static final String NUMERIC_FIELD_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "numericfield-test.json";

    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private NumericField numericField;
    private String currentText;
    private BigDecimal currentValue;
    private boolean currentValid;

    public void settings() {
        size(760, 340);
        smooth(8);
    }

    public void setup() {
        NumericFieldConfigLoader loader = new NumericFieldConfigLoader(this);
        NumericFieldConfig config = loader.load(NUMERIC_FIELD_CONFIG_PATH);
        numericField = NumericFieldFactory.create(this, config);
        numericField.setChangeListener(value -> {
            System.out.println("NumericField text = " + value);
            refreshState();
        });
        numericField.setValueChangeListener(value -> System.out.println("NumericField value = " + value));
        refreshState();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new NumericFieldInputLayer(0, numericField));
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        numericField.draw();
        fill(180);
        text(numericField.getCode() + " | focused = " + numericField.isFocused() + " | valid = " + currentValid, 380, 170);
        text("text = " + currentText, 380, 200);
        text("value = " + (currentValue == null ? "invalid" : currentValue.toPlainString()), 380, 225);
        text("config-driven numeric field using NumericFieldConfigLoader and NumericFieldFactory", 380, 260);
        text("click to focus | digits, optional leading -, optional . | left/right/home/end move | enter no-op", 380, 285);
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
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

    private void refreshState() {
        currentText = numericField.getText();
        currentValue = numericField.getValue();
        currentValid = numericField.isValid();
    }
}
