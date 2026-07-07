package com.cpz.processing.controls.examples.progressbar;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.progressbar.ProgressBar;
import com.cpz.processing.controls.controls.progressbar.style.ProgressBarStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * Visual example for the non-interactive progress bar control.
 *
 * @author CPZ
 */
public class ProgressBarTest extends PApplet {
    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private ProgressBar loadingBar;
    private ProgressBar runtimeBar;
    private ProgressBar disabledBar;

    public static void main(String[] args) {
        PApplet.main(ProgressBarTest.class);
    }

    public void settings() {
        size(640, 280);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);
        TooltipStyleConfig tooltipStyle = readableTooltipStyle();
        ProgressBarStyle baseProgressStyle = new ProgressBarStyle()
                .setTrackColor(Colors.gray(58))
                .setStrokeColor(Colors.gray(220))
                .setStrokeWeight(1.5F);
        ProgressBarStyle loadingStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(47, 128, 237));
        ProgressBarStyle runtimeStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(46, 204, 113));
        ProgressBarStyle disabledStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(154, 125, 255));

        this.loadingBar = new ProgressBar(this, "pbLoading", 120.0F, 76.0F, 400.0F, 24.0F)
                .setTooltip("Loading progress")
                .setTooltipStyle(tooltipStyle);
        this.loadingBar.setValue(0.35F);
        this.loadingBar.setStyle(loadingStyle);

        this.runtimeBar = new ProgressBar(this, "pbRuntime",
                ControlBounds.relative(0.188F, 0.464F, 0.625F, 0.086F))
                .setTooltip("Runtime progress")
                .setTooltipStyle(tooltipStyle);
        this.runtimeBar.setStyle(runtimeStyle);

        this.disabledBar = new ProgressBar(this, "pbDisabled", 120.0F, 184.0F, 400.0F, 24.0F)
                .setTooltip("Disabled still exposes tooltip")
                .setTooltipStyle(tooltipStyle);
        this.disabledBar.setValue(0.7F);
        this.disabledBar.setEnabled(false);
        this.disabledBar.setStyle(disabledStyle);

        this.tooltips.registerTarget(this.loadingBar);
        this.tooltips.registerTarget(this.runtimeBar);
        this.tooltips.registerTarget(this.disabledBar);
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.updateRuntimeBar();

        this.loadingBar.draw();
        this.runtimeBar.draw();
        this.disabledBar.draw();
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
        this.runtimeBar.setTooltipText("Runtime progress: " + nf(progress * 100.0F, 0, 0) + "%");
        this.tooltips.refresh();
    }

    private static TooltipStyleConfig readableTooltipStyle() {
        return new TooltipStyleConfig()
                .setBackgroundColor(0xF21B1F26)
                .setTextColor(0xFFFFFFFF)
                .setBorderColor(0xFF8A94A6)
                .setTextSize(14.0F)
                .setTextPadding(10.0F)
                .setCornerRadius(8.0F)
                .setOffset(10.0F)
                .setStrokeWeight(1.0F);
    }

    private void drawLabels() {
        fill(220);
        text("fixed", 84.0F, 88.0F);
        text("runtime", 84.0F, 142.0F);
        text("disabled", 84.0F, 196.0F);
        fill(150);
        text("Hover the bars for tooltips.", width * 0.5F, 238.0F);
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
