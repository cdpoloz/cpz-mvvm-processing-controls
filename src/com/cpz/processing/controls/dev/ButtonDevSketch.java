package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.buttoncontrol.input.ButtonInputAdapter;
import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.buttoncontrol.style.ButtonStyleConfig;
import com.cpz.processing.controls.buttoncontrol.style.DefaultButtonStyle;
import com.cpz.processing.controls.buttoncontrol.view.ButtonView;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class ButtonDevSketch extends PApplet {

    private ButtonView primaryButtonView;
    private ButtonView disabledButtonView;
    private ButtonView customButtonView;

    private ButtonViewModel primaryButtonViewModel;
    private ButtonViewModel disabledButtonViewModel;
    private ButtonViewModel customButtonViewModel;

    private ButtonInputAdapter primaryButtonInput;
    private ButtonInputAdapter disabledButtonInput;
    private ButtonInputAdapter customButtonInput;

    private int clickCount;
    private String lastAction = "No clicks";

    @Override
    public void settings() {
        size(760, 420);
        smooth(4);
    }

    @Override
    public void setup() {
        primaryButtonViewModel = new ButtonViewModel(new ButtonModel("Primary"));
        primaryButtonViewModel.setClickListener(() -> {
            clickCount++;
            lastAction = "Primary click #" + clickCount;
        });
        primaryButtonView = new ButtonView(this, primaryButtonViewModel, 180, 120, 180, 56);
        primaryButtonInput = new ButtonInputAdapter(primaryButtonView, primaryButtonViewModel);

        disabledButtonViewModel = new ButtonViewModel(new ButtonModel("Disabled"));
        disabledButtonViewModel.setEnabled(false);
        disabledButtonViewModel.setClickListener(() -> lastAction = "Should not fire");
        disabledButtonView = new ButtonView(this, disabledButtonViewModel, 180, 220, 180, 56);
        disabledButtonInput = new ButtonInputAdapter(disabledButtonView, disabledButtonViewModel);

        customButtonViewModel = new ButtonViewModel(new ButtonModel("Custom"));
        customButtonViewModel.setClickListener(() -> lastAction = "Custom click");
        customButtonView = new ButtonView(this, customButtonViewModel, 470, 170, 220, 70);
        customButtonView.setStyle(new DefaultButtonStyle(createCustomStyle()));
        customButtonInput = new ButtonInputAdapter(customButtonView, customButtonViewModel);
    }

    @Override
    public void draw() {
        background(28);
        drawTitles();
        primaryButtonView.draw();
        disabledButtonView.draw();
        customButtonView.draw();
        drawDebug();
    }

    @Override
    public void mouseMoved() {
        forwardMove(mouseX, mouseY);
    }

    @Override
    public void mouseDragged() {
        forwardMove(mouseX, mouseY);
    }

    @Override
    public void mousePressed() {
        primaryButtonInput.handleMousePress(mouseX, mouseY);
        disabledButtonInput.handleMousePress(mouseX, mouseY);
        customButtonInput.handleMousePress(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        primaryButtonInput.handleMouseRelease(mouseX, mouseY);
        disabledButtonInput.handleMouseRelease(mouseX, mouseY);
        customButtonInput.handleMouseRelease(mouseX, mouseY);
    }

    private void forwardMove(float mx, float my) {
        primaryButtonInput.handleMouseMove(mx, my);
        disabledButtonInput.handleMouseMove(mx, my);
        customButtonInput.handleMouseMove(mx, my);
    }

    private void drawTitles() {
        pushStyle();
        fill(230);
        textAlign(CENTER);
        text("Button default", 180, 70);
        text("Button custom", 470, 70);
        popStyle();
    }

    private void drawDebug() {
        pushStyle();
        fill(220);
        textAlign(LEFT);
        text("Checks:", 24, 310);
        text("- Hover changes color", 24, 332);
        text("- Pressed darkens the background", 24, 352);
        text("- Disabled does not trigger callback", 24, 372);
        text("- Release outside cancels click", 24, 392);
        text("Last action: " + lastAction, 330, 310);
        text("Primary hovered: " + primaryButtonViewModel.isHovered(), 330, 332);
        text("Primary pressed: " + primaryButtonViewModel.isPressed(), 330, 352);
        text("Disabled enabled: " + disabledButtonViewModel.isEnabled(), 330, 372);
        popStyle();
    }

    private ButtonStyleConfig createCustomStyle() {
        ButtonStyleConfig config = new ButtonStyleConfig();
        config.baseColor = Colors.rgb(24, 160, 88);
        config.textColor = Colors.gray(255);
        config.cornerRadius = 18f;
        config.disabledAlpha = 90;
        config.hoverBlendWithWhite = 0.10f;
        config.pressedBlendWithBlack = 0.28f;
        return config;
    }
}
