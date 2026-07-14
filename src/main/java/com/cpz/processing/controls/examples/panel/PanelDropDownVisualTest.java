package com.cpz.processing.controls.examples.panel;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.util.List;

/**
 * Visual validation for runtime DropDown composition inside Panel.
 *
 * @author CPZ
 */
public class PanelDropDownVisualTest extends PApplet {
    private InputManager inputManager;
    private OverlayManager overlayManager;

    private Panel panel;
    private DropDown panelDropDown;
    private Toggle panelToggle;
    private Button lowerButton;
    private Button btnVisible;
    private Button btnEnabled;
    private Button btnMove;

    private int lowerClicks;
    private boolean panelShifted;

    public static void main(String[] args) {
        PApplet.main(PanelDropDownVisualTest.class);
    }

    public void settings() {
        size(900, 520);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();

        this.createPanel();
        this.createExternalControls();
        this.registerInput();
    }

    public void draw() {
        background(24, 27, 31);
        this.drawBackdrop();
        this.drawPanelFrame();
        this.panel.draw();
        this.lowerButton.draw();
        this.btnVisible.draw();
        this.btnEnabled.draw();
        this.btnMove.draw();
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
        } else if (key == 'v' || key == 'V') {
            this.togglePanelVisible();
        } else if (key == 'e' || key == 'E') {
            this.togglePanelEnabled();
        } else if (key == 'm' || key == 'M') {
            this.movePanel();
        }
    }

    public void exit() {
        if (this.panelDropDown != null) {
            this.panelDropDown.dispose();
        }
        if (this.overlayManager != null) {
            this.overlayManager.clearAll();
        }
        super.exit();
    }

    private void createPanel() {
        this.panel = new Panel(this, "pnlDropDownDemo", 170.0F, 110.0F, 300.0F, 150.0F);

        this.panelDropDown = new DropDown(
                this,
                this.overlayManager,
                this.inputManager,
                "ddPanelChild",
                List.of("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta"),
                120,
                42.0F,
                110.0F,
                28.0F
        );
        this.panelDropDown.setStyle(this.createDropDownStyle());
        this.panelToggle = new Toggle(this, "tglPanelChild", 218.0F, 42.0F, 62.0F, 34.0F);
        Button childButton = new Button(this, "btnPanelChild", "Panel button", 150.0F, 104.0F, 180.0F, 34.0F);

        this.panel.add(this.panelDropDown)
                .add(this.panelToggle)
                .add(childButton);
    }

    private void createExternalControls() {
        this.lowerButton = new Button(this, "btnBehindPanel", "Behind panel / overlay target", 320.0F, 302.0F, 260.0F, 36.0F);
        this.lowerButton.setClickListener(() -> this.lowerClicks++);

        this.btnVisible = new Button(this, "btnTogglePanelVisible", "Show / hide panel", 170.0F, 438.0F, 180.0F, 40.0F);
        this.btnVisible.setClickListener(this::togglePanelVisible);

        this.btnEnabled = new Button(this, "btnTogglePanelEnabled", "Enable / disable", 390.0F, 438.0F, 180.0F, 40.0F);
        this.btnEnabled.setClickListener(this::togglePanelEnabled);

        this.btnMove = new Button(this, "btnMovePanel", "Move panel", 610.0F, 438.0F, 150.0F, 40.0F);
        this.btnMove.setClickListener(this::movePanel);
    }

    private void registerInput() {
        this.inputManager.registerLayer(new ButtonInputLayer(10, this.btnVisible, this.btnEnabled, this.btnMove, this.lowerButton));
        this.inputManager.registerLayer(new PanelInputLayer(0, this.panel));
    }

    private DefaultDropDownStyle createDropDownStyle() {
        com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig style =
                new com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig();
        style.itemHeight = 22.0F;
        style.maxVisibleItems = 8;
        style.textSize = 12.0F;
        style.baseFillOverride = Colors.rgb(232, 239, 245);
        style.listFillOverride = Colors.rgb(245, 248, 251);
        style.textOverride = Colors.rgb(33, 44, 57);
        style.borderOverride = Colors.rgb(90, 122, 150);
        style.focusedBorderOverride = Colors.rgb(38, 132, 212);
        style.hoverItemOverlayOverride = Colors.argb(50, 38, 132, 212);
        style.selectedItemOverlayOverride = Colors.argb(78, 38, 132, 212);
        return new DefaultDropDownStyle(style);
    }

    private void drawBackdrop() {
        pushStyle();
        noStroke();
        fill(42, 62, 84);
        rectMode(CORNER);
        rect(120.0F, 240.0F, 410.0F, 110.0F, 16.0F);
        fill(220);
        textAlign(LEFT, TOP);
        text("The large blue area plus the button sit behind the panel.\nUse the dropdown list outside the panel bounds to detect click-through.", 136.0F, 254.0F);
        popStyle();
    }

    private void drawPanelFrame() {
        if (!this.panel.isVisible()) {
            return;
        }

        pushStyle();
        rectMode(CORNER);
        noFill();
        stroke(this.panel.isEnabled() ? Colors.rgb(88, 180, 230) : Colors.rgb(112, 118, 126));
        strokeWeight(2.0F);
        rect(this.panel.getX(), this.panel.getY(), this.panel.getWidth(), this.panel.getHeight(), 8.0F);
        popStyle();
    }

    private void drawStatus() {
        fill(228);
        textAlign(LEFT, TOP);
        text("Panel position: (" + (int) this.panel.getX() + ", " + (int) this.panel.getY() + ")"
                + " | visible=" + this.panel.isVisible()
                + " | enabled=" + this.panel.isEnabled()
                + " | dropdown expanded=" + this.panelDropDown.isExpanded()
                + " | selected=" + this.panelDropDown.getSelectedItem(), 42.0F, 28.0F);
        text("Behind-panel button clicks: " + this.lowerClicks, 42.0F, 54.0F);
        text("Keys: V visibility, E enabled, M move. ESC closes the top overlay when one is open.", 42.0F, 80.0F);
    }

    private void drawActiveOverlays() {
        for (OverlayEntry entry : this.overlayManager.getActiveOverlays()) {
            entry.getRender().run();
        }
    }

    private void dispatchPointer(PointerEvent.Type type) {
        this.inputManager.dispatchPointer(new PointerEvent(type, mouseX, mouseY, mouseButton));
    }

    private void togglePanelVisible() {
        this.panel.setVisible(!this.panel.isVisible());
    }

    private void togglePanelEnabled() {
        this.panel.setEnabled(!this.panel.isEnabled());
    }

    private void movePanel() {
        this.panelShifted = !this.panelShifted;
        if (this.panelShifted) {
            this.panel.setPosition(360.0F, 150.0F);
        } else {
            this.panel.setPosition(170.0F, 110.0F);
        }
    }
}
