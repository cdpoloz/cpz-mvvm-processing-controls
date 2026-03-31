package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.buttoncontrol.input.ButtonInputAdapter;
import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.buttoncontrol.style.ButtonStyleConfig;
import com.cpz.processing.controls.buttoncontrol.style.DefaultButtonStyle;
import com.cpz.processing.controls.buttoncontrol.style.render.SvgButtonRenderer;
import com.cpz.processing.controls.buttoncontrol.view.ButtonView;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class ButtonDevSketch extends PApplet {

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
        svgButtonInput.handleMousePress(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        primaryButtonInput.handleMouseRelease(mouseX, mouseY);
        svgButtonInput.handleMouseRelease(mouseX, mouseY);
    }

    private void forwardMove(float mx, float my) {
        primaryButtonInput.handleMouseMove(mx, my);
        svgButtonInput.handleMouseMove(mx, my);
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
}
