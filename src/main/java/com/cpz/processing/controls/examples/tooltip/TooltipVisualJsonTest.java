package com.cpz.processing.controls.examples.tooltip;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipArea;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipFactory;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.processing.controls.util.Util;
import java.io.File;
import java.util.Map;
import processing.core.PApplet;

/**
 * Interactive tooltip JSON validation sketch.
 *
 * @author CPZ
 */
public class TooltipVisualJsonTest extends PApplet {
    private static final String CONTROL_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "tooltip-visual-test.json";
    private static final String SERVER_TOOLTIP_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "server-tooltip.json";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private Map<String, Control> controls;
    private Button button;
    private Label label;
    private DropDown dropDown;
    private TooltipArea serverArea;

    public static void main(String[] args) {
        PApplet.main(TooltipVisualJsonTest.class);
    }

    public void settings() {
        size(860, 430);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);

        ControlConfigLoader loader = new ControlConfigLoader(this, this.overlayManager, this.inputManager);
        this.controls = loader.load(CONTROL_CONFIG_PATH);
        this.button = Util.getControl(this.controls, "btnTooltipJson", Button.class);
        this.label = Util.getControl(this.controls, "lblTooltipJson", Label.class);
        this.dropDown = Util.getControl(this.controls, "ddTooltipJson", DropDown.class);

        this.button.setClickListener(() -> System.out.println("TooltipVisualJsonTest button clicked"));
        this.dropDown.setChangeListener(index -> System.out.println("TooltipVisualJsonTest selectedIndex = " + index));

        this.serverArea = new TooltipArea(520.0f, 230.0f, 190.0f, 92.0f)
                .setTooltip(TooltipFactory.loadFromJson(this, SERVER_TOOLTIP_CONFIG_PATH));

        this.tooltips.registerTarget(this.button);
        this.tooltips.registerTarget(this.label);
        this.tooltips.registerTarget(this.dropDown);
        this.tooltips.registerTarget(this.serverArea);

        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));
        this.inputManager.registerLayer(new ButtonInputLayer(0, this.button));
        this.inputManager.registerLayer(new DropDownInputLayer(0, this.dropDown));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.drawManualTarget();
        this.controls.values().forEach(Control::draw);
        this.drawActiveOverlays();

        fill(170);
        textAlign(CENTER, CENTER);
        text("Hover JSON-loaded controls and the manual rectangle.", width * 0.5f, 370.0f);
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
                this.closeTopOverlay(topOverlay);
            }
        }
    }

    public void exit() {
        if (this.dropDown != null) {
            this.dropDown.dispose();
        }
        if (this.tooltips != null) {
            this.tooltips.dispose();
        }
        if (this.overlayManager != null) {
            this.overlayManager.clearAll();
        }
        super.exit();
    }

    private void dispatchPointer(PointerEvent.Type type) {
        this.inputManager.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }

    private void drawManualTarget() {
        pushStyle();
        rectMode(CORNER);
        stroke(86, 142, 203);
        strokeWeight(2.0f);
        fill(42, 54, 66);
        rect(520.0f, 230.0f, 190.0f, 92.0f, 8.0f);
        fill(220, 232, 245);
        textAlign(CENTER, CENTER);
        text("TooltipArea\nexternal JSON", 615.0f, 276.0f);
        popStyle();
    }

    private void drawActiveOverlays() {
        for (OverlayEntry entry : this.overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private void closeTopOverlay(OverlayEntry entry) {
        if (entry.getOnClose() != null) {
            entry.getOnClose().run();
        }
    }
}
