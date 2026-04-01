package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.style.DefaultButtonStyle;
import com.cpz.processing.controls.controls.button.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.controls.button.input.ButtonInputAdapter;
import com.cpz.processing.controls.controls.button.view.ButtonView;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.Colors;

import java.util.Objects;

import processing.core.PApplet;

public class ButtonDevSketch extends PApplet {
    private final InputManager inputManager = new InputManager();
    private ButtonView primaryButtonView;
    private ButtonView svgButtonView;
    private ButtonViewModel primaryButtonViewModel;
    private ButtonViewModel svgButtonViewModel;
    private ButtonInputAdapter primaryButtonInput;
    private ButtonInputAdapter svgButtonInput;
    private int clickCount;
    private String lastAction = "No clicks";
    private boolean svgEnabled = true;

    public void settings() {
        this.size(820, 440);
        this.smooth(4);
    }

    public void setup() {
        this.primaryButtonViewModel = new ButtonViewModel(new ButtonModel("Primary"));
        this.primaryButtonViewModel.setClickListener(() -> {
            ++this.clickCount;
            this.svgEnabled = !this.svgEnabled;
            this.svgButtonViewModel.setEnabled(this.svgEnabled);
            this.lastAction = "Primary click #" + this.clickCount;
        });
        this.primaryButtonView = new ButtonView(this, this.primaryButtonViewModel, 210.0F, 150.0F, 190.0F, 60.0F);
        this.primaryButtonInput = new ButtonInputAdapter(this.primaryButtonView, this.primaryButtonViewModel);
        this.svgButtonViewModel = new ButtonViewModel(new ButtonModel("SVG Action"));
        this.svgButtonViewModel.setClickListener(() -> this.lastAction = "SVG click");
        this.svgButtonView = new ButtonView(this, this.svgButtonViewModel, 590.0F, 170.0F, 250.0F, 96.0F);
        this.svgButtonView.setStyle(new DefaultButtonStyle(this.createSvgStyle()));
        this.svgButtonInput = new ButtonInputAdapter(this.svgButtonView, this.svgButtonViewModel);
        this.inputManager.registerLayer(new ButtonRootInputLayer());
    }

    public void draw() {
        this.background(28);
        this.drawTitles();
        this.primaryButtonView.draw();
        this.svgButtonView.draw();
        this.drawDebug();
    }

    public void keyReleased() {
        if (this.key == ESC) this.key = 0;
        this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
    }

    public void keyPressed() {
        if (key == ESC) key = 0;
    }

    public void keyTyped() {
        if (key == ESC) key = 0;
    }

    public void mouseMoved() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    public void mouseDragged() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    public void mousePressed() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    public void mouseReleased() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    private void drawTitles() {
        this.pushStyle();
        this.fill(230);
        this.textAlign(3);
        this.text("Button baseline", 210.0F, 84.0F);
        this.text("Button SVG", 590.0F, 84.0F);
        this.popStyle();
    }

    private void drawDebug() {
        this.pushStyle();
        this.fill(220);
        this.textAlign(37);
        this.text("Checks:", 24.0F, 320.0F);
        this.text("- Hover changes fill and strokeWeight", 24.0F, 342.0F);
        this.text("- Pressed darkens only the fill", 24.0F, 362.0F);
        this.text("- Primary click toggles SVG enabled state", 24.0F, 382.0F);
        this.text("- Release outside cancels click", 24.0F, 402.0F);
        this.text("Last action: " + this.lastAction, 430.0F, 320.0F);
        this.text("Primary hovered: " + this.primaryButtonViewModel.isHovered(), 430.0F, 342.0F);
        this.text("SVG pressed: " + this.svgButtonViewModel.isPressed(), 430.0F, 362.0F);
        this.text("SVG enabled: " + this.svgButtonViewModel.isEnabled(), 430.0F, 382.0F);
        this.popStyle();
    }

    private ButtonStyleConfig createSvgStyle() {
        ButtonStyleConfig var1 = new ButtonStyleConfig();
        var1.baseColor = Colors.rgb(219, 98, 48);
        var1.textColor = Colors.gray(255);
        var1.strokeColor = Colors.gray(255);
        var1.strokeWeight = 2.0F;
        var1.strokeWeightHover = 4.0F;
        var1.cornerRadius = 18.0F;
        var1.disabledAlpha = 90;
        var1.hoverBlendWithWhite = 0.12F;
        var1.pressedBlendWithBlack = 0.25F;
        var1.setRenderer(new SvgButtonRenderer(this, "data/img/test.svg"));
        return var1;
    }

    private final class ButtonRootInputLayer extends DefaultInputLayer {
        private ButtonRootInputLayer() {
            Objects.requireNonNull(ButtonDevSketch.this);
            super(0);
        }

        public boolean handlePointerEvent(PointerEvent var1) {
            switch (var1.getType()) {
                case MOVE:
                case DRAG:
                    ButtonDevSketch.this.primaryButtonInput.handleMouseMove(var1.getX(), var1.getY());
                    ButtonDevSketch.this.svgButtonInput.handleMouseMove(var1.getX(), var1.getY());
                    return true;
                case PRESS:
                    ButtonDevSketch.this.primaryButtonInput.handleMousePress(var1.getX(), var1.getY());
                    ButtonDevSketch.this.svgButtonInput.handleMousePress(var1.getX(), var1.getY());
                    return true;
                case RELEASE:
                    ButtonDevSketch.this.primaryButtonInput.handleMouseRelease(var1.getX(), var1.getY());
                    ButtonDevSketch.this.svgButtonInput.handleMouseRelease(var1.getX(), var1.getY());
                    return true;
                default:
                    return false;
            }
        }

        public boolean handleKeyboardEvent(KeyboardEvent var1) {
            return false;
        }
    }
}
