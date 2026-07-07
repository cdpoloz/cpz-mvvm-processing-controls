package com.cpz.processing.controls.examples.progressbar;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.config.ControlConfigLoader;
import com.cpz.processing.controls.controls.progressbar.ProgressBar;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.processing.controls.util.Util;
import java.io.File;
import java.util.Map;
import processing.core.PApplet;

/**
 * JSON visual example for the non-interactive progress bar control.
 *
 * @author CPZ
 */
public class ProgressBarJsonTest extends PApplet {
    private static final String PROGRESSBAR_CONFIG_PATH = "data" + File.separator + "config" + File.separator + "progressbar.json";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private Map<String, Control> controls;
    private ProgressBar runtimeBar;

    public static void main(String[] args) {
        PApplet.main(ProgressBarJsonTest.class);
    }

    public void settings() {
        size(720, 360);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);

        this.controls = new ControlConfigLoader(this).load(PROGRESSBAR_CONFIG_PATH);
        this.runtimeBar = Util.getControl(this.controls, "pbRuntimeJson", ProgressBar.class);

        for (Control control : this.controls.values()) {
            if (control instanceof ProgressBar) {
                this.tooltips.registerTarget((ProgressBar) control);
            }
        }
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.updateRuntimeBar();
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

    private void updateRuntimeBar() {
        float progress = (sin(millis() * 0.002F) + 1.0F) * 0.5F;
        this.runtimeBar.setValue(progress);
        this.runtimeBar.setTooltipText("JSON runtime progress: " + nf(progress * 100.0F, 0, 0) + "%");
        this.tooltips.refresh();
    }

    private void drawLabels() {
        fill(220);
        text("JSON left to right", 104.0F, 82.0F);
        text("JSON right to left", 104.0F, 136.0F);
        text("JSON bottom to top", 263.0F, 316.0F);
        text("JSON top to bottom", 455.0F, 316.0F);
        fill(150);
        text("Loaded from data/config/progressbar.json", width * 0.5F, 342.0F);
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
