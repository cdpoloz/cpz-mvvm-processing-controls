package com.cpz.processing.controls.examples.button;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.ButtonFactory;
import com.cpz.processing.controls.controls.button.config.ButtonConfig;
import com.cpz.processing.controls.controls.button.config.ButtonConfigLoader;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

import java.io.File;

/**
 * @author CPZ
 */
public class ButtonSvgJsonTest extends PApplet {
    private static final String BUTTON_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "button-svg-test.json";

    private InputManager inputManager;
    private Button button;
    private int clickCount;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        ButtonConfigLoader loader = new ButtonConfigLoader(this);
        ButtonConfig config = loader.load(BUTTON_CONFIG_PATH);
        button = ButtonFactory.create(this, config);
        button.setClickListener(() -> {
            System.out.println("You clicked the SVG JSON button!");
            clickCount++;
        });
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new ButtonInputLayer(0, button));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        button.draw();
        text(button.getCode() + " | Current click count = " + clickCount, 300, 225);
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
