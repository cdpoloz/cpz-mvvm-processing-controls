package com.cpz.processing.controls.examples.panel;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.util.List;

/**
 * Visual validation for runtime Panel style changes.
 *
 * @author CPZ
 */
public class PanelStyleRuntimeTest extends PApplet {
    private InputManager inputManager;
    private OverlayManager overlayManager;

    private Panel styledPanel;
    private Panel noBackgroundPanel;
    private Panel noStrokePanel;
    private Panel borderOnlyPanel;
    private DropDown dropDown;
    private Button externalButton;

    private int paletteIndex;
    private int childClicks;
    private int externalClicks;
    private float strokeWeight = 2.0F;
    private float cornerRadius = 14.0F;

    private final int[] backgrounds = {
            0xEA20242A,
            0xEA21372F,
            0xEA34283E,
            0xEA1E3542
    };
    private final int[] strokes = {
            0xFF7E8A96,
            0xFF74B69A,
            0xFFC59BEA,
            0xFF82B9D2
    };

    public static void main(String[] args) {
        PApplet.main(PanelStyleRuntimeTest.class);
    }

    public void settings() {
        size(940, 560);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();
        this.createPanels();
        this.createExternalControls();
        this.registerInput();
    }

    public void draw() {
        background(22, 25, 29);
        this.drawBackdrop();
        this.externalButton.draw();
        this.styledPanel.draw();
        this.noBackgroundPanel.draw();
        this.noStrokePanel.draw();
        this.borderOnlyPanel.draw();
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
        } else if (key == 'c' || key == 'C') {
            this.paletteIndex = (this.paletteIndex + 1) % this.backgrounds.length;
            this.applyMainPanelStyle();
        } else if (key == 'b' || key == 'B') {
            this.styledPanel.setBackgroundVisible(!this.styledPanel.isBackgroundVisible());
        } else if (key == 's' || key == 'S') {
            this.styledPanel.setStrokeVisible(!this.styledPanel.isStrokeVisible());
        } else if (key == 'w' || key == 'W') {
            this.strokeWeight = this.strokeWeight >= 6.0F ? 1.0F : this.strokeWeight + 1.0F;
            this.styledPanel.setStrokeWeight(this.strokeWeight);
        } else if (key == 'r' || key == 'R') {
            this.cornerRadius = this.cornerRadius >= 28.0F ? 0.0F : this.cornerRadius + 7.0F;
            this.styledPanel.setCornerRadius(this.cornerRadius);
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

    private void createPanels() {
        this.styledPanel = new Panel(this, "pnlStyled", 70.0F, 122.0F, 355.0F, 206.0F);
        this.styledPanel.setStyle(new PanelStyle()
                .setBackgroundVisible(true)
                .setStrokeVisible(true));
        this.applyMainPanelStyle();

        Label title = new Label(this, "lblStyled", "Styled Panel", 28.0F, 22.0F, 180.0F, 24.0F);
        Button childButton = new Button(this, "btnStyledChild", "Child button", 102.0F, 78.0F, 168.0F, 36.0F);
        childButton.setClickListener(() -> this.childClicks++);
        this.dropDown = new DropDown(
                this,
                this.overlayManager,
                this.inputManager,
                "ddStyledPanel",
                List.of("North", "East", "South", "West", "Center", "Outer option"),
                0,
                270.0F,
                78.0F,
                140.0F,
                30.0F
        );
        this.dropDown.setStyle(this.createDropDownStyle());
        this.styledPanel.add(title)
                .add(childButton)
                .add(this.dropDown);

        this.noBackgroundPanel = new Panel(this, "pnlNoBackground", 475.0F, 122.0F, 185.0F, 92.0F);
        this.noBackgroundPanel.setStyle(new PanelStyle()
                .setBackgroundVisible(false)
                .setStrokeVisible(true)
                .setStrokeColor(0xFFE3B660)
                .setStrokeWeight(4.0F)
                .setCornerRadius(20.0F));
        this.noBackgroundPanel.add(new Label(this, "lblNoBackground", "No background", 20.0F, 24.0F, 140.0F, 22.0F));

        this.noStrokePanel = new Panel(this, "pnlNoStroke", 690.0F, 122.0F, 185.0F, 92.0F);
        this.noStrokePanel.setStyle(new PanelStyle()
                .setBackgroundVisible(true)
                .setBackgroundColor(0xEA31424E)
                .setStrokeVisible(false)
                .setCornerRadius(4.0F));
        this.noStrokePanel.add(new Label(this, "lblNoStroke", "No border", 24.0F, 24.0F, 120.0F, 22.0F));

        this.borderOnlyPanel = new Panel(this, "pnlBorderOnly", 475.0F, 254.0F, 400.0F, 74.0F);
        this.borderOnlyPanel.setStyle(new PanelStyle()
                .setBackgroundVisible(false)
                .setStrokeVisible(true)
                .setStrokeColor(0xFF78D3E8)
                .setStrokeWeight(1.0F)
                .setCornerRadius(28.0F));
        this.borderOnlyPanel.add(new Label(this, "lblBorderOnly", "Border only: no fill, children still draw", 28.0F, 22.0F, 320.0F, 22.0F));
    }

    private void createExternalControls() {
        this.externalButton = new Button(this, "btnExternal", "External button", 470.0F, 450.0F, 120.0F, 40.0F);
        this.externalButton.setClickListener(() -> this.externalClicks++);
    }

    private void registerInput() {
        this.inputManager.registerLayer(new PanelInputLayer(
                0,
                this.styledPanel,
                this.noBackgroundPanel,
                this.noStrokePanel,
                this.borderOnlyPanel
        ));
        this.inputManager.registerLayer(new ButtonInputLayer(-1, this.externalButton));
    }

    private DefaultDropDownStyle createDropDownStyle() {
        DropDownStyleConfig style = new DropDownStyleConfig();
        style.baseFillOverride = Colors.rgb(236, 241, 246);
        style.listFillOverride = Colors.rgb(248, 250, 252);
        style.textOverride = Colors.rgb(30, 38, 47);
        style.borderOverride = Colors.rgb(100, 118, 138);
        style.focusedBorderOverride = Colors.rgb(64, 148, 224);
        style.itemHeight = 24.0F;
        style.maxVisibleItems = 6;
        style.textSize = 13.0F;
        return new DefaultDropDownStyle(style);
    }

    private void applyMainPanelStyle() {
        this.styledPanel.setBackgroundColor(this.backgrounds[this.paletteIndex]);
        this.styledPanel.setStrokeColor(this.strokes[this.paletteIndex]);
        this.styledPanel.setStrokeWeight(this.strokeWeight);
        this.styledPanel.setCornerRadius(this.cornerRadius);
    }

    private void drawBackdrop() {
        pushStyle();
        noStroke();
        fill(38, 47, 58);
        rectMode(CORNER);
        rect(44.0F, 92.0F, 856.0F, 274.0F, 18.0F);
        fill(226);
        textAlign(LEFT, TOP);
        text("Runtime Panel style: C color, B background, S border, W stroke width, R corner radius. ESC closes dropdown overlay.", 48.0F, 38.0F);
        text("DropDown remains a global overlay while the closed field is a panel child.", 48.0F, 62.0F);
        popStyle();
    }

    private void drawStatus() {
        pushStyle();
        fill(226);
        textAlign(LEFT, TOP);
        text("Styled panel: background=" + this.styledPanel.isBackgroundVisible()
                + ", border=" + this.styledPanel.isStrokeVisible()
                + ", strokeWeight=" + this.styledPanel.getStrokeWeight()
                + ", radius=" + this.styledPanel.getCornerRadius(), 52.0F, 386.0F);
        text("Child clicks=" + this.childClicks
                + " | external clicks=" + this.externalClicks
                + " | selected=" + this.dropDown.getSelectedItem(), 52.0F, 416.0F);
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
