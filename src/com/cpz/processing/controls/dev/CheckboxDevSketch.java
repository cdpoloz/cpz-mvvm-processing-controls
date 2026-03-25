package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.checkboxcontrol.input.CheckboxInputAdapter;
import com.cpz.processing.controls.checkboxcontrol.model.CheckboxModel;
import com.cpz.processing.controls.checkboxcontrol.view.CheckboxView;
import com.cpz.processing.controls.checkboxcontrol.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.labelcontrol.LabelModel;
import com.cpz.processing.controls.labelcontrol.view.LabelView;
import com.cpz.processing.controls.labelcontrol.view.LabelViewModel;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class CheckboxDevSketch extends PApplet {

    private CheckboxView checkboxAView;
    private CheckboxView checkboxBView;
    private CheckboxView checkboxCView;

    private CheckboxViewModel checkboxAViewModel;
    private CheckboxViewModel checkboxBViewModel;
    private CheckboxViewModel checkboxCViewModel;

    private CheckboxInputAdapter checkboxAInput;
    private CheckboxInputAdapter checkboxBInput;
    private CheckboxInputAdapter checkboxCInput;

    private LabelView labelATitle;
    private LabelView labelAStatus;
    private LabelView labelBTitle;
    private LabelView labelBStatus;
    private LabelView labelCTitle;
    private LabelView labelCStatus;
    private LabelView helperLabel;

    private LabelViewModel labelATitleViewModel;
    private LabelViewModel labelAStatusViewModel;
    private LabelViewModel labelBTitleViewModel;
    private LabelViewModel labelBStatusViewModel;
    private LabelViewModel labelCTitleViewModel;
    private LabelViewModel labelCStatusViewModel;
    private LabelViewModel helperLabelViewModel;

    @Override
    public void settings() {
        size(900, 420);
        smooth(4);
    }

    @Override
    public void setup() {
        checkboxAViewModel = new CheckboxViewModel(new CheckboxModel(true));
        checkboxAView = new CheckboxView(this, checkboxAViewModel, 80, 100, 28);
        checkboxAInput = new CheckboxInputAdapter(checkboxAView, checkboxAViewModel);

        checkboxBViewModel = new CheckboxViewModel(new CheckboxModel(false));
        checkboxBView = new CheckboxView(this, checkboxBViewModel, 80, 180, 28);
        checkboxBInput = new CheckboxInputAdapter(checkboxBView, checkboxBViewModel);

        checkboxCViewModel = new CheckboxViewModel(new CheckboxModel(true));
        checkboxCViewModel.setEnabled(false);
        checkboxCView = new CheckboxView(this, checkboxCViewModel, 80, 260, 28);
        checkboxCInput = new CheckboxInputAdapter(checkboxCView, checkboxCViewModel);

        labelATitleViewModel = new LabelViewModel(new LabelModel());
        labelATitle = createLabel(labelATitleViewModel, "Option A", 120, 104);
        labelAStatusViewModel = new LabelViewModel(new LabelModel());
        labelAStatus = new LabelView(this, labelAStatusViewModel, 320, 104);

        labelBTitleViewModel = new LabelViewModel(new LabelModel());
        labelBTitle = createLabel(labelBTitleViewModel, "Option B", 120, 184);
        labelBStatusViewModel = new LabelViewModel(new LabelModel());
        labelBStatus = new LabelView(this, labelBStatusViewModel, 320, 184);

        labelCTitleViewModel = new LabelViewModel(new LabelModel());
        labelCTitle = createLabel(labelCTitleViewModel, "Option C (disabled)", 120, 264);
        labelCStatusViewModel = new LabelViewModel(new LabelModel());
        labelCStatus = new LabelView(this, labelCStatusViewModel, 320, 264);

        helperLabelViewModel = new LabelViewModel(new LabelModel());
        helperLabel = createLabel(helperLabelViewModel, "Press V to toggle Option B visibility", 40, 340);
        updateStatusLabels();
    }

    @Override
    public void draw() {
        background(242);
        updateStatusLabels();
        drawSectionTitles();
        checkboxAView.draw();
        checkboxBView.draw();
        checkboxCView.draw();
        labelATitle.draw();
        labelAStatus.draw();
        labelBTitle.draw();
        labelBStatus.draw();
        labelCTitle.draw();
        labelCStatus.draw();
        helperLabel.draw();
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
        checkboxAInput.handleMousePress(mouseX, mouseY);
        checkboxBInput.handleMousePress(mouseX, mouseY);
        checkboxCInput.handleMousePress(mouseX, mouseY);
    }

    @Override
    public void mouseReleased() {
        checkboxAInput.handleMouseRelease(mouseX, mouseY);
        checkboxBInput.handleMouseRelease(mouseX, mouseY);
        checkboxCInput.handleMouseRelease(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {
        if (key == 'v' || key == 'V') {
            checkboxBViewModel.setVisible(!checkboxBViewModel.isVisible());
            labelBTitle.setPosition(120, 184);
            labelBStatus.setPosition(320, 184);
        }
    }

    private void forwardMove(float mx, float my) {
        checkboxAInput.handleMouseMove(mx, my);
        checkboxBInput.handleMouseMove(mx, my);
        checkboxCInput.handleMouseMove(mx, my);
    }

    private void updateStatusLabels() {
        labelAStatusViewModel.setText(statusFor("A", checkboxAViewModel));
        labelBStatusViewModel.setText(statusFor("B", checkboxBViewModel));
        labelCStatusViewModel.setText(statusFor("C", checkboxCViewModel));

        labelBTitleViewModel.setVisible(checkboxBViewModel.isVisible());
        labelBStatusViewModel.setVisible(checkboxBViewModel.isVisible());
        labelBStatus.setPosition(320, 184);
    }

    private String statusFor(String id, CheckboxViewModel vm) {
        return "State " + id
                + ": checked=" + vm.isChecked()
                + " hovered=" + vm.isHovered()
                + " pressed=" + vm.isPressed()
                + " enabled=" + vm.isEnabled()
                + " visible=" + vm.isVisible();
    }

    private LabelView createLabel(LabelViewModel vm, String text, float x, float y) {
        vm.setText(text);
        return new LabelView(this, vm, x, y);
    }

    private void drawSectionTitles() {
        pushStyle();
        fill(40);
        textAlign(LEFT);
        text("Checkbox MVVM Demo", 40, 40);
        text("Click each checkbox to toggle checked/unchecked.", 40, 60);
        popStyle();
    }
}
