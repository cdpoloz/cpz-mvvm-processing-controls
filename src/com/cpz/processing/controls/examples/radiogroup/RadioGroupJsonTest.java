package com.cpz.processing.controls.examples.radiogroup;

import com.cpz.processing.controls.controls.radiogroup.RadioGroup;
import com.cpz.processing.controls.controls.radiogroup.RadioGroupFactory;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfig;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupConfigLoader;
import com.cpz.processing.controls.controls.radiogroup.input.RadioGroupInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;

import java.io.File;

public class RadioGroupJsonTest extends PApplet {
    private static final String RADIO_GROUP_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "radiogroup-test.json";

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
        RadioGroupConfigLoader loader = new RadioGroupConfigLoader(this);
        RadioGroupConfig config = loader.load(RADIO_GROUP_CONFIG_PATH);
        radioGroup = RadioGroupFactory.create(this, config);
        radioGroup.setChangeListener(index -> {
            // the code that executes after the radio group selection changes goes here, for example:
            System.out.println("Selected index = " + index);
            currentSelection = radioGroup.getSelectedOption();
        });
        currentSelection = radioGroup.getSelectedOption();
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
        text("config-driven radio group using RadioGroupConfigLoader and RadioGroupFactory", 350, 300);
        text("click an option to focus | ↑ up | ↓ down | enter/space select", 350, 330);
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
