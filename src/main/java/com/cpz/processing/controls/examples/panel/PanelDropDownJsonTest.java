package com.cpz.processing.controls.examples.panel;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.util.Util;
import processing.core.PApplet;

import java.io.File;
import java.util.Map;

/**
 * JSON-driven visual example for runtime Panel + DropDown composition.
 *
 * @author CPZ
 */
public class PanelDropDownJsonTest extends PApplet {
    private static final String CONFIG_PATH = "data" + File.separator + "config" + File.separator + "panel-dropdown.json";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private Map<String, Control> controls;

    private Panel panel;
    private DropDown dropDown;
    private Button panelButton;
    private Button behindButton;

    private String selectedMode;
    private int panelButtonClicks;
    private int behindButtonClicks;

    public static void main(String[] args) {
        PApplet.main(PanelDropDownJsonTest.class);
    }

    public void settings() {
        size(900, 520);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();

        this.controls = new ControlConfigLoader(this, this.overlayManager, this.inputManager).load(CONFIG_PATH);
        this.panel = Util.getControl(this.controls, "pnlJsonDropDown", Panel.class);
        this.dropDown = Util.getControl(this.controls, "ddPanelMode", DropDown.class);
        this.panelButton = Util.getControl(this.controls, "btnPanelApply", Button.class);
        this.behindButton = Util.getControl(this.controls, "btnBehindPanel", Button.class);

        this.panel.add(this.dropDown)
                .add(this.panelButton);
        this.selectedMode = this.dropDown.getSelectedItem();
        this.dropDown.setChangeListener(index -> this.selectedMode = this.dropDown.getSelectedItem());
        this.panelButton.setClickListener(() -> this.panelButtonClicks++);
        this.behindButton.setClickListener(() -> this.behindButtonClicks++);

        this.inputManager.registerLayer(new PanelInputLayer(0, this.panel));
        this.inputManager.registerLayer(new ButtonInputLayer(-1, this.behindButton));
    }

    public void draw() {
        background(24, 27, 31);
        this.drawBackdrop();
        this.behindButton.draw();
        this.panel.draw();
        this.drawStatus();
        this.drawActiveOverlays();
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

    public void keyPressed() {
        if (key == ESC) {
            OverlayEntry topOverlay = this.overlayManager.getTopOverlay().orElse(null);
            if (topOverlay != null) {
                key = 0;
                if (topOverlay.getOnClose() != null) {
                    topOverlay.getOnClose().run();
                }
            }
        }
    }

    public void exit() {
        if (this.dropDown != null) {
            this.dropDown.dispose();
        }
        if (this.overlayManager != null) {
            this.overlayManager.clearAll();
        }
        super.exit();
    }

    private void drawBackdrop() {
        pushStyle();
        rectMode(CORNER);
        noStroke();
        fill(42, 62, 84);
        rect(120.0F, 242.0F, 430.0F, 116.0F, 10.0F);
        fill(220);
        textAlign(LEFT, TOP);
        text("JSON defines the panel, dropdown, panel button, and rear button.\nThe parent-child relationship is established in Java with panel.add(...).", 138.0F, 256.0F);
        popStyle();
    }

    private void drawStatus() {
        pushStyle();
        fill(228);
        textAlign(LEFT, TOP);
        text("Selected mode: " + this.selectedMode, 42.0F, 30.0F);
        text("Panel button clicks: " + this.panelButtonClicks + " | rear button clicks: " + this.behindButtonClicks, 42.0F, 56.0F);
        text("ESC closes the top overlay when one is open.", 42.0F, 82.0F);
        popStyle();
    }

    private void drawActiveOverlays() {
        for (OverlayEntry entry : this.overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private void dispatchPointer(PointerEvent.Type type) {
        this.inputManager.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }
}
