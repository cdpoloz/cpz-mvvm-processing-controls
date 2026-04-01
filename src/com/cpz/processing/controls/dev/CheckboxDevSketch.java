package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.checkboxcontrol.input.CheckboxInputAdapter;
import com.cpz.processing.controls.checkboxcontrol.model.CheckboxModel;
import com.cpz.processing.controls.checkboxcontrol.style.CheckboxStyleConfig;
import com.cpz.processing.controls.checkboxcontrol.style.DefaultCheckboxStyle;
import com.cpz.processing.controls.checkboxcontrol.style.render.SvgCheckboxRenderer;
import com.cpz.processing.controls.checkboxcontrol.view.CheckboxView;
import com.cpz.processing.controls.checkboxcontrol.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.labelcontrol.LabelModel;
import com.cpz.processing.controls.labelcontrol.view.LabelView;
import com.cpz.processing.controls.labelcontrol.view.LabelViewModel;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class CheckboxDevSketch extends PApplet {

    private final InputManager inputManager = new InputManager();

    private CheckboxView checkboxAView;
    private CheckboxView checkboxBView;
    private CheckboxView checkboxSvgView;

    private CheckboxViewModel checkboxAViewModel;
    private CheckboxViewModel checkboxBViewModel;
    private CheckboxViewModel checkboxSvgViewModel;

    private CheckboxInputAdapter checkboxAInput;
    private CheckboxInputAdapter checkboxBInput;
    private CheckboxInputAdapter checkboxSvgInput;

    private LabelView labelATitle;
    private LabelView labelAStatus;
    private LabelView labelBTitle;
    private LabelView labelBStatus;
    private LabelView labelSvgTitle;
    private LabelView labelSvgStatus;
    private LabelView helperLabel;

    private LabelViewModel labelATitleViewModel;
    private LabelViewModel labelAStatusViewModel;
    private LabelViewModel labelBTitleViewModel;
    private LabelViewModel labelBStatusViewModel;
    private LabelViewModel labelSvgTitleViewModel;
    private LabelViewModel labelSvgStatusViewModel;
    private LabelViewModel helperLabelViewModel;

    @Override
    public void settings() {
        size(900, 420);
        smooth(4);
    }

    @Override
    public void setup() {
        checkboxAViewModel = new CheckboxViewModel(new CheckboxModel(true));
        checkboxAView = new CheckboxView(this, checkboxAViewModel, 90, 110, 30);
        checkboxAInput = new CheckboxInputAdapter(checkboxAView, checkboxAViewModel);

        checkboxBViewModel = new CheckboxViewModel(new CheckboxModel(false));
        checkboxBView = new CheckboxView(this, checkboxBViewModel, 90, 200, 28);
        checkboxBInput = new CheckboxInputAdapter(checkboxBView, checkboxBViewModel);

        checkboxSvgViewModel = new CheckboxViewModel(new CheckboxModel(true));
        checkboxSvgViewModel.setEnabled(false);
        checkboxSvgView = new CheckboxView(this, checkboxSvgViewModel, 540, 170, 88, 72);
        checkboxSvgView.setStyle(new DefaultCheckboxStyle(createSvgStyle()));
        checkboxSvgInput = new CheckboxInputAdapter(checkboxSvgView, checkboxSvgViewModel);

        labelATitleViewModel = new LabelViewModel(new LabelModel());
        labelATitle = createLabel(labelATitleViewModel, "Option A", 120, 104);
        labelAStatusViewModel = new LabelViewModel(new LabelModel());
        labelAStatus = new LabelView(this, labelAStatusViewModel, 320, 104);

        labelBTitleViewModel = new LabelViewModel(new LabelModel());
        labelBTitle = createLabel(labelBTitleViewModel, "Option B", 120, 184);
        labelBStatusViewModel = new LabelViewModel(new LabelModel());
        labelBStatus = new LabelView(this, labelBStatusViewModel, 320, 184);

        labelSvgTitleViewModel = new LabelViewModel(new LabelModel());
        labelSvgTitle = createLabel(labelSvgTitleViewModel, "Option SVG (disabled)", 620, 176);
        labelSvgStatusViewModel = new LabelViewModel(new LabelModel());
        labelSvgStatus = new LabelView(this, labelSvgStatusViewModel, 620, 202);

        helperLabelViewModel = new LabelViewModel(new LabelModel());
        helperLabel = createLabel(helperLabelViewModel, "Click Option A to toggle SVG checkbox enabled. Press V to toggle Option B visibility.", 40, 360);
        updateStatusLabels();

        inputManager.registerLayer(new CheckboxRootInputLayer());
    }

    @Override
    public void draw() {
        background(242);
        updateStatusLabels();
        drawSectionTitles();
        checkboxAView.draw();
        checkboxBView.draw();
        checkboxSvgView.draw();
        labelATitle.draw();
        labelAStatus.draw();
        labelBTitle.draw();
        labelBStatus.draw();
        labelSvgTitle.draw();
        labelSvgStatus.draw();
        helperLabel.draw();
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

    @Override
    public void keyPressed() {
        inputManager.dispatchKeyboard(new KeyboardEvent(
                KeyboardEvent.Type.PRESS,
                key,
                keyCode,
                keyEvent != null && keyEvent.isShiftDown(),
                keyEvent != null && keyEvent.isControlDown(),
                keyEvent != null && keyEvent.isAltDown()
        ));
    }

    private void updateStatusLabels() {
        labelAStatusViewModel.setText(statusFor("A", checkboxAViewModel));
        labelBStatusViewModel.setText(statusFor("B", checkboxBViewModel));
        labelSvgStatusViewModel.setText(statusFor("SVG", checkboxSvgViewModel));

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
        text("Classic and SVG checkbox renderers share the same state/style pipeline.", 40, 60);
        popStyle();
    }

    private CheckboxStyleConfig createSvgStyle() {
        CheckboxStyleConfig config = new CheckboxStyleConfig();
        config.boxColor = color(214, 112, 58);
        config.boxHoverColor = color(236, 142, 96);
        config.boxPressedColor = color(174, 84, 36);
        config.checkColor = color(255);
        config.borderColor = color(255);
        config.borderWidth = 2f;
        config.borderWidthHover = 4f;
        config.cornerRadius = 0f;
        config.disabledAlpha = 90;
        config.checkInset = 0.22f;
        config.setRenderer(new SvgCheckboxRenderer(this, "data/img/test.svg"));
        return config;
    }

    private final class CheckboxRootInputLayer extends DefaultInputLayer {

        private CheckboxRootInputLayer() {
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case MOVE:
                case DRAG:
                    checkboxAInput.handleMouseMove(event.getX(), event.getY());
                    checkboxBInput.handleMouseMove(event.getX(), event.getY());
                    checkboxSvgInput.handleMouseMove(event.getX(), event.getY());
                    return true;

                case PRESS:
                    checkboxAInput.handleMousePress(event.getX(), event.getY());
                    checkboxBInput.handleMousePress(event.getX(), event.getY());
                    checkboxSvgInput.handleMousePress(event.getX(), event.getY());
                    return true;

                case RELEASE:
                    checkboxAInput.handleMouseRelease(event.getX(), event.getY());
                    checkboxBInput.handleMouseRelease(event.getX(), event.getY());
                    checkboxSvgInput.handleMouseRelease(event.getX(), event.getY());
                    checkboxSvgViewModel.setEnabled(checkboxAViewModel.isChecked());
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            if (event.getType() == KeyboardEvent.Type.PRESS && (event.getKey() == 'v' || event.getKey() == 'V')) {
                checkboxBViewModel.setVisible(!checkboxBViewModel.isVisible());
                labelBTitle.setPosition(120, 184);
                labelBStatus.setPosition(320, 184);
                return true;
            }
            return false;
        }
    }
}
