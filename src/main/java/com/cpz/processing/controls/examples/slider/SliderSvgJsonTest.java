package com.cpz.processing.controls.examples.slider;

import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.SliderFactory;
import com.cpz.processing.controls.controls.slider.config.SliderConfig;
import com.cpz.processing.controls.controls.slider.config.SliderConfigLoader;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.io.File;

/**
 * @author CPZ
 */
public class SliderSvgJsonTest extends PApplet {
    private static final String SLIDER_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "slider-svg-test.json";

    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        SliderConfigLoader loader = new SliderConfigLoader(this);
        SliderConfig config = loader.load(SLIDER_CONFIG_PATH);
        slider = SliderFactory.create(this, config);
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new SliderInputLayer(0, slider));
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        slider.draw();
        text(slider.getCode() + " | value = " + currentValue, 300, 240);
        text("SVG thumb loaded from JSON through the same slider style path", 300, 275);
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

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }
}
