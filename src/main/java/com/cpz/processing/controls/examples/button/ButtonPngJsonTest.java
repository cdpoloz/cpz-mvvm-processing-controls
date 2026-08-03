package com.cpz.processing.controls.examples.button;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import java.io.File;
import java.util.Map;
import processing.core.PApplet;

/**
 * Visual JSON example for PNG alpha-mask buttons.
 *
 * @author CPZ
 */
public class ButtonPngJsonTest extends PApplet {
    private static final String CONFIG_PATH = "data" + File.separator + "config" + File.separator + "button-png-test.json";

    private InputManager inputManager;
    private Map<String, Control> controls;

    public static void main(String[] args) {
        PApplet.main(ButtonPngJsonTest.class);
    }

    public void settings() {
        size(720, 320);
        smooth(8);
    }

    public void setup() {
        this.controls = new ControlConfigLoader(this).load(CONFIG_PATH);
        Button activeButton = (Button) this.controls.get("btnPngJson");
        Button disabledButton = (Button) this.controls.get("btnPngDisabledJson");
        activeButton.setClickListener(() -> System.out.println("You clicked the JSON PNG button!"));

        this.inputManager = new InputManager();
        this.inputManager.registerLayer(new ButtonInputLayer(0, activeButton, disabledButton));
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.controls.values().forEach(Control::draw);

        fill(220);
        text("JSON normal / hover / pressed", 210.0F, 225.0F);
        text("JSON disabled", 510.0F, 225.0F);
        fill(150);
        text("Loaded from data/config/button-png-test.json", width * 0.5F, 275.0F);
    }

    public void mouseMoved() {
        this.dispatchPointer(PointerEvent.Type.MOVE);
    }

    public void mouseDragged() {
        this.dispatchPointer(PointerEvent.Type.DRAG);
    }

    public void mousePressed() {
        this.dispatchPointer(PointerEvent.Type.PRESS);
    }

    public void mouseReleased() {
        this.dispatchPointer(PointerEvent.Type.RELEASE);
    }

    private void dispatchPointer(PointerEvent.Type type) {
        this.inputManager.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }
}
