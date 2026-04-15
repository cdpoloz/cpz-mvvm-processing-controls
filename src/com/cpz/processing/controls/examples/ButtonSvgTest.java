package com.cpz.processing.controls.examples;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

import java.io.File;

public class ButtonSvgTest extends PApplet {

    private InputManager inputManager;
    private Button button;
    private int clickCount;

    public void settings() {
        size(600, 300);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 125f;
        float w = 150f;
        float h = 130f;
        button = new Button(this, "SVG Button", x, y, w, h);
        button.setClickListener(() -> {
            // the code that executes after a button click goes here, for example:
            System.out.println("You clicked the SVG button!");
            clickCount++;
        });
        // style (optional)
        ButtonStyleConfig bsc = new ButtonStyleConfig();
        bsc.baseColor = Colors.rgb(48, 98, 219);
        bsc.textColor = Colors.gray(255);
        bsc.strokeColor = Colors.gray(255);
        bsc.strokeWeight = 2.0f;
        bsc.strokeWeightHover = 4.0f;
        bsc.cornerRadius = 18.0f;
        bsc.disabledAlpha = 90;
        bsc.hoverBlendWithWhite = 0.12f;
        bsc.pressedBlendWithBlack = 0.25f;
        bsc.setRenderer(new SvgButtonRenderer(this, "data" + File.separator + "img" + File.separator + "test.svg"));
        button.setStyle(new DefaultButtonStyle(bsc));
        // inputManager
        inputManager = new InputManager();
        inputManager.registerLayer(new ButtonInputLayer(0, button));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        button.draw();
        text("Current click count = " + clickCount, 300, 225);
    }

    // mouse events
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
