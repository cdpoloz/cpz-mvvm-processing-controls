package com.cpz.processing.controls.examples.panel;

import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.input.ButtonInputLayer;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.panel.Panel;
import com.cpz.processing.controls.controls.panel.input.PanelInputLayer;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayEntry;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.overlay.tooltip.input.TooltipInputLayer;
import com.cpz.processing.controls.core.overlay.tooltip.util.TooltipOverlayController;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import com.cpz.utils.color.Colors;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * Visual example for the Panel MVP.
 *
 * @author CPZ
 */
public class PanelVisualTest extends PApplet {
    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;
    private OverlayManager overlayManager;
    private TooltipOverlayController tooltips;

    private Panel panel;
    private Button childButton;
    private Toggle childToggle;
    private TextField childTextField;
    private Button btnVisible;
    private Button btnEnabled;
    private Button btnMove;

    private int childClicks;
    private boolean panelShifted;

    public static void main(String[] args) {
        PApplet.main(PanelVisualTest.class);
    }

    public void settings() {
        size(760, 440);
        smooth(8);
    }

    public void setup() {
        this.inputManager = new InputManager();
        this.keyboardState = new KeyboardState();
        this.processingKeyboardAdapter = new ProcessingKeyboardAdapter(this.keyboardState, this.inputManager);
        this.overlayManager = new OverlayManager();
        this.tooltips = new TooltipOverlayController(this, this.overlayManager);

        this.createPanel();
        this.createExternalControls();
        this.registerInput();
    }

    public void draw() {
        background(30);
        this.drawPanelFrame();
        this.panel.draw();
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
        if (!this.childTextField.isFocused()) {
            if (key == 'v' || key == 'V') {
                this.togglePanelVisible();
            } else if (key == 'e' || key == 'E') {
                this.togglePanelEnabled();
            } else if (key == 'm' || key == 'M') {
                this.movePanel();
            }
        }
        this.processingKeyboardAdapter.keyPressed(key, keyCode);
    }

    public void keyReleased() {
        this.processingKeyboardAdapter.keyReleased(key, keyCode);
    }

    public void keyTyped() {
        this.processingKeyboardAdapter.keyTyped(key, keyCode);
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

    private void createPanel() {
        this.panel = new Panel(this, "pnlDemo", 210.0f, 118.0f, 330.0f, 210.0f);

        Label title = new Label(this, "lblPanelTitle", "Panel children use local coordinates", 24.0f, 22.0f, 280.0f, 26.0f);
        this.childButton = new Button(this, "btnPanelChild", "Local button", 95.0f, 78.0f, 150.0f, 44.0f);
        this.childButton.setClickListener(() -> this.childClicks++);
        this.childToggle = new Toggle(this, "tglPanelChild", 230.0f, 78.0f, 64.0f, 38.0f);
        this.childTextField = new TextField(this, "txtPanelChild", "edit me", 74.0f, 150.0f, 220.0f, 40.0f);

        PFont font = createFont("data/font/JetBrainsMono.ttf", 13.0f);
        TooltipStyleConfig tooltipStyle = new TooltipStyleConfig()
                .setFont(font)
                .setTextSize(13.0f)
                .setBackgroundColor(0xEA1F252D)
                .setTextColor(0xFFFFFFFF)
                .setBorderColor(0x6698A8B8);
        this.childButton.setTooltip("This tooltip target is offset by the panel position.")
                .setTooltipStyle(tooltipStyle);

        this.panel.add(title)
                .add(this.childButton)
                .add(this.childToggle)
                .add(this.childTextField);
        this.tooltips.registerTarget(this.panel.tooltipTarget(this.childButton));
    }

    private void createExternalControls() {
        this.btnVisible = new Button(this, "btnTogglePanelVisible", "Show / hide", 120.0f, 370.0f, 160.0f, 42.0f);
        this.btnVisible.setClickListener(this::togglePanelVisible);

        this.btnEnabled = new Button(this, "btnTogglePanelEnabled", "Enable / disable", 315.0f, 370.0f, 180.0f, 42.0f);
        this.btnEnabled.setClickListener(this::togglePanelEnabled);

        this.btnMove = new Button(this, "btnMovePanel", "Move panel", 525.0f, 370.0f, 150.0f, 42.0f);
        this.btnMove.setClickListener(this::movePanel);
    }

    private void registerInput() {
        this.inputManager.registerLayer(new TooltipInputLayer(1000, this.tooltips));
        this.inputManager.registerLayer(new ButtonInputLayer(10, this.btnVisible, this.btnEnabled, this.btnMove));
        this.inputManager.registerLayer(new PanelInputLayer(0, this.panel));
    }

    private void drawPanelFrame() {
        if (!this.panel.isVisible()) {
            return;
        }

        pushStyle();
        rectMode(CORNER);
        noFill();
        stroke(this.panel.isEnabled() ? Colors.rgb(80, 168, 220) : Colors.rgb(105, 112, 120));
        strokeWeight(2.0f);
        rect(this.panel.getX(), this.panel.getY(), this.panel.getWidth(), this.panel.getHeight(), 6.0f);
        stroke(Colors.argb(120, 255, 255, 255));
        line(this.panel.getX(), this.panel.getY(), this.panel.getX() + 12.0f, this.panel.getY());
        line(this.panel.getX(), this.panel.getY(), this.panel.getX(), this.panel.getY() + 12.0f);
        popStyle();
    }

    private void drawStatus() {
        fill(220);
        textAlign(LEFT, TOP);
        text("Panel: " + (this.panel.isVisible() ? "visible" : "hidden")
                + " / " + (this.panel.isEnabled() ? "enabled" : "disabled")
                + " / position=(" + (int) this.panel.getX() + ", " + (int) this.panel.getY() + ")"
                + " / child clicks=" + this.childClicks, 44.0f, 34.0f);
        text("External buttons or keys: V visibility, E enabled, M move. The blue frame is drawn by the sketch.", 44.0f, 62.0f);
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
            this.panel.setPosition(300.0f, 128.0f);
        } else {
            this.panel.setPosition(210.0f, 118.0f);
        }
        this.tooltips.refresh();
    }
}
