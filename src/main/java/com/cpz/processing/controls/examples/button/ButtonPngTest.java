package com.cpz.processing.controls.examples.button;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.PngButtonRenderer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.utils.color.Colors;
import java.io.File;
import processing.core.PApplet;

/**
 * Visual runtime example for PNG alpha-mask buttons.
 *
 * @author CPZ
 */
public class ButtonPngTest extends PApplet {
    private static final String PNG_PATH = "data" + File.separator + "img" + File.separator + "button-mask.png";

    private InputManager inputManager;
    private Button activeButton;
    private Button disabledButton;

    public static void main(String[] args) {
        PApplet.main(ButtonPngTest.class);
    }

    public void settings() {
        size(720, 320);
        smooth(8);
    }

    public void setup() {
        this.activeButton = this.createButton("btnPngRuntime", "PNG Button", 210.0F, true);
        this.disabledButton = this.createButton("btnPngDisabled", "Disabled", 510.0F, false);

        this.activeButton.setClickListener(() -> System.out.println("You clicked the PNG button!"));

        this.inputManager = new InputManager();
        this.inputManager.registerLayer(new ButtonInputLayer(0, this.activeButton, this.disabledButton));
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.activeButton.draw();
        this.disabledButton.draw();

        fill(220);
        text("normal / hover / pressed", 210.0F, 225.0F);
        text("disabled", 510.0F, 225.0F);
        fill(150);
        text("Hover and hold the pointer over the left PNG mask.", width * 0.5F, 275.0F);
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

    private Button createButton(String code, String text, float x, boolean enabled) {
        Button button = new Button(this, code, text, x, 130.0F, 220.0F, 120.0F);
        ButtonStyleConfig style = new ButtonStyleConfig();
        style.baseColor = Colors.rgb(48, 98, 219);
        style.textColor = Colors.gray(255);
        style.disabledAlpha = 90;
        style.hoverBlendWithWhite = 0.12F;
        style.pressedBlendWithBlack = 0.25F;
        style.setRenderer(new PngButtonRenderer(this, PNG_PATH));
        button.setStyle(new DefaultButtonStyle(style));
        button.setEnabled(enabled);
        return button;
    }

    private void dispatchPointer(PointerEvent.Type type) {
        this.inputManager.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }
}
