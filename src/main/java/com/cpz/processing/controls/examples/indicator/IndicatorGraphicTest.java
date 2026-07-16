package com.cpz.processing.controls.examples.indicator;

import com.cpz.processing.controls.controls.indicator.Indicator;
import com.cpz.processing.controls.controls.indicator.style.IndicatorStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.io.File;

/**
 * Visual example for circular, SVG, and PNG-mask indicator rendering.
 *
 * @author CPZ
 */
public class IndicatorGraphicTest extends PApplet {
    private static final String SVG_PATH = "data" + File.separator + "img" + File.separator + "test.svg";
    private static final String PNG_PATH = "data" + File.separator + "img" + File.separator + "test.png";

    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private Indicator circleIndicator;
    private Indicator svgIndicator;
    private Indicator pngIndicator;

    public static void main(String[] args) {
        PApplet.main(IndicatorGraphicTest.class);
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

    public void settings() {
        size(620, 280);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);
        TooltipStyleConfig tooltipStyle = readableTooltipStyle();

        IndicatorStyle sharedStyle = new IndicatorStyle()
                .setOnColor(Colors.rgb(48, 98, 219))
                .setOffColor(Colors.gray(70))
                .setStrokeColor(Colors.gray(210))
                .setStrokeWeight(2.0F);

        this.circleIndicator = new Indicator(this, "indGraphicCircle", 116.0F, 90.0F, 52.0F, 52.0F)
                .setTooltip("Default circular indicator")
                .setTooltipStyle(tooltipStyle);
        this.circleIndicator.setStyle(new IndicatorStyle(sharedStyle));

        this.svgIndicator = new Indicator(this, "indGraphicSvg", 284.0F, 90.0F, 52.0F, 52.0F, SVG_PATH)
                .setTooltip("SVG indicator")
                .setTooltipStyle(tooltipStyle);
        this.svgIndicator.setStyle(new IndicatorStyle(sharedStyle).setRenderer(SVG_PATH));
        this.svgIndicator.setOn(true);

        this.pngIndicator = new Indicator(this, "indGraphicPng", 452.0F, 90.0F, 52.0F, 52.0F, PNG_PATH)
                .setTooltip("PNG alpha-mask indicator")
                .setTooltipStyle(tooltipStyle);
        this.pngIndicator.setStyle(new IndicatorStyle(sharedStyle).setRenderer(PNG_PATH));

        this.tooltips.registerTarget(this.circleIndicator);
        this.tooltips.registerTarget(this.svgIndicator);
        this.tooltips.registerTarget(this.pngIndicator);
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));

        textAlign(CENTER, CENTER);
    }

    public void draw() {
        background(28);
        this.updateRuntimeIndicator();

        this.circleIndicator.draw();
        this.svgIndicator.draw();
        this.pngIndicator.draw();
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
        this.pngIndicator.setOn(runtimeOn);
        this.pngIndicator.setTooltipText(runtimeOn ? "PNG indicator on" : "PNG indicator off");
        this.tooltips.refresh();
    }

    private void drawLabels() {
        fill(220);
        text("circle", 142.0F, 166.0F);
        text("SVG on", 310.0F, 166.0F);
        text("PNG runtime", 478.0F, 166.0F);
        fill(150);
        text("PNG RGB is ignored; offColor/onColor tint the alpha mask.", width * 0.5F, 226.0F);
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
