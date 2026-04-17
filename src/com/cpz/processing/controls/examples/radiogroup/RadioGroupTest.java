package com.cpz.processing.controls.examples.radiogroup;

import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
import com.cpz.processing.controls.controls.radiogroup.input.RadioGroupInputLayer;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;

import java.util.List;

public class RadioGroupTest extends PApplet {
    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private RadioGroup radioGroup;
    private String currentSelection;

    public void settings() {
        size(700, 360);
        smooth(8);
    }

    public void setup() {
        float x = 375f;
        float y = 40f;
        float w = 200f;
        radioGroup = new RadioGroup(
                this,
                "rgTest",
                List.of("Mercury", "Venus", "Earth", "Mars", "Jupiter"),
                2,
                x,
                y,
                w
        );
        currentSelection = radioGroup.getSelectedOption();
        radioGroup.setChangeListener(index -> {
            // the code that executes after the radio group selection changes goes here, for example:
            System.out.println("Selected index = " + index);
            currentSelection = radioGroup.getSelectedOption();
        });
        // style
        RadioGroupStyleConfig rgsc = new RadioGroupStyleConfig();
        rgsc.textOverride = Colors.gray(245);
        rgsc.indicatorOverride = Colors.gray(255);
        rgsc.hoveredBackgroundOverride = Colors.rgb(44, 56, 74);
        rgsc.pressedBackgroundOverride = Colors.rgb(32, 42, 56);
        rgsc.selectedDotOverride = Colors.rgb(56, 159, 232);
        rgsc.itemHeight = 34.0f;
        rgsc.itemSpacing = 10.0f;
        rgsc.indicatorOuterDiameter = 18.0f;
        rgsc.indicatorInnerDiameter = 8.0f;
        rgsc.strokeWeight = 1.8f;
        rgsc.textSize = 17.0f;
        rgsc.cornerRadius = 8.0f;
        rgsc.disabledAlpha = 85;
        radioGroup.setStyle(new RadioGroupStyle(rgsc));
        // input manager
        inputManager = new InputManager();
        inputManager.registerLayer(new RadioGroupInputLayer(0, radioGroup));
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
        // text output
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        radioGroup.draw();
        fill(180);
        text(radioGroup.getCode() + " | selected = " + currentSelection, 350, 270);
        text("click an option to focus | ↑ up | ↓ down | enter/space select", 350, 300);
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

    public void keyPressed() {
        processingKeyboardAdapter.keyPressed(key, keyCode);
    }

    public void keyReleased() {
        processingKeyboardAdapter.keyReleased(key, keyCode);
    }

    public void keyTyped() {
        processingKeyboardAdapter.keyTyped(key, keyCode);
    }
}
