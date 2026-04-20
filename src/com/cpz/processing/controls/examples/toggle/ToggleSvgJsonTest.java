package com.cpz.processing.controls.examples.toggle;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.ToggleFactory;
import com.cpz.processing.controls.controls.toggle.config.ToggleConfig;
import com.cpz.processing.controls.controls.toggle.config.ToggleConfigLoader;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

import java.io.File;

/**
 * @author CPZ
 */
public class ToggleSvgJsonTest extends PApplet {
    private static final String TOGGLE_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "toggle-svg-test.json";

    private InputManager inputManager;
    private Toggle toggle;
    private int currentState;

    public void settings() {
        size(600, 320);
        smooth(8);
    }

    public void setup() {
        ToggleConfigLoader loader = new ToggleConfigLoader(this);
        ToggleConfig config = loader.load(TOGGLE_CONFIG_PATH);
        toggle = ToggleFactory.create(this, config);
        toggle.setChangeListener(value -> currentState = value);
        currentState = toggle.getState();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new ToggleInputLayer(0, toggle));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        toggle.draw();
        text(toggle.getCode() + " | state = " + currentState + " / " + (toggle.getTotalStates() - 1), 300, 215);
        text("click to cycle 0 → 1 → 2 → 0", 300, 250);
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
