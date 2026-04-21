package com.cpz.processing.controls.examples.textfield;

import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.textfield.config.TextFieldStyleConfig;
import com.cpz.processing.controls.controls.textfield.input.TextFieldInputLayer;
import com.cpz.processing.controls.controls.textfield.style.DefaultTextFieldStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class TextFieldTest extends PApplet {
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
        float x = 380f;
        float y = 110f;
        float w = 420f;
        float h = 48f;
        textField = new TextField(this, "txtTest", "Click to focus", x, y, w, h);
        textField.setChangeListener(value -> {
            // the code that executes after the text field value changes goes here, for example:
            System.out.println("TextField text = " + value);
            currentText = value;
        });
        currentText = textField.getText();
        // style
        TextFieldStyleConfig tfsc = new TextFieldStyleConfig();
        tfsc.backgroundColor = Colors.rgb(236, 242, 248);
        tfsc.borderColor = Colors.rgb(72, 116, 156);
        tfsc.textColor = Colors.rgb(28, 44, 62);
        tfsc.cursorColor = Colors.rgb(38, 132, 212);
        tfsc.selectionColor = Colors.rgb(182, 217, 248);
        tfsc.selectionTextColor = Colors.rgb(28, 44, 62);
        tfsc.textSize = 16.0f;
        textField.setStyle(new DefaultTextFieldStyle(tfsc));
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
        text("click the field to focus | type text | backspace/delete edit | left/right/home/end move | enter no-op", 380, 280);
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
