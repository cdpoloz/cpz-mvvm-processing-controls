package com.cpz.processing.controls.examples.panel;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.dropdown.DropDown;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.panel.style.PanelStyle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;

import java.util.List;

/**
 * Visual validation for relative DropDown composition inside a Panel.
 *
 * <p>The panel starts with relative root bounds and the dropdown keeps relative
 * local bounds inside that panel. The sketch highlights the resolved panel
 * frame and the closed field global bounds while keeping overlay rendering in
 * the supported order: {@code panel.draw()} first, overlays second.</p>
 *
 * @author CPZ
 */
public class PanelDropDownVisualTest extends PApplet {
    private static final ControlBounds INITIAL_PANEL_BOUNDS = ControlBounds.relative(0.15F, 0.12F, 0.55F, 0.32F);
    private static final ControlBounds INITIAL_DROPDOWN_BOUNDS = ControlBounds.relative(0.25F, 0.25F, 0.55F, 0.12F);

    private InputManager inputManager;
    private OverlayManager overlayManager;

    private Panel panel;
    private DropDown panelDropDown;
    private Button behindButton;
    private Button btnMove;
    private Button btnResize;
    private Button btnReset;

    private int behindClicks;
    private boolean panelMoved;
    private boolean panelResized;

    public static void main(String[] args) {
        PApplet.main(PanelDropDownVisualTest.class);
    }

    public void settings() {
        size(900, 520);
        smooth(8);
    }

    public void setup() {
        surface.setResizable(true);
        this.inputManager = new InputManager();
        this.overlayManager = new OverlayManager();

        this.createPanel();
        this.createExternalControls();
        this.registerInput();
    }

    public void draw() {
        background(22, 25, 29);
        this.drawBackdrop();
        this.behindButton.draw();
        this.btnMove.draw();
        this.btnResize.draw();
        this.btnReset.draw();
        this.panel.draw();
        this.drawActiveOverlays();
        this.drawDebugBounds();
        this.drawStatus();
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
            return;
        }

        if (key == 'm' || key == 'M') {
            this.toggleMovePanel();
        } else if (key == 's' || key == 'S') {
            this.toggleResizePanel();
        } else if (key == 'r' || key == 'R') {
            this.resetPanelRelativeBounds();
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
        this.panel = new Panel(this, "pnlRelativeDropDown", INITIAL_PANEL_BOUNDS);
        this.panel.setStyle(new PanelStyle()
                .setBackgroundVisible(true)
                .setBackgroundColor(Colors.argb(208, 31, 41, 52))
                .setStrokeVisible(true)
                .setStrokeColor(Colors.rgb(88, 164, 214))
                .setStrokeWeight(2.0F)
                .setCornerRadius(14.0F));

        this.panelDropDown = new DropDown(
                this,
                this.overlayManager,
                this.inputManager,
                "ddRelativeChild",
                List.of("Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta"),
                INITIAL_DROPDOWN_BOUNDS
        );
        this.panelDropDown.setStyle(this.createDropDownStyle());
        this.panel.add(this.panelDropDown);
    }

    private void createExternalControls() {
        this.behindButton = new Button(this, "btnBehind", "Behind overlay target", 670.0F, 246.0F, 200.0F, 38.0F);
        this.behindButton.setClickListener(() -> this.behindClicks++);

        this.btnMove = new Button(this, "btnMovePanel", "Move panel", 118.0F, 456.0F, 160.0F, 40.0F);
        this.btnMove.setClickListener(this::toggleMovePanel);

        this.btnResize = new Button(this, "btnResizePanel", "Resize panel", 310.0F, 456.0F, 160.0F, 40.0F);
        this.btnResize.setClickListener(this::toggleResizePanel);

        this.btnReset = new Button(this, "btnResetPanel", "Reset relative", 502.0F, 456.0F, 170.0F, 40.0F);
        this.btnReset.setClickListener(this::resetPanelRelativeBounds);
    }

    private void registerInput() {
        this.inputManager.registerLayer(new ButtonInputLayer(10, this.btnMove, this.btnResize, this.btnReset));
        this.inputManager.registerLayer(new PanelInputLayer(0, this.panel));
        this.inputManager.registerLayer(new ButtonInputLayer(-1, this.behindButton));
    }

    private DefaultDropDownStyle createDropDownStyle() {
        com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig style =
                new com.cpz.processing.controls.controls.dropdown.config.DropDownStyleConfig();
        style.itemHeight = 24.0F;
        style.maxVisibleItems = 6;
        style.textSize = 12.0F;
        style.baseFillOverride = Colors.rgb(235, 240, 245);
        style.listFillOverride = Colors.rgb(244, 248, 252);
        style.textOverride = Colors.rgb(30, 41, 54);
        style.borderOverride = Colors.rgb(91, 132, 170);
        style.focusedBorderOverride = Colors.rgb(52, 160, 232);
        style.hoverItemOverlayOverride = Colors.argb(52, 52, 160, 232);
        style.selectedItemOverlayOverride = Colors.argb(78, 52, 160, 232);
        return new DefaultDropDownStyle(style);
    }

    private void drawBackdrop() {
        pushStyle();
        rectMode(CORNER);
        noStroke();
        fill(38, 58, 78);
        rect(620.0F, 182.0F, 240.0F, 126.0F, 18.0F);
        fill(228);
        textAlign(LEFT, TOP);
        text("The button on the right stays behind the panel and overlay.\nOpen the dropdown and click its list over this region to verify there is no click-through.", 636.0F, 198.0F);
        popStyle();
    }

    private void drawDebugBounds() {
        TooltipBounds localBounds = this.panelDropDown.getTooltipBounds();
        TooltipBounds globalBounds = this.panel.tooltipTarget(this.panelDropDown).getTooltipBounds();

        pushStyle();
        rectMode(CORNER);
        noFill();

        stroke(98, 208, 255);
        strokeWeight(2.0F);
        rect(this.panel.getX(), this.panel.getY(), this.panel.getWidth(), this.panel.getHeight(), 8.0F);

        stroke(255, 206, 96);
        rect(globalBounds.x(), globalBounds.y(), globalBounds.width(), globalBounds.height(), 6.0F);

        stroke(114, 255, 170);
        float localCenterX = localBounds.x() + localBounds.width() * 0.5F;
        float localCenterY = localBounds.y() + localBounds.height() * 0.5F;
        float globalCenterX = globalBounds.x() + globalBounds.width() * 0.5F;
        float globalCenterY = globalBounds.y() + globalBounds.height() * 0.5F;
        line(this.panel.getX(), this.panel.getY(), globalCenterX, globalCenterY);
        line(globalCenterX - 8.0F, globalCenterY, globalCenterX + 8.0F, globalCenterY);
        line(globalCenterX, globalCenterY - 8.0F, globalCenterX, globalCenterY + 8.0F);

        fill(114, 255, 170);
        textAlign(LEFT, BOTTOM);
        text("local center (" + format(localCenterX) + ", " + format(localCenterY) + ")", this.panel.getX() + 10.0F, this.panel.getY() - 10.0F);
        text("global center (" + format(globalCenterX) + ", " + format(globalCenterY) + ")", globalBounds.x(), globalBounds.y() - 10.0F);
        popStyle();
    }

    private void drawStatus() {
        TooltipBounds localBounds = this.panelDropDown.getTooltipBounds();
        TooltipBounds globalBounds = this.panel.tooltipTarget(this.panelDropDown).getTooltipBounds();

        pushStyle();
        fill(232);
        textAlign(LEFT, TOP);
        text("Canvas: " + width + " x " + height
                + " | panel: (" + format(this.panel.getX()) + ", " + format(this.panel.getY()) + ", "
                + format(this.panel.getWidth()) + ", " + format(this.panel.getHeight()) + ")", 30.0F, 24.0F);
        text("DropDown relative measures: x=0.25w, y=0.25h, width=0.55h, height=0.12h", 30.0F, 48.0F);
        text("DropDown local bounds: (" + format(localBounds.x()) + ", " + format(localBounds.y()) + ", "
                + format(localBounds.width()) + ", " + format(localBounds.height()) + ")", 30.0F, 72.0F);
        text("DropDown global bounds: (" + format(globalBounds.x()) + ", " + format(globalBounds.y()) + ", "
                + format(globalBounds.width()) + ", " + format(globalBounds.height()) + ")", 30.0F, 96.0F);
        text("Expanded=" + this.panelDropDown.isExpanded()
                + " | selectedIndex=" + this.panelDropDown.getSelectedIndex()
                + " | selectedItem=" + this.panelDropDown.getSelectedItem()
                + " | behindClicks=" + this.behindClicks, 30.0F, 120.0F);
        text("Keys: M move panel, S resize panel, R restore relative panel, ESC close top overlay.", 30.0F, 144.0F);
        text("Draw order: behind controls -> panel.draw() -> overlayManager active overlays -> debug/status", 30.0F, 168.0F);
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

    private void toggleMovePanel() {
        this.panelMoved = !this.panelMoved;
        if (this.panelMoved) {
            this.panel.setPosition(320.0F, 92.0F);
        } else if (this.panelResized) {
            this.panel.setPosition(160.0F, 62.0F);
        } else {
            this.resetPanelRelativeBounds();
        }
    }

    private void toggleResizePanel() {
        this.panelResized = !this.panelResized;
        if (this.panelResized) {
            this.panel.setSize(430.0F, 228.0F);
        } else if (this.panelMoved) {
            this.panel.setSize(495.0F, 166.0F);
        } else {
            this.resetPanelRelativeBounds();
        }
    }

    private void resetPanelRelativeBounds() {
        this.panelMoved = false;
        this.panelResized = false;
        this.panel.setBounds(INITIAL_PANEL_BOUNDS);
    }

    private static String format(float value) {
        return Integer.toString(Math.round(value));
    }
}
