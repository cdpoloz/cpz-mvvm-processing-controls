package com.cpz.processing.controls.examples.toggle;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;

/**
 * Minimal sketch to verify that toggle hit testing matches the full control bounds.
 *
 * @author CPZ
 */
public class ToggleHitAreaSketch extends PApplet {
    private static final float TOGGLE_X = 703f;
    private static final float TOGGLE_Y = 87f;
    private static final float TOGGLE_WIDTH = 123.5f;
    private static final float TOGGLE_HEIGHT = 27.5f;

    private InputManager inputManager;
    private Toggle toggle;
    private int currentState;

    public void settings() {
        size(900, 180);
        smooth(8);
    }

    public void setup() {
        toggle = new Toggle(this, "tglHitArea", 1, 2, TOGGLE_X, TOGGLE_Y, TOGGLE_WIDTH, TOGGLE_HEIGHT);
        toggle.setChangeListener(value -> {
            currentState = value;
            System.out.println("Toggle state = " + value);
        });
        currentState = toggle.getState();

        inputManager = new InputManager();
        inputManager.registerLayer(new ToggleInputLayer(0, toggle));

        textAlign(CENTER, CENTER);
        textSize(12f);
    }

    public void draw() {
        background(28);
        stroke(255, 140, 90);
        strokeWeight(1.5f);
        noFill();
        rectMode(CENTER);
        rect(TOGGLE_X, TOGGLE_Y, TOGGLE_WIDTH, TOGGLE_HEIGHT);

        toggle.draw();

        fill(240);
        noStroke();
        text("Click anywhere inside the outlined rectangle to toggle 0/1", width * 0.5f, 135f);
        text("state = " + currentState + " | bounds: [" + (TOGGLE_X - TOGGLE_WIDTH * 0.5f) + ", " + (TOGGLE_Y - TOGGLE_HEIGHT * 0.5f) + "] -> [" + (TOGGLE_X + TOGGLE_WIDTH * 0.5f) + ", " + (TOGGLE_Y + TOGGLE_HEIGHT * 0.5f) + "]", width * 0.5f, 155f);
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
