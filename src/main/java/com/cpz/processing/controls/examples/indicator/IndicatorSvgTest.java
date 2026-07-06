package com.cpz.processing.controls.examples.indicator;

import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.indicator.Indicator;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import java.io.File;

import com.cpz.utils.color.Colors;
import processing.core.PApplet;

/**
 * Visual SVG example for the non-interactive indicator control.
 *
 * @author CPZ
 */
public class IndicatorSvgTest extends PApplet {
    private static final String SVG_PATH = "data" + File.separator + "img" + File.separator + "test.svg";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private Indicator offIndicator;
    private Indicator onIndicator;
    private Indicator runtimeIndicator;

    public static void main(String[] args) {
        PApplet.main(IndicatorSvgTest.class);
    }

    public void settings() {
        size(620, 280);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);
        TooltipStyleConfig tooltipStyle = readableTooltipStyle();

        this.offIndicator = new Indicator(this, "indSvgOff", 116.0F, 90.0F, 52.0F, 52.0F, SVG_PATH)
                .setTooltip("SVG indicator off")
                .setTooltipStyle(tooltipStyle);
        this.offIndicator.setOffColor(Colors.gray(70));

        this.onIndicator = new Indicator(this, "indSvgOn", 284.0F, 90.0F, 52.0F, 52.0F, SVG_PATH)
                .setTooltip("SVG indicator on")
                .setTooltipStyle(tooltipStyle);
        this.onIndicator.setOffColor(Colors.gray(70));
        this.onIndicator.setOnColor(Colors.rgb(48, 98, 219));
        this.onIndicator.setOn(true);

        this.runtimeIndicator = new Indicator(this, "indSvgRuntime",
                ControlBounds.relative(0.729F, 0.321F, 0.186F, 0.186F), SVG_PATH)
                .setTooltip("SVG runtime state and color")
                .setTooltipStyle(tooltipStyle);
        this.runtimeIndicator.setOffColor(Colors.gray(70));
        this.runtimeIndicator.setOnColor(Colors.rgb(48, 98, 219));

        this.tooltips.registerTarget(this.offIndicator);
        this.tooltips.registerTarget(this.onIndicator);
        this.tooltips.registerTarget(this.runtimeIndicator);
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.updateRuntimeIndicator();

        this.offIndicator.draw();
        this.onIndicator.draw();
        this.runtimeIndicator.draw();
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
        this.runtimeIndicator.setTooltipText(runtimeOn ? "SVG runtime indicator on" : "SVG runtime indicator off");
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
        text("SVG off", 142.0F, 166.0F);
        text("SVG on", 310.0F, 166.0F);
        text("SVG runtime", 478.0F, 166.0F);
        fill(150);
        text("SVG is tinted with offColor/onColor.", width * 0.5F, 226.0F);
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
