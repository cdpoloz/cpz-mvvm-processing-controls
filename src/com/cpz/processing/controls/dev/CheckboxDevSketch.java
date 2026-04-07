package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.checkbox.config.CheckboxStyleConfig;
import com.cpz.processing.controls.controls.checkbox.model.CheckboxModel;
import com.cpz.processing.controls.controls.checkbox.style.DefaultCheckboxStyle;
import com.cpz.processing.controls.controls.checkbox.style.render.SvgCheckboxRenderer;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputAdapter;
import com.cpz.processing.controls.controls.checkbox.view.CheckboxView;
import com.cpz.processing.controls.controls.checkbox.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;

import java.util.Objects;

import processing.core.PApplet;

/**
 * Development sketch for the checkbox dev flow.
 *
 * Responsibilities:
 * - Exercise public controls in an interactive sketch.
 * - Provide a development-time validation surface.
 *
 * Behavior:
 * - Targets interactive validation rather than library reuse.
 *
 * Notes:
 * - This type is intended for development and demonstration flows.
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
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;

    /**
     * Updates tings.
     *
     * Behavior:
     * - Updates the public state or registration owned by this type.
     */
    public void settings() {
        this.size(900, 420);
        this.smooth(4);
    }

    /**
     * Updates up.
     *
     * Behavior:
     * - Updates the public state or registration owned by this type.
     */
    public void setup() {
        this.keyboardState = new KeyboardState();
        this.processingKeyboardAdapter = new ProcessingKeyboardAdapter(this.keyboardState, this.inputManager);
        this.checkboxAViewModel = new CheckboxViewModel(new CheckboxModel(true));
        this.checkboxAView = new CheckboxView(this, this.checkboxAViewModel, 90.0F, 110.0F, 30.0F);
        this.checkboxAInput = new CheckboxInputAdapter(this.checkboxAView, this.checkboxAViewModel);
        this.checkboxBViewModel = new CheckboxViewModel(new CheckboxModel(false));
        this.checkboxBView = new CheckboxView(this, this.checkboxBViewModel, 90.0F, 200.0F, 28.0F);
        this.checkboxBInput = new CheckboxInputAdapter(this.checkboxBView, this.checkboxBViewModel);
        this.checkboxSvgViewModel = new CheckboxViewModel(new CheckboxModel(true));
        this.checkboxSvgViewModel.setEnabled(false);
        this.checkboxSvgView = new CheckboxView(this, this.checkboxSvgViewModel, 540.0F, 170.0F, 88.0F, 72.0F);
        this.checkboxSvgView.setStyle(new DefaultCheckboxStyle(this.createSvgStyle()));
        this.checkboxSvgInput = new CheckboxInputAdapter(this.checkboxSvgView, this.checkboxSvgViewModel);
        this.labelATitleViewModel = new LabelViewModel(new LabelModel());
        this.labelATitle = this.createLabel(this.labelATitleViewModel, "Option A", 120.0F, 104.0F);
        this.labelAStatusViewModel = new LabelViewModel(new LabelModel());
        this.labelAStatus = new LabelView(this, this.labelAStatusViewModel, 320.0F, 104.0F);
        this.labelBTitleViewModel = new LabelViewModel(new LabelModel());
        this.labelBTitle = this.createLabel(this.labelBTitleViewModel, "Option B", 120.0F, 184.0F);
        this.labelBStatusViewModel = new LabelViewModel(new LabelModel());
        this.labelBStatus = new LabelView(this, this.labelBStatusViewModel, 320.0F, 184.0F);
        this.labelSvgTitleViewModel = new LabelViewModel(new LabelModel());
        this.labelSvgTitle = this.createLabel(this.labelSvgTitleViewModel, "Option SVG (disabled)", 620.0F, 176.0F);
        this.labelSvgStatusViewModel = new LabelViewModel(new LabelModel());
        this.labelSvgStatus = new LabelView(this, this.labelSvgStatusViewModel, 620.0F, 202.0F);
        this.helperLabelViewModel = new LabelViewModel(new LabelModel());
        this.helperLabel = this.createLabel(this.helperLabelViewModel, "Click Option A to toggle SVG checkbox enabled. Press V to toggle Option B visibility.", 40.0F, 360.0F);
        this.updateStatusLabels();
        this.inputManager.registerLayer(new CheckboxRootInputLayer());
    }

    /**
     * Draws the current frame.
     *
     * Behavior:
     * - Uses already available state and does not define business rules.
     */
    public void draw() {
        this.background(242);
        this.updateStatusLabels();
        this.drawSectionTitles();
        this.checkboxAView.draw();
        this.checkboxBView.draw();
        this.checkboxSvgView.draw();
        this.labelATitle.draw();
        this.labelAStatus.draw();
        this.labelBTitle.draw();
        this.labelBStatus.draw();
        this.labelSvgTitle.draw();
        this.labelSvgStatus.draw();
        this.helperLabel.draw();
    }

    /**
     * Performs key released.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void keyReleased() {
        if (this.key == ESC) this.key = 0;
        this.processingKeyboardAdapter.keyReleased(this.key, this.keyCode);
    }

    /**
     * Performs key typed.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void keyTyped() {
        if (key == ESC) key = 0;
        this.processingKeyboardAdapter.keyTyped(this.key, this.keyCode);
    }

    /**
     * Performs mouse moved.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void mouseMoved() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    /**
     * Performs mouse dragged.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void mouseDragged() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    /**
     * Performs mouse pressed.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void mousePressed() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    /**
     * Performs mouse released.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void mouseReleased() {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) this.mouseX, (float) this.mouseY, this.mouseButton));
    }

    /**
     * Performs key pressed.
     *
     * Behavior:
     * - Executes the public operation exposed by this type.
     */
    public void keyPressed() {
        if (key == ESC) key = 0;
        this.processingKeyboardAdapter.keyPressed(this.key, this.keyCode);
    }

    private void updateStatusLabels() {
        this.labelAStatusViewModel.setText(this.statusFor("A", this.checkboxAViewModel));
        this.labelBStatusViewModel.setText(this.statusFor("B", this.checkboxBViewModel));
        this.labelSvgStatusViewModel.setText(this.statusFor("SVG", this.checkboxSvgViewModel));
        this.labelBTitleViewModel.setVisible(this.checkboxBViewModel.isVisible());
        this.labelBStatusViewModel.setVisible(this.checkboxBViewModel.isVisible());
        this.labelBStatus.setPosition(320.0F, 184.0F);
    }

    private String statusFor(String var1, CheckboxViewModel var2) {
        return "State " + var1 + ": checked=" + var2.isChecked() + " hovered=" + var2.isHovered() + " pressed=" + var2.isPressed() + " enabled=" + var2.isEnabled() + " visible=" + var2.isVisible();
    }

    private LabelView createLabel(LabelViewModel var1, String var2, float var3, float var4) {
        var1.setText(var2);
        return new LabelView(this, var1, var3, var4);
    }

    private void drawSectionTitles() {
        this.pushStyle();
        this.fill(40);
        this.textAlign(37);
        this.text("Checkbox MVVM Demo", 40.0F, 40.0F);
        this.text("Classic and SVG checkbox renderers share the same state/style pipeline.", 40.0F, 60.0F);
        this.popStyle();
    }

    private CheckboxStyleConfig createSvgStyle() {
        CheckboxStyleConfig var1 = new CheckboxStyleConfig();
        var1.boxColor = this.color(214, 112, 58);
        var1.boxHoverColor = this.color(236, 142, 96);
        var1.boxPressedColor = this.color(174, 84, 36);
        var1.checkColor = this.color(255);
        var1.borderColor = this.color(255);
        var1.borderWidth = 2.0F;
        var1.borderWidthHover = 4.0F;
        var1.cornerRadius = 0.0F;
        var1.disabledAlpha = 90;
        var1.checkInset = 0.22F;
        var1.setRenderer(new SvgCheckboxRenderer(this, "data/img/test.svg"));
        return var1;
    }

    private final class CheckboxRootInputLayer extends DefaultInputLayer {
        private CheckboxRootInputLayer() {
            Objects.requireNonNull(CheckboxDevSketch.this);
            super(0);
        }

        /**
         * Handles pointer event.
         *
         * @param var1 parameter used by this operation
         * @return result of this operation
         *
         * Behavior:
         * - Applies the public interaction flow exposed by this type.
         */
        public boolean handlePointerEvent(PointerEvent var1) {
            switch (var1.getType()) {
                case MOVE:
                case DRAG:
                    CheckboxDevSketch.this.checkboxAInput.handleMouseMove(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxBInput.handleMouseMove(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxSvgInput.handleMouseMove(var1.getX(), var1.getY());
                    return true;
                case PRESS:
                    CheckboxDevSketch.this.checkboxAInput.handleMousePress(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxBInput.handleMousePress(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxSvgInput.handleMousePress(var1.getX(), var1.getY());
                    return true;
                case RELEASE:
                    CheckboxDevSketch.this.checkboxAInput.handleMouseRelease(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxBInput.handleMouseRelease(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxSvgInput.handleMouseRelease(var1.getX(), var1.getY());
                    CheckboxDevSketch.this.checkboxSvgViewModel.setEnabled(CheckboxDevSketch.this.checkboxAViewModel.isChecked());
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Handles keyboard event.
         *
         * @param var1 parameter used by this operation
         * @return result of this operation
         *
         * Behavior:
         * - Applies the public interaction flow exposed by this type.
         */
        public boolean handleKeyboardEvent(KeyboardEvent var1) {
            if (var1.getType() == KeyboardEvent.Type.PRESS && (var1.getKey() == 'v' || var1.getKey() == 'V')) {
                CheckboxDevSketch.this.checkboxBViewModel.setVisible(!CheckboxDevSketch.this.checkboxBViewModel.isVisible());
                CheckboxDevSketch.this.labelBTitle.setPosition(120.0F, 184.0F);
                CheckboxDevSketch.this.labelBStatus.setPosition(320.0F, 184.0F);
                return true;
            } else {
                return false;
            }
        }
    }
}
