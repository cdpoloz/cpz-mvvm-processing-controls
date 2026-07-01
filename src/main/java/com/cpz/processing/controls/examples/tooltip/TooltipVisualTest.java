package com.cpz.processing.controls.examples.tooltip;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputLayer;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.HorizontalAlign;
import com.cpz.processing.controls.controls.label.style.VerticalAlign;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipArea;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.utils.color.Colors;
import java.util.List;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Interactive tooltip validation sketch.
 *
 * @author CPZ
 */
public class TooltipVisualTest extends PApplet {
    private InputManager inputManager;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;
    private Button button;
    private Button disabledButton;
    private Label label;
    private DropDown dropDown;
    private TooltipArea serverArea;
    private boolean autorun;
    private int buttonClicks;

    public static void main(String[] args) {
        PApplet.main(TooltipVisualTest.class);
    }

    public void settings() {
        size(820, 420);
        smooth(8);
    }

    public void setup() {
        this.autorun = Boolean.getBoolean("cpz.tooltip.visual.autorun");
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);
        PFont jetBrainsMono = createFont("data/font/JetBrainsMono.ttf", 14.0f);
        TooltipStyleConfig darkTooltipStyle = new TooltipStyleConfig()
                .setFont(jetBrainsMono)
                .setTextSize(14.0f)
                .setBackgroundColor(0xE61B1F26)
                .setTextColor(0xFFFFFFFF)
                .setBorderColor(0x668A94A6);

        this.button = new Button(this, "btnTooltip", "Button target", 185.0f, 95.0f, 210.0f, 52.0f)
                .setTooltip("Button tooltip")
                .setTooltipStyle(darkTooltipStyle);
        this.button.setClickListener(() -> {
            this.buttonClicks++;
            this.button.setTooltipText("Button tooltip updated " + this.buttonClicks);
            this.tooltips.refresh();
            System.out.println("TooltipVisualTest button clicked");
        });
        ButtonStyleConfig buttonStyle = new ButtonStyleConfig();
        buttonStyle.baseColor = Colors.rgb(42, 96, 194);
        buttonStyle.textColor = Colors.gray(255);
        buttonStyle.strokeColor = Colors.argb(180, 255, 255, 255);
        buttonStyle.cornerRadius = 12.0f;
        this.button.setStyle(new DefaultButtonStyle(buttonStyle));

        this.disabledButton = new Button(this, "btnDisabledTooltip", "Disabled target", 185.0f, 260.0f, 210.0f, 52.0f)
                .setTooltip("Disabled control tooltip")
                .setTooltipStyle(darkTooltipStyle);
        this.disabledButton.setEnabled(false);
        this.disabledButton.setClickListener(() -> System.out.println("Disabled button should not click"));
        ButtonStyleConfig disabledButtonStyle = new ButtonStyleConfig();
        disabledButtonStyle.baseColor = Colors.rgb(88, 98, 112);
        disabledButtonStyle.textColor = Colors.gray(255);
        disabledButtonStyle.strokeColor = Colors.argb(150, 255, 255, 255);
        disabledButtonStyle.cornerRadius = 12.0f;
        disabledButtonStyle.disabledAlpha = 125;
        this.disabledButton.setStyle(new DefaultButtonStyle(disabledButtonStyle));

        this.label = new Label(this, "lblTooltip", "Label target", 70.0f, 178.0f, 230.0f, 42.0f)
                .setTooltip("Label tooltip")
                .setTooltipStyle(darkTooltipStyle);
        LabelStyleConfig labelStyle = new LabelStyleConfig();
        labelStyle.font = createFont("data/font/JetBrainsMono.ttf", 20.0f);
        labelStyle.textSize = 20.0f;
        labelStyle.textColor = Colors.rgb(220, 232, 245);
        labelStyle.alignX = HorizontalAlign.CENTER;
        labelStyle.alignY = VerticalAlign.CENTER;
        this.label.setStyle(new DefaultLabelStyle(labelStyle));

        this.dropDown = new DropDown(
                this,
                this.overlayManager,
                this.inputManager,
                "ddTooltip",
                List.of("Rack A", "Rack B", "Rack C"),
                0,
                550.0f,
                95.0f,
                300.0f,
                50.0f
        ).setTooltip("DropDown base tooltip")
                .setTooltipStyle(darkTooltipStyle);
        DropDownStyleConfig dropDownStyle = new DropDownStyleConfig();
        dropDownStyle.baseFillOverride = Colors.rgb(238, 243, 248);
        dropDownStyle.listFillOverride = Colors.rgb(248, 250, 252);
        dropDownStyle.textOverride = Colors.rgb(30, 38, 48);
        dropDownStyle.borderOverride = Colors.rgb(78, 112, 154);
        dropDownStyle.focusedBorderOverride = Colors.rgb(42, 96, 194);
        dropDownStyle.hoverItemOverlayOverride = Colors.argb(48, 42, 96, 194);
        dropDownStyle.textSize = 15.0f;
        this.dropDown.setStyle(new DefaultDropDownStyle(dropDownStyle));

        this.serverArea = new TooltipArea(500.0f, 235.0f, 170.0f, 90.0f)
                .setTooltip("Manual rectangle tooltip")
                .setTooltipStyle(darkTooltipStyle);

        this.tooltips.registerTarget(this.button);
        this.tooltips.registerTarget(this.disabledButton);
        this.tooltips.registerTarget(this.label);
        this.tooltips.registerTarget(this.dropDown);
        this.tooltips.registerTarget(this.serverArea);

        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));
        this.inputManager.registerLayer(new ButtonInputLayer(0, this.button, this.disabledButton));
        this.inputManager.registerLayer(new DropDownInputLayer(0, this.dropDown));
    }

    public void draw() {
        if (this.autorun) {
            this.dispatchAutorunPointer();
        }

        background(28);
        this.drawManualTarget();
        this.button.draw();
        this.disabledButton.draw();
        this.label.draw();
        this.dropDown.draw();
        this.drawActiveOverlays();

        fill(170);
        textAlign(CENTER, CENTER);
        text("Click the first button to update its tooltip; disabled controls can still explain their state.", width * 0.5f, 370.0f);

        if (this.autorun && (frameCount == 2 || frameCount == 4 || frameCount == 6 || frameCount == 8)) {
            saveFrame("target/tooltip-visual-validation-####.png");
        }

        if (this.autorun && frameCount >= 8) {
            exit();
        }
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

    private void dispatchAutorunPointer() {
        float x;
        float y;
        if (frameCount < 3) {
            x = 185.0f;
            y = 95.0f;
        } else if (frameCount < 5) {
            x = 185.0f;
            y = 260.0f;
        } else if (frameCount < 7) {
            x = 550.0f;
            y = 95.0f;
        } else {
            x = 585.0f;
            y = 280.0f;
        }
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, x, y, mouseButton));
    }

    private void drawManualTarget() {
        pushStyle();
        rectMode(CORNER);
        stroke(86, 142, 203);
        strokeWeight(2.0f);
        fill(42, 54, 66);
        rect(500.0f, 235.0f, 170.0f, 90.0f, 8.0f);
        fill(220, 232, 245);
        textAlign(CENTER, CENTER);
        text("Manual rect", 585.0f, 280.0f);
        popStyle();
    }

    private void drawActiveOverlays() {
        for (OverlayEntry entry : this.overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }
}
