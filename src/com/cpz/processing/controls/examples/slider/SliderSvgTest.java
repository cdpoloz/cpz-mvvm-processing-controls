package com.cpz.processing.controls.examples.slider;

import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.input.SliderInputLayer;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.style.SvgColorMode;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;
import processing.core.PShape;
import processing.event.MouseEvent;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author CPZ
 */
public class SliderSvgTest extends PApplet {
    private InputManager inputManager;
    private Slider slider;
    private String currentValue;

    public void settings() {
        size(600, 340);
        smooth(8);
    }

    public void setup() {
        float x = 300f;
        float y = 130f;
        float w = 320f;
        float h = 72f;
        slider = new Slider(
                this,
                "sldSvgTest",
                new BigDecimal("0"),
                new BigDecimal("1"),
                new BigDecimal("0.05"),
                new BigDecimal("0.35"),
                x,
                y,
                w,
                h
        );
        slider.setFormatter(value -> value.setScale(2, RoundingMode.HALF_UP).toPlainString());
        slider.setChangeListener(value -> currentValue = slider.getFormattedValue());
        currentValue = slider.getFormattedValue();
        // style
        SliderStyleConfig ssc = new SliderStyleConfig();
        ssc.trackColor = Colors.rgb(62, 72, 86);
        ssc.trackHoverColor = Colors.rgb(86, 98, 116);
        ssc.trackPressedColor = Colors.rgb(44, 52, 64);
        ssc.trackStrokeColor = Colors.gray(220);
        ssc.trackStrokeWeight = 1.5f;
        ssc.trackStrokeWeightHover = 2.5f;
        ssc.trackThickness = 10.0f;
        ssc.activeTrackColor = Colors.rgb(56, 159, 232);
        ssc.activeTrackHoverColor = Colors.rgb(98, 184, 244);
        ssc.activeTrackPressedColor = Colors.rgb(38, 124, 184);
        ssc.thumbColor = Colors.gray(255);
        ssc.thumbHoverColor = Colors.rgb(198, 234, 255);
        ssc.thumbPressedColor = Colors.rgb(170, 220, 255);
        ssc.thumbStrokeColor = Colors.rgb(32, 78, 120);
        ssc.thumbStrokeWeight = 2.0f;
        ssc.thumbStrokeWeightHover = 3.0f;
        ssc.thumbSize = 28.0f;
        ssc.textColor = Colors.gray(245);
        ssc.disabledAlpha = 90;
        ssc.showValueText = true;
        ssc.svgColorMode = SvgColorMode.USE_RENDER_STYLE;
        ssc.thumbShape = loadThumbShape("data" + File.separator + "img" + File.separator + "test.svg");
        slider.setStyle(new SliderStyle(ssc));
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
        text("SVG thumb rendered through the same slider pipeline", 300, 275);
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

    private PShape loadThumbShape(String path) {
        PShape shape = loadShape(path);
        if (shape == null && path.startsWith("data/")) {
            shape = loadShape(path.substring("data/".length()));
        }
        return shape;
    }
}
