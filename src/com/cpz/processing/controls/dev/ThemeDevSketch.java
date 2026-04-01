package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.controls.button.model.ButtonModel;
import com.cpz.processing.controls.controls.button.view.ButtonInputAdapter;
import com.cpz.processing.controls.controls.button.view.ButtonView;
import com.cpz.processing.controls.controls.button.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.controls.checkbox.model.CheckboxModel;
import com.cpz.processing.controls.controls.checkbox.view.CheckboxInputAdapter;
import com.cpz.processing.controls.controls.checkbox.view.CheckboxView;
import com.cpz.processing.controls.controls.checkbox.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.view.SliderInputAdapter;
import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.controls.switchcontrol.model.SwitchModel;
import com.cpz.processing.controls.controls.switchcontrol.view.SwitchInputAdapter;
import com.cpz.processing.controls.controls.switchcontrol.view.SwitchView;
import com.cpz.processing.controls.controls.switchcontrol.viewmodel.SwitchViewModel;
import com.cpz.processing.controls.controls.textfield.model.TextFieldModel;
import com.cpz.processing.controls.controls.textfield.view.TextFieldInputAdapter;
import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.theme.DarkTheme;
import com.cpz.processing.controls.core.theme.LightTheme;
import com.cpz.processing.controls.core.theme.Theme;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeTokens;

import java.math.BigDecimal;
import java.util.Objects;

import processing.core.PApplet;
import processing.event.MouseEvent;

public final class ThemeDevSketch extends PApplet {
    private final FocusManager focusManager = new FocusManager();
    private final InputManager inputManager = new InputManager();
    private ButtonView buttonView;
    private ButtonViewModel buttonViewModel;
    private ButtonInputAdapter buttonInput;
    private ButtonView iconButtonView;
    private ButtonViewModel iconButtonViewModel;
    private ButtonInputAdapter iconButtonInput;
    private TextFieldView textFieldView;
    private TextFieldViewModel textFieldViewModel;
    private TextFieldInputAdapter textFieldInput;
    private KeyboardInputAdapter keyboardAdapter;
    private SwitchView switchView;
    private SwitchViewModel switchViewModel;
    private SwitchInputAdapter switchInput;
    private CheckboxView checkboxView;
    private CheckboxViewModel checkboxViewModel;
    private CheckboxInputAdapter checkboxInput;
    private SliderView sliderView;
    private SliderViewModel sliderViewModel;
    private SliderInputAdapter sliderInput;
    private LabelView sliderValueLabel;
    private LabelViewModel sliderValueLabelViewModel;

    public void settings() {
        this.size(920, 420);
        this.smooth(4);
    }

    public void setup() {
        ThemeManager.setTheme(new LightTheme());
        this.keyboardAdapter = new KeyboardInputAdapter(this.focusManager);
        this.buttonViewModel = new ButtonViewModel(new ButtonModel("Toggle Ready"));
        this.buttonViewModel.setClickListener(() -> this.checkboxViewModel.setChecked(!this.checkboxViewModel.isChecked()));
        this.buttonView = new ButtonView(this, this.buttonViewModel, 180.0F, 150.0F, 220.0F, 60.0F);
        this.buttonInput = new ButtonInputAdapter(this.buttonView, this.buttonViewModel);
        this.iconButtonViewModel = new ButtonViewModel(new ButtonModel("Hidden"));
        this.iconButtonViewModel.setShowText(false);
        this.iconButtonViewModel.setClickListener(() -> this.textFieldViewModel.setText("Hidden-text button clicked"));
        this.iconButtonView = new ButtonView(this, this.iconButtonViewModel, 180.0F, 245.0F, 76.0F, 50.0F);
        this.iconButtonInput = new ButtonInputAdapter(this.iconButtonView, this.iconButtonViewModel);
        this.textFieldViewModel = new TextFieldViewModel(new TextFieldModel());
        this.textFieldViewModel.setText("Press 't' to toggle theme");
        this.textFieldView = new TextFieldView(this, this.textFieldViewModel, (float) this.width * 0.5F, 260.0F, 420.0F, 48.0F);
        this.textFieldInput = new TextFieldInputAdapter(this.textFieldView, this.textFieldViewModel, this.focusManager);
        this.switchViewModel = new SwitchViewModel(new SwitchModel());
        this.switchViewModel.setTotalStates(2);
        this.switchView = new SwitchView(this, this.switchViewModel, 430.0F, 150.0F, 64.0F);
        this.switchInput = new SwitchInputAdapter(this.switchView, this.switchViewModel);
        this.checkboxViewModel = new CheckboxViewModel(new CheckboxModel(true));
        this.checkboxView = new CheckboxView(this, this.checkboxViewModel, 610.0F, 150.0F, 34.0F);
        this.checkboxInput = new CheckboxInputAdapter(this.checkboxView, this.checkboxViewModel);
        SliderModel var1 = new SliderModel();
        var1.setMin(BigDecimal.ZERO);
        var1.setMax(BigDecimal.ONE);
        var1.setStep(new BigDecimal("0.05"));
        var1.setValue(new BigDecimal("0.45"));
        this.sliderViewModel = new SliderViewModel(var1);
        this.sliderViewModel.setFormatter((var0) -> "Slider " + var0.setScale(2, 4).toPlainString());
        this.sliderViewModel.setShowText(false);
        this.sliderView = new SliderView(this, this.sliderViewModel, (float) this.width * 0.5F, 360.0F, 360.0F, 70.0F, SliderOrientation.HORIZONTAL);
        this.sliderInput = new SliderInputAdapter(this.sliderView, this.sliderViewModel);
        this.sliderValueLabelViewModel = new LabelViewModel(new LabelModel());
        this.sliderValueLabel = new LabelView(this, this.sliderValueLabelViewModel, (float) this.width * 0.5F - 42.0F, 324.0F);
        this.inputManager.registerLayer(new ThemeRootInputLayer());
    }

    public void draw() {
        ThemeTokens var1 = ThemeManager.getTheme().tokens();
        this.background(var1.surfaceVariant);
        this.sliderValueLabelViewModel.setText(this.sliderViewModel.getFormattedValue());
        this.drawHeader(var1);
        this.buttonView.draw();
        this.iconButtonView.draw();
        this.switchView.draw();
        this.checkboxView.draw();
        this.textFieldView.draw();
        this.sliderView.draw();
        this.sliderValueLabel.draw();
        this.drawFooter(var1);
    }

    public void keyReleased() {
        if (key == ESC) key = 0;
        this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.RELEASE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
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

    public void mouseWheel(MouseEvent var1) {
        this.inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.WHEEL, (float) this.mouseX, (float) this.mouseY, this.mouseButton, (float) var1.getCount(), var1.isShiftDown(), var1.isControlDown()));
    }

    public void keyPressed() {
        if (key == ESC) key = 0;
        this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.PRESS, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
    }

    public void keyTyped() {
        if (key == ESC) key = 0;
        this.inputManager.dispatchKeyboard(new KeyboardEvent(KeyboardEvent.Type.TYPE, this.key, this.keyCode, this.keyEvent != null && this.keyEvent.isShiftDown(), this.keyEvent != null && this.keyEvent.isControlDown(), this.keyEvent != null && this.keyEvent.isAltDown()));
    }

    private void drawHeader(ThemeTokens var1) {
        this.pushStyle();
        this.fill(var1.onSurface);
        this.textAlign(3, 3);
        this.textSize(24.0F);
        this.text("Theme Dev Sketch", (float) this.width * 0.5F, 56.0F);
        this.fill(var1.onSurfaceVariant);
        this.textSize(14.0F);
        this.text("Press 't' to switch between LightTheme and DarkTheme across all controls", (float) this.width * 0.5F, 88.0F);
        this.popStyle();
    }

    private void drawFooter(ThemeTokens var1) {
        this.pushStyle();
        this.fill(var1.onSurfaceVariant);
        this.textAlign(37, 3);
        this.textSize(14.0F);
        this.text("Button showText=true", 112.0F, 106.0F);
        this.text("Button showText=false", 96.0F, 286.0F);
        this.text("Switch", 430.0F, 106.0F);
        this.text("Checkbox", 610.0F, 106.0F);
        this.text("TextField", (float) this.width * 0.5F - 210.0F, 216.0F);
        this.text("External label mirrors slider value", (float) this.width * 0.5F - 210.0F, 304.0F);
        this.text("Current theme: " + this.currentThemeName(), (float) this.width * 0.5F - 180.0F, 404.0F);
        this.text("Validate background, borders, hover/pressed, cursor, selection and slider progress after toggling", (float) this.width * 0.5F - 180.0F, 430.0F);
        this.popStyle();
    }

    private void toggleTheme() {
        Theme var1 = ThemeManager.getTheme();
        if (var1 instanceof LightTheme) {
            ThemeManager.setTheme(new DarkTheme());
        } else {
            ThemeManager.setTheme(new LightTheme());
        }

    }

    private String currentThemeName() {
        return ThemeManager.getTheme() instanceof LightTheme ? "Light" : "Dark";
    }

    private final class ThemeRootInputLayer extends DefaultInputLayer {
        private ThemeRootInputLayer() {
            Objects.requireNonNull(ThemeDevSketch.this);
            super(0);
        }

        public boolean handlePointerEvent(PointerEvent var1) {
            switch (var1.getType()) {
                case MOVE:
                    ThemeDevSketch.this.buttonInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.iconButtonInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.switchInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.checkboxInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.sliderInput.handleMouseMove(var1.getX(), var1.getY());
                    return true;
                case DRAG:
                    ThemeDevSketch.this.buttonInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.iconButtonInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.switchInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.checkboxInput.handleMouseMove(var1.getX(), var1.getY());
                    ThemeDevSketch.this.textFieldInput.handleMouseDrag(var1.getX(), var1.getY());
                    ThemeDevSketch.this.sliderInput.handleMouseDrag(var1.getX(), var1.getY());
                    return true;
                case PRESS:
                    ThemeDevSketch.this.buttonInput.handleMousePress(var1.getX(), var1.getY());
                    ThemeDevSketch.this.iconButtonInput.handleMousePress(var1.getX(), var1.getY());
                    ThemeDevSketch.this.switchInput.handleMousePress(var1.getX(), var1.getY());
                    ThemeDevSketch.this.checkboxInput.handleMousePress(var1.getX(), var1.getY());
                    ThemeDevSketch.this.sliderInput.handleMousePress(var1.getX(), var1.getY());
                    boolean var2 = ThemeDevSketch.this.textFieldInput.handleMousePress(var1.getX(), var1.getY());
                    if (!var2 && !ThemeDevSketch.this.textFieldView.contains(var1.getX(), var1.getY())) {
                        ThemeDevSketch.this.focusManager.clearFocus();
                    }

                    return true;
                case RELEASE:
                    ThemeDevSketch.this.buttonInput.handleMouseRelease(var1.getX(), var1.getY());
                    ThemeDevSketch.this.iconButtonInput.handleMouseRelease(var1.getX(), var1.getY());
                    ThemeDevSketch.this.switchInput.handleMouseRelease(var1.getX(), var1.getY());
                    ThemeDevSketch.this.checkboxInput.handleMouseRelease(var1.getX(), var1.getY());
                    ThemeDevSketch.this.sliderInput.handleMouseRelease(var1.getX(), var1.getY());
                    ThemeDevSketch.this.textFieldInput.handleMouseRelease();
                    return true;
                case WHEEL:
                    ThemeDevSketch.this.sliderInput.handleMouseWheel(var1.getWheelDelta(), var1.isShiftDown(), var1.isControlDown());
                    return true;
                default:
                    return false;
            }
        }

        public boolean handleKeyboardEvent(KeyboardEvent var1) {
            if (var1.getType() != KeyboardEvent.Type.PRESS || var1.getKey() != 't' && var1.getKey() != 'T') {
                if (var1.getType() != KeyboardEvent.Type.TYPE || var1.getKey() != 't' && var1.getKey() != 'T') {
                    ThemeDevSketch.this.keyboardAdapter.handleKeyboardEvent(var1);
                    return true;
                } else {
                    return true;
                }
            } else {
                ThemeDevSketch.this.toggleTheme();
                return true;
            }
        }
    }
}
