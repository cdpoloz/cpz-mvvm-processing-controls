package com.cpz.processing.controls.examples.numericfield;

import com.cpz.processing.controls.controls.numericfield.NumericField;
import com.cpz.processing.controls.controls.numericfield.config.NumericFieldStyleConfig;
import com.cpz.processing.controls.controls.numericfield.input.NumericFieldInputLayer;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;

import java.math.BigDecimal;

public class NumericFieldTest extends PApplet {
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
        float x = 380f;
        float y = 110f;
        float w = 420f;
        float h = 48f;
        numericField = new NumericField(this, "numTest", "12.5", x, y, w, h);
        numericField.setChangeListener(value -> {
            System.out.println("NumericField text = " + value);
            refreshState();
        });
        numericField.setValueChangeListener(value -> System.out.println("NumericField value = " + value));
        refreshState();
        // style
        NumericFieldStyleConfig nfsc = new NumericFieldStyleConfig();
        nfsc.backgroundColor = Colors.rgb(236, 242, 248);
        nfsc.borderColor = Colors.rgb(72, 116, 156);
        nfsc.textColor = Colors.rgb(28, 44, 62);
        nfsc.cursorColor = Colors.rgb(38, 132, 212);
        nfsc.selectionColor = Colors.rgb(182, 217, 248);
        nfsc.selectionTextColor = Colors.rgb(28, 44, 62);
        nfsc.textSize = 16.0f;
        numericField.setStyle(new NumericFieldStyle(nfsc));
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
        text("click to focus | digits, optional leading -, optional . | left/right/home/end move | enter no-op", 380, 260);
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
