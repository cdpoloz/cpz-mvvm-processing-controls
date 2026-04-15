package com.cpz.processing.controls.examples;

import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputLayer;
import com.cpz.processing.controls.controls.checkbox.style.DefaultCheckboxStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public class CheckboxTest extends PApplet {
    private InputManager inputManager;
    private Checkbox checkbox;
    private boolean currentValue;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 125f;
        float w = 42f;
        float h = 42f;
        checkbox = new Checkbox(this, "chkTest", true, x, y, w, h);
        checkbox.setChangeListener(value -> currentValue = value);
        currentValue = checkbox.isChecked();
        // style
        CheckboxStyleConfig csc = new CheckboxStyleConfig();
        csc.boxColor = Colors.rgb(48, 98, 219);
        csc.boxHoverColor = Colors.rgb(74, 122, 234);
        csc.boxPressedColor = Colors.rgb(34, 77, 184);
        csc.checkColor = Colors.gray(255);
        csc.borderColor = Colors.gray(255);
        csc.borderWidth = 2.0f;
        csc.borderWidthHover = 4.0f;
        csc.cornerRadius = 10.0f;
        csc.disabledAlpha = 90;
        csc.checkInset = 0.20f;
        checkbox.setStyle(new DefaultCheckboxStyle(csc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new CheckboxInputLayer(0, checkbox));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        checkbox.draw();
        text(checkbox.getCode() + " | Current checked state = " + currentValue, 300, 200);
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
}
