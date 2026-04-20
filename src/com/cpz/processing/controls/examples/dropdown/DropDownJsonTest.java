package com.cpz.processing.controls.examples.dropdown;

import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.DropDownFactory;
import com.cpz.processing.controls.controls.dropdown.config.DropDownConfig;
import com.cpz.processing.controls.controls.dropdown.config.DropDownConfigLoader;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import processing.core.PApplet;

import java.io.File;

/**
 * @author CPZ
 */
public class DropDownJsonTest extends PApplet {
    private static final String DROP_DOWN_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "dropdown-test.json";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private DropDown dropDown;
    private String currentSelection;

    public void settings() {
        size(760, 360);
        smooth(8);
    }

    public void setup() {
        inputManager = new InputManager();
        overlayManager = new OverlayManager();

        DropDownConfigLoader loader = new DropDownConfigLoader(this);
        DropDownConfig config = loader.load(DROP_DOWN_CONFIG_PATH);
        dropDown = DropDownFactory.create(this, overlayManager, inputManager, config);
        dropDown.setChangeListener(index -> {
            System.out.println("DropDown selectedIndex = " + index);
            currentSelection = dropDown.getSelectedItem();
        });
        currentSelection = dropDown.getSelectedItem();

        inputManager.registerLayer(new DropDownInputLayer(0, dropDown));
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        dropDown.draw();
        drawActiveOverlays();
        fill(180);
        text(dropDown.getCode() + " | focused = " + dropDown.isFocused() + " | expanded = " + dropDown.isExpanded(), 380, 185);
        text("selected = " + currentSelection, 380, 215);
        text("config-driven drop down using DropDownConfigLoader and DropDownFactory", 380, 250);
        text("ESC closes overlay only while one is open; otherwise Processing keeps its normal behavior", 380, 275);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, mouseX, mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, mouseX, mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, mouseX, mouseY, mouseButton));
    }

    public void keyPressed() {
        if (key == ESC) {
            OverlayEntry topOverlay = overlayManager.getTopOverlay().orElse(null);
            if (topOverlay != null) {
                key = 0;
                closeTopOverlay(topOverlay);
            }
        }
    }

    public void exit() {
        dropDown.dispose();
        overlayManager.clearAll();
        super.exit();
    }

    private void drawActiveOverlays() {
        for (OverlayEntry entry : overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private void closeTopOverlay(OverlayEntry entry) {
        if (entry.getOnClose() != null) {
            entry.getOnClose().run();
        }
    }
}
