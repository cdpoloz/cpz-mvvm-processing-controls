package com.cpz.processing.controls.dev;

import com.cpz.processing.controls.buttoncontrol.input.ButtonInputAdapter;
import com.cpz.processing.controls.buttoncontrol.model.ButtonModel;
import com.cpz.processing.controls.buttoncontrol.view.ButtonView;
import com.cpz.processing.controls.buttoncontrol.viewmodel.ButtonViewModel;
import com.cpz.processing.controls.checkboxcontrol.input.CheckboxInputAdapter;
import com.cpz.processing.controls.checkboxcontrol.model.CheckboxModel;
import com.cpz.processing.controls.checkboxcontrol.view.CheckboxView;
import com.cpz.processing.controls.checkboxcontrol.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.common.focus.FocusManager;
import com.cpz.processing.controls.common.input.KeyboardInputAdapter;
import com.cpz.processing.controls.common.theme.DarkTheme;
import com.cpz.processing.controls.common.theme.LightTheme;
import com.cpz.processing.controls.common.theme.Theme;
import com.cpz.processing.controls.common.theme.ThemeManager;
import com.cpz.processing.controls.common.theme.ThemeTokens;
import com.cpz.processing.controls.labelcontrol.LabelModel;
import com.cpz.processing.controls.labelcontrol.view.LabelView;
import com.cpz.processing.controls.labelcontrol.view.LabelViewModel;
import com.cpz.processing.controls.slidercontrol.SliderOrientation;
import com.cpz.processing.controls.slidercontrol.input.SliderInputAdapter;
import com.cpz.processing.controls.slidercontrol.model.SliderModel;
import com.cpz.processing.controls.slidercontrol.view.SliderView;
import com.cpz.processing.controls.slidercontrol.viewmodel.SliderViewModel;
import com.cpz.processing.controls.switchcontrol.SwitchInputAdapter;
import com.cpz.processing.controls.switchcontrol.SwitchModel;
import com.cpz.processing.controls.switchcontrol.view.SwitchView;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewModel;
import com.cpz.processing.controls.textfieldcontrol.input.TextFieldInputAdapter;
import com.cpz.processing.controls.textfieldcontrol.model.TextFieldModel;
import com.cpz.processing.controls.textfieldcontrol.view.TextFieldView;
import com.cpz.processing.controls.textfieldcontrol.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.input.DefaultInputLayer;
import com.cpz.processing.controls.input.InputManager;
import com.cpz.processing.controls.input.KeyboardEvent;
import com.cpz.processing.controls.input.PointerEvent;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.math.BigDecimal;

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

    @Override
    public void settings() {
        size(920, 420);
        smooth(4);
    }

    @Override
    public void setup() {
        ThemeManager.setTheme(new LightTheme());

        keyboardAdapter = new KeyboardInputAdapter(focusManager);

        buttonViewModel = new ButtonViewModel(new ButtonModel("Toggle Ready"));
        buttonViewModel.setClickListener(() -> checkboxViewModel.setChecked(!checkboxViewModel.isChecked()));
        buttonView = new ButtonView(this, buttonViewModel, 180f, 150f, 220f, 60f);
        buttonInput = new ButtonInputAdapter(buttonView, buttonViewModel);

        iconButtonViewModel = new ButtonViewModel(new ButtonModel("Hidden"));
        iconButtonViewModel.setShowText(false);
        iconButtonViewModel.setClickListener(() -> textFieldViewModel.setText("Hidden-text button clicked"));
        iconButtonView = new ButtonView(this, iconButtonViewModel, 180f, 245f, 76f, 50f);
        iconButtonInput = new ButtonInputAdapter(iconButtonView, iconButtonViewModel);

        textFieldViewModel = new TextFieldViewModel(new TextFieldModel());
        textFieldViewModel.setText("Press 't' to toggle theme");
        textFieldView = new TextFieldView(this, textFieldViewModel, width * 0.5f, 260f, 420f, 48f);
        textFieldInput = new TextFieldInputAdapter(textFieldView, textFieldViewModel, focusManager);

        switchViewModel = new SwitchViewModel(new SwitchModel());
        switchViewModel.setTotalStates(2);
        switchView = new SwitchView(this, switchViewModel, 430f, 150f, 64f);
        switchInput = new SwitchInputAdapter(switchView, switchViewModel);

        checkboxViewModel = new CheckboxViewModel(new CheckboxModel(true));
        checkboxView = new CheckboxView(this, checkboxViewModel, 610f, 150f, 34f);
        checkboxInput = new CheckboxInputAdapter(checkboxView, checkboxViewModel);

        SliderModel sliderModel = new SliderModel();
        sliderModel.setMin(BigDecimal.ZERO);
        sliderModel.setMax(BigDecimal.ONE);
        sliderModel.setStep(new BigDecimal("0.05"));
        sliderModel.setValue(new BigDecimal("0.45"));
        sliderViewModel = new SliderViewModel(sliderModel);
        sliderViewModel.setFormatter(value -> "Slider " + value.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        sliderViewModel.setShowText(false);
        sliderView = new SliderView(this, sliderViewModel, width * 0.5f, 360f, 360f, 70f, SliderOrientation.HORIZONTAL);
        sliderInput = new SliderInputAdapter(sliderView, sliderViewModel);

        sliderValueLabelViewModel = new LabelViewModel(new LabelModel());
        sliderValueLabel = new LabelView(this, sliderValueLabelViewModel, width * 0.5f - 42f, 324f);

        inputManager.registerLayer(new ThemeRootInputLayer());
    }

    @Override
    public void draw() {
        ThemeTokens tokens = ThemeManager.getTheme().tokens();
        background(tokens.surfaceVariant);
        sliderValueLabelViewModel.setText(sliderViewModel.getFormattedValue());
        drawHeader(tokens);
        buttonView.draw();
        iconButtonView.draw();
        switchView.draw();
        checkboxView.draw();
        textFieldView.draw();
        sliderView.draw();
        sliderValueLabel.draw();
        drawFooter(tokens);
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
    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                mouseX,
                mouseY,
                mouseButton,
                event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
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

    @Override
    public void keyTyped() {
        inputManager.dispatchKeyboard(new KeyboardEvent(
                KeyboardEvent.Type.TYPE,
                key,
                keyCode,
                keyEvent != null && keyEvent.isShiftDown(),
                keyEvent != null && keyEvent.isControlDown(),
                keyEvent != null && keyEvent.isAltDown()
        ));
    }

    private void drawHeader(ThemeTokens tokens) {
        pushStyle();
        fill(tokens.onSurface);
        textAlign(CENTER, CENTER);
        textSize(24f);
        text("Theme Dev Sketch", width * 0.5f, 56f);
        fill(tokens.onSurfaceVariant);
        textSize(14f);
        text("Press 't' to switch between LightTheme and DarkTheme across all controls", width * 0.5f, 88f);
        popStyle();
    }

    private void drawFooter(ThemeTokens tokens) {
        pushStyle();
        fill(tokens.onSurfaceVariant);
        textAlign(LEFT, CENTER);
        textSize(14f);
        text("Button showText=true", 112f, 106f);
        text("Button showText=false", 96f, 286f);
        text("Switch", 430f, 106f);
        text("Checkbox", 610f, 106f);
        text("TextField", width * 0.5f - 210f, 216f);
        text("External label mirrors slider value", width * 0.5f - 210f, 304f);
        text("Current theme: " + currentThemeName(), width * 0.5f - 180f, 404f);
        text("Validate background, borders, hover/pressed, cursor, selection and slider progress after toggling", width * 0.5f - 180f, 430f);
        popStyle();
    }

    private void toggleTheme() {
        Theme current = ThemeManager.getTheme();
        if (current instanceof LightTheme) {
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
            super(0);
        }

        @Override
        public boolean handlePointerEvent(PointerEvent event) {
            switch (event.getType()) {
                case MOVE:
                    buttonInput.handleMouseMove(event.getX(), event.getY());
                    iconButtonInput.handleMouseMove(event.getX(), event.getY());
                    switchInput.handleMouseMove(event.getX(), event.getY());
                    checkboxInput.handleMouseMove(event.getX(), event.getY());
                    sliderInput.handleMouseMove(event.getX(), event.getY());
                    return true;

                case DRAG:
                    buttonInput.handleMouseMove(event.getX(), event.getY());
                    iconButtonInput.handleMouseMove(event.getX(), event.getY());
                    switchInput.handleMouseMove(event.getX(), event.getY());
                    checkboxInput.handleMouseMove(event.getX(), event.getY());
                    textFieldInput.handleMouseDrag(event.getX(), event.getY());
                    sliderInput.handleMouseDrag(event.getX(), event.getY());
                    return true;

                case PRESS:
                    buttonInput.handleMousePress(event.getX(), event.getY());
                    iconButtonInput.handleMousePress(event.getX(), event.getY());
                    switchInput.handleMousePress(event.getX(), event.getY());
                    checkboxInput.handleMousePress(event.getX(), event.getY());
                    sliderInput.handleMousePress(event.getX(), event.getY());
                    boolean handled = textFieldInput.handleMousePress(event.getX(), event.getY());
                    if (!handled && !textFieldView.contains(event.getX(), event.getY())) {
                        focusManager.clearFocus();
                    }
                    return true;

                case RELEASE:
                    buttonInput.handleMouseRelease(event.getX(), event.getY());
                    iconButtonInput.handleMouseRelease(event.getX(), event.getY());
                    switchInput.handleMouseRelease(event.getX(), event.getY());
                    checkboxInput.handleMouseRelease(event.getX(), event.getY());
                    sliderInput.handleMouseRelease(event.getX(), event.getY());
                    textFieldInput.handleMouseRelease();
                    return true;

                case WHEEL:
                    sliderInput.handleMouseWheel(event.getWheelDelta(), event.isShiftDown(), event.isControlDown());
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public boolean handleKeyboardEvent(KeyboardEvent event) {
            if (event.getType() == KeyboardEvent.Type.PRESS && (event.getKey() == 't' || event.getKey() == 'T')) {
                toggleTheme();
                return true;
            }
            if (event.getType() == KeyboardEvent.Type.TYPE && (event.getKey() == 't' || event.getKey() == 'T')) {
                return true;
            }
            keyboardAdapter.handleKeyboardEvent(event);
            return true;
        }
    }
}
