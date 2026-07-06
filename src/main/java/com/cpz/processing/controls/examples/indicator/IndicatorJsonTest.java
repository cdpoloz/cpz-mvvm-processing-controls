package com.cpz.processing.controls.examples.indicator;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.indicator.Indicator;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.processing.controls.util.Util;
import java.io.File;
import java.util.Map;

import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * JSON visual example for the non-interactive indicator control.
 *
 * @author CPZ
 */
public class IndicatorJsonTest extends PApplet {
    private static final String INDICATOR_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "indicator.json";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private Map<String, Control> controls;
    private Indicator runtimeIndicator;

    public static void main(String[] args) {
        PApplet.main(IndicatorJsonTest.class);
    }

    public void settings() {
        size(620, 280);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);

        this.controls = new ControlConfigLoader(this).load(INDICATOR_CONFIG_PATH);
        this.runtimeIndicator = Util.getControl(this.controls, "indRuntimeJson", Indicator.class);

        for (Control control : this.controls.values()) {
            if (control instanceof Indicator) {
                this.tooltips.registerTarget((Indicator) control);
            }
        }
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.updateRuntimeIndicator();
        this.controls.values().forEach(Control::draw);
        this.drawLabels();
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

    public void exit() {
        if (this.tooltips != null) {
            this.tooltips.dispose();
        }
        if (this.overlayManager != null) {
            this.overlayManager.clearAll();
        }
        super.exit();
    }

    private void updateRuntimeIndicator() {
        boolean runtimeOn = (millis() / 1000) % 2 == 0;
        this.runtimeIndicator.setOn(runtimeOn);
        this.runtimeIndicator.setOnColor(runtimeOn ? Colors.rgb(255,0,255) : this.runtimeIndicator.getOffColor());
        this.runtimeIndicator.setTooltipText(runtimeOn ? "JSON runtime indicator on" : "JSON runtime indicator off");
        this.tooltips.refresh();
    }

    private void drawLabels() {
        fill(220);
        text("JSON off", 142.0F, 166.0F);
        text("JSON on", 310.0F, 166.0F);
        text("JSON runtime", 478.0F, 166.0F);
        fill(150);
        text("Loaded from data/config/indicator.json", width * 0.5F, 226.0F);
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
