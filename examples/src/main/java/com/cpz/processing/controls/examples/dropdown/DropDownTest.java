package com.cpz.processing.controls.examples.dropdown;

import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;

import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.util.List;

/**
 * @author CPZ
 */
public class DropDownTest extends PApplet {
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

        dropDown = new DropDown(this, overlayManager, inputManager, "ddTest",
                List.of("Option Alpha", "Option Beta", "Option Gamma", "Option Delta", "Option Epsilon"),
                1, 380.0f, 110.0f, 420.0f, 48.0f);
        dropDown.setChangeListener(index -> {
            System.out.println("DropDown selectedIndex = " + index);
            currentSelection = dropDown.getSelectedItem();
        });
        currentSelection = dropDown.getSelectedItem();

        DropDownStyleConfig style = new DropDownStyleConfig();
        style.baseFillOverride = Colors.rgb(236, 242, 248);
        style.listFillOverride = Colors.rgb(245, 248, 252);
        style.textOverride = Colors.rgb(28, 44, 62);
        style.borderOverride = Colors.rgb(72, 116, 156);
        style.focusedBorderOverride = Colors.rgb(38, 132, 212);
        style.hoverItemOverlayOverride = Colors.argb(48, 38, 132, 212);
        style.selectedItemOverlayOverride = Colors.argb(72, 38, 132, 212);
        style.textSize = 16.0f;
        style.itemHeight = 38.0f;
        dropDown.setStyle(new DefaultDropDownStyle(style));

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
        text("click base to open/close | click option to select | click outside to close", 380, 250);
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
