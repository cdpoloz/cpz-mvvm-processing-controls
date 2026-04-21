package com.cpz.processing.controls.examples.toggle;

import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.config.ToggleStyleConfig;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputLayer;
import com.cpz.processing.controls.controls.toggle.style.ParametricToggleStyle;
import com.cpz.processing.controls.controls.toggle.style.render.SvgShapeRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.io.File;

/**
 * @author CPZ
 */
public class ToggleSvgTest extends PApplet {
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
        toggle = new Toggle(this, "tglSvgTest", 0, 3, x, y, d, d);
        toggle.setChangeListener(value -> currentState = value);
        currentState = toggle.getState();
        // style
        ToggleStyleConfig tsc = new ToggleStyleConfig();
        tsc.setShapeRenderer(new SvgShapeRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));
        tsc.stateColors = new Integer[]{
                Colors.gray(70),
                Colors.rgb(235, 160, 40),
                Colors.rgb(32, 188, 176)
        };
        tsc.strokeColor = Colors.gray(255);
        tsc.strokeWidth = 1.5f;
        tsc.strokeWidthHover = 3.5f;
        tsc.hoverBlendWithWhite = 0.14f;
        tsc.pressedBlendWithBlack = 0.24f;
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
