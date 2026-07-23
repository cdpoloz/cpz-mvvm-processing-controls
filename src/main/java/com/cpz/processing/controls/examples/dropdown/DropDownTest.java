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
    private DropDown primaryDropDown;
    private DropDown secondaryDropDown;
    private String primarySelection;
    private String secondarySelection;

    public void settings() {
        size(760, 460);
        smooth(8);
    }

    public void setup() {
        inputManager = new InputManager();
        overlayManager = new OverlayManager();

        primaryDropDown = new DropDown(this, overlayManager, inputManager, "ddPrimary",
                List.of("Primary Alpha", "Primary Beta", "Primary Gamma", "Primary Delta", "Primary Epsilon"),
                0, 380.0f, 90.0f, 420.0f, 44.0f);
        primaryDropDown.setChangeListener(index -> {
            System.out.println("Primary DropDown selectedIndex = " + index);
            primarySelection = primaryDropDown.getSelectedItem();
        });
        primarySelection = primaryDropDown.getSelectedItem();

        secondaryDropDown = new DropDown(this, overlayManager, inputManager, "ddSecondary",
                List.of("Secondary One", "Secondary Two", "Secondary Three"),
                1, 380.0f, 169.0f, 420.0f, 38.0f);
        secondaryDropDown.setChangeListener(index -> {
            System.out.println("Secondary DropDown selectedIndex = " + index);
            secondarySelection = secondaryDropDown.getSelectedItem();
        });
        secondarySelection = secondaryDropDown.getSelectedItem();

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
        DefaultDropDownStyle dropDownStyle = new DefaultDropDownStyle(style);
        primaryDropDown.setStyle(dropDownStyle);
        secondaryDropDown.setStyle(dropDownStyle);

        inputManager.registerLayer(new DropDownInputLayer(0, primaryDropDown));
        inputManager.registerLayer(new DropDownInputLayer(0, secondaryDropDown));
        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        primaryDropDown.draw();
        secondaryDropDown.draw();
        drawActiveOverlays();
        fill(180);
        text(primaryDropDown.getCode() + " | selected = " + primarySelection
                + " | expanded = " + primaryDropDown.isExpanded(), 380, 340);
        text(secondaryDropDown.getCode() + " | selected = " + secondarySelection
                + " | expanded = " + secondaryDropDown.isExpanded(), 380, 370);
        text("Open the first menu: Primary Beta intentionally covers the second field and must win.", 380, 405);
        text("Close the first menu to use the second normally | ESC closes the active overlay.", 380, 430);
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
        primaryDropDown.dispose();
        secondaryDropDown.dispose();
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
