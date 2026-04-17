package com.cpz.processing.controls.examples.checkbox;

import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.checkbox.CheckboxFactory;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxConfig;
import com.cpz.processing.controls.controls.checkbox.config.CheckboxConfigLoader;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

import java.io.File;

public class CheckboxJsonTest extends PApplet {
    private static final String CHECKBOX_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "checkbox-test.json";

    private InputManager inputManager;
    private Checkbox checkbox;
    private boolean currentValue;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        CheckboxConfigLoader loader = new CheckboxConfigLoader(this);
        CheckboxConfig config = loader.load(CHECKBOX_CONFIG_PATH);
        checkbox = CheckboxFactory.create(this, config);
        checkbox.setChangeListener(value -> currentValue = value);
        currentValue = checkbox.isChecked();
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
