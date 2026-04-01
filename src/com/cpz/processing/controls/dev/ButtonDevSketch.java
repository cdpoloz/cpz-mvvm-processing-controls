package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.buttoncontrol.input.ButtonInputAdapter;
import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.buttoncontrol.style.ButtonStyleConfig;
import com.cpz.processing.controls.buttoncontrol.style.DefaultButtonStyle;
import com.cpz.processing.controls.buttoncontrol.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.buttoncontrol.view.ButtonView;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
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

    @Override
    public void settings() {
        size(820, 440);
        smooth(4);
    }

    @Override
    public void setup() {
        primaryButtonViewModel = new ButtonViewModel(new ButtonModel("Primary"));
        primaryButtonViewModel.setClickListener(() -> {
            clickCount++;
            svgEnabled = !svgEnabled;
            svgButtonViewModel.setEnabled(svgEnabled);
            lastAction = "Primary click #" + clickCount;
        });
        primaryButtonView = new ButtonView(this, primaryButtonViewModel, 210, 150, 190, 60);
        primaryButtonInput = new ButtonInputAdapter(primaryButtonView, primaryButtonViewModel);

        svgButtonViewModel = new ButtonViewModel(new ButtonModel("SVG Action"));
        svgButtonViewModel.setClickListener(() -> lastAction = "SVG click");
        svgButtonView = new ButtonView(this, svgButtonViewModel, 590, 170, 250, 96);
        svgButtonView.setStyle(new DefaultButtonStyle(createSvgStyle()));
        svgButtonInput = new ButtonInputAdapter(svgButtonView, svgButtonViewModel);

        inputManager.registerLayer(new ButtonRootInputLayer());
    }

    @Override
    public void draw() {
        background(28);
        drawTitles();
        primaryButtonView.draw();
        svgButtonView.draw();
        drawDebug();
    }

    @Override
    public void keyReleased() {
        if (key == ESC) key = 0;
        inputManager.dispatchKeyboard(new KeyboardEvent(
                KeyboardEvent.Type.RELEASE,
                key,
                keyCode,
                keyEvent != null && keyEvent.isShiftDown(),
                keyEvent != null && keyEvent.isControlDown(),
                keyEvent != null && keyEvent.isAltDown()
        ));
    }

    @Override
    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, mouseX, mouseY, mouseButton));
    }

    private void drawTitles() {
        pushStyle();
        fill(230);
        textAlign(CENTER);
        text("Button baseline", 210, 84);
        text("Button SVG", 590, 84);
        popStyle();
    }

    private void drawDebug() {
        pushStyle();
        fill(220);
        textAlign(LEFT);
        text("Checks:", 24, 320);
        text("- Hover changes fill and strokeWeight", 24, 342);
        text("- Pressed darkens only the fill", 24, 362);
        text("- Primary click toggles SVG enabled state", 24, 382);
        text("- Release outside cancels click", 24, 402);
        text("Last action: " + lastAction, 430, 320);
        text("Primary hovered: " + primaryButtonViewModel.isHovered(), 430, 342);
        text("SVG pressed: " + svgButtonViewModel.isPressed(), 430, 362);
        text("SVG enabled: " + svgButtonViewModel.isEnabled(), 430, 382);
        popStyle();
    }

    private ButtonStyleConfig createSvgStyle() {
        ButtonStyleConfig config = new ButtonStyleConfig();
        config.baseColor = Colors.rgb(219, 98, 48);
        config.textColor = Colors.gray(255);
        config.strokeColor = Colors.gray(255);
        config.strokeWeight = 2f;
        config.strokeWeightHover = 4f;
        config.cornerRadius = 18f;
        config.disabledAlpha = 90;
        config.hoverBlendWithWhite = 0.12f;
        config.pressedBlendWithBlack = 0.25f;
        config.setRenderer(new SvgButtonRenderer(this, "data/img/test.svg"));
        return config;
    }

    private final class ButtonRootInputLayer extends DefaultInputLayer {

        private ButtonRootInputLayer() {
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case MOVE:
                case DRAG:
                    primaryButtonInput.handleMouseMove(event.getX(), event.getY());
                    svgButtonInput.handleMouseMove(event.getX(), event.getY());
                    return true;

                case PRESS:
                    primaryButtonInput.handleMousePress(event.getX(), event.getY());
                    svgButtonInput.handleMousePress(event.getX(), event.getY());
                    return true;

                case RELEASE:
                    primaryButtonInput.handleMouseRelease(event.getX(), event.getY());
                    svgButtonInput.handleMouseRelease(event.getX(), event.getY());
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            return false;
        }
    }
}
