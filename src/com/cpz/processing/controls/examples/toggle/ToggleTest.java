package com.cpz.processing.controls.examples.toggle;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputLayer;
import com.cpz.processing.controls.controls.toggle.style.ParametricToggleStyle;
import com.cpz.processing.controls.controls.toggle.style.render.CircleShapeRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class ToggleTest extends PApplet {
    private InputManager inputManager;
    private Toggle toggle;
    private int currentState;

    public void settings() {
        size(600, 320);
        smooth(8);

    }

    public void setup() {
        float x = 300f;
        float y = 125f;
        float d = 100f;
        toggle = new Toggle(this, "tglTest", 0, 3, x, y, d, d);
        toggle.setChangeListener(value -> {
            // the code that executes after a toggle click goes here, for example:
            System.out.println("Toggle state = " + value);
            currentState = value;
        });
        currentState = toggle.getState();
        // style
        ToggleStyleConfig tsc = new ToggleStyleConfig();
        tsc.setShapeRenderer(new CircleShapeRenderer());
        tsc.stateColors = new Integer[]{
                Colors.gray(70),
                Colors.rgb(232, 155, 44),
                Colors.rgb(32, 188, 176)
        };
        tsc.strokeColor = Colors.gray(255);
        tsc.strokeWidth = 2.0f;
        tsc.strokeWidthHover = 4.0f;
        tsc.hoverBlendWithWhite = 0.18f;
        tsc.pressedBlendWithBlack = 0.20f;
        tsc.disabledAlpha = 70;
        toggle.setStyle(new ParametricToggleStyle(tsc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new ToggleInputLayer(0, toggle));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        toggle.draw();
        text(toggle.getCode() + " | state = " + currentState + " / " + (toggle.getTotalStates() - 1), 300, 210);
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
