package com.cpz.processing.controls.examples.progressbar;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.progressbar.ProgressBar;
import com.cpz.processing.controls.controls.progressbar.ProgressBarFillDirection;
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
    private ProgressBar leftToRightBar;
    private ProgressBar rightToLeftBar;
    private ProgressBar bottomToTopBar;
    private ProgressBar topToBottomBar;

    public static void main(String[] args) {
        PApplet.main(ProgressBarTest.class);
    }

    public void settings() {
        size(720, 360);
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
        ProgressBarStyle leftToRightStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(47, 128, 237))
                .setFillDirection(ProgressBarFillDirection.LEFT_TO_RIGHT);
        ProgressBarStyle rightToLeftStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(46, 204, 113))
                .setFillDirection(ProgressBarFillDirection.RIGHT_TO_LEFT);
        ProgressBarStyle bottomToTopStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(242, 153, 74))
                .setFillDirection(ProgressBarFillDirection.BOTTOM_TO_TOP);
        ProgressBarStyle topToBottomStyle = new ProgressBarStyle(baseProgressStyle)
                .setFillColor(Colors.rgb(154, 125, 255))
                .setFillDirection(ProgressBarFillDirection.TOP_TO_BOTTOM);

        this.leftToRightBar = new ProgressBar(this, "pbLeftToRight", 170.0F, 70.0F, 420.0F, 24.0F)
                .setTooltip("Left to right progress")
                .setTooltipStyle(tooltipStyle);
        this.leftToRightBar.setValue(0.35F);
        this.leftToRightBar.setStyle(leftToRightStyle);

        this.rightToLeftBar = new ProgressBar(this, "pbRightToLeft",
                ControlBounds.relative(0.236F, 0.342F, 0.583F, 0.067F))
                .setTooltip("Right to left progress")
                .setTooltipStyle(tooltipStyle);
        this.rightToLeftBar.setValue(0.65F);
        this.rightToLeftBar.setStyle(rightToLeftStyle);

        this.bottomToTopBar = new ProgressBar(this, "pbBottomToTop", 242.0F, 182.0F, 42.0F, 118.0F)
                .setTooltip("Bottom to top progress")
                .setTooltipStyle(tooltipStyle);
        this.bottomToTopBar.setValue(0.72F);
        this.bottomToTopBar.setStyle(bottomToTopStyle);

        this.topToBottomBar = new ProgressBar(this, "pbTopToBottom", 434.0F, 182.0F, 42.0F, 118.0F)
                .setTooltip("Top to bottom progress")
                .setTooltipStyle(tooltipStyle);
        this.topToBottomBar.setValue(0.48F);
        this.topToBottomBar.setStyle(topToBottomStyle);

        this.tooltips.registerTarget(this.leftToRightBar);
        this.tooltips.registerTarget(this.rightToLeftBar);
        this.tooltips.registerTarget(this.bottomToTopBar);
        this.tooltips.registerTarget(this.topToBottomBar);
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.updateRuntimeBar();

        this.leftToRightBar.draw();
        this.rightToLeftBar.draw();
        this.bottomToTopBar.draw();
        this.topToBottomBar.draw();
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
        this.rightToLeftBar.setValue(progress);
        this.rightToLeftBar.setTooltipText("Right to left: " + nf(progress * 100.0F, 0, 0) + "%");
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
        text("left to right", 104.0F, 82.0F);
        text("right to left", 104.0F, 136.0F);
        text("bottom to top", 263.0F, 316.0F);
        text("top to bottom", 455.0F, 316.0F);
        fill(150);
        text("Hover the bars for tooltips.", width * 0.5F, 342.0F);
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
