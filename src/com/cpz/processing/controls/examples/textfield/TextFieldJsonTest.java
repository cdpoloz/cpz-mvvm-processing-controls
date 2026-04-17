package com.cpz.processing.controls.examples.textfield;

import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.textfield.TextFieldFactory;
import com.cpz.processing.controls.controls.textfield.config.TextFieldConfig;
import com.cpz.processing.controls.controls.textfield.config.TextFieldConfigLoader;
import com.cpz.processing.controls.controls.textfield.input.TextFieldInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;

import java.io.File;

public class TextFieldJsonTest extends PApplet {
    private static final String TEXT_FIELD_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "textfield-test.json";

    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private TextField textField;
    private String currentText;

    public void settings() {
        size(760, 320);
        smooth(8);
    }

    public void setup() {
        TextFieldConfigLoader loader = new TextFieldConfigLoader(this);
        TextFieldConfig config = loader.load(TEXT_FIELD_CONFIG_PATH);
        textField = TextFieldFactory.create(this, config);
        textField.setChangeListener(value -> {
            // the code that executes after the text field value changes goes here, for example:
            System.out.println("TextField text = " + value);
            currentText = value;
        });
        currentText = textField.getText();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new TextFieldInputLayer(0, textField));
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        textField.draw();
        fill(180);
        text(textField.getCode() + " | focused = " + textField.isFocused(), 380, 220);
        text("text = " + currentText, 380, 250);
        text("config-driven text field using TextFieldConfigLoader and TextFieldFactory", 380, 280);
        text("click the field to focus | type text | backspace/delete edit | left/right/home/end move | enter no-op", 380, 305);
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
}
