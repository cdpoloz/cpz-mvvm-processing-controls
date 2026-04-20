package com.cpz.processing.controls.examples.theme;

import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.button.Button;
import com.cpz.processing.controls.controls.button.style.ButtonDefaultStyles;
import com.cpz.processing.controls.controls.checkbox.Checkbox;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxDefaultStyles;
import com.cpz.processing.controls.controls.label.Label;
import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.controls.label.style.DefaultLabelStyle;
import com.cpz.processing.controls.controls.label.style.LabelStyle;
import com.cpz.processing.controls.controls.slider.Slider;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.controls.slider.style.SliderDefaultStyles;
import com.cpz.processing.controls.controls.textfield.TextField;
import com.cpz.processing.controls.controls.textfield.style.TextFieldDefaultStyles;
import com.cpz.processing.controls.controls.toggle.Toggle;
import com.cpz.processing.controls.controls.toggle.style.ToggleDefaultStyles;
import com.cpz.processing.controls.core.input.DefaultInputLayer;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.theme.DarkTheme;
import com.cpz.processing.controls.core.theme.LightTheme;
import com.cpz.processing.controls.core.theme.ThemeManager;
import com.cpz.processing.controls.core.theme.ThemeTokens;
import com.cpz.processing.controls.input.KeyboardState;
import com.cpz.processing.controls.input.ProcessingKeyboardAdapter;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Official theme example based on public facades.
 *
 * @author CPZ
 */
public final class ThemeFacadeSketch extends PApplet {
    private final ThemeManager themeManager = new ThemeManager(new LightTheme());
    private final List<Control> controls = new ArrayList<>();

    private InputManager inputManager;
    private KeyboardState keyboardState;
    private ProcessingKeyboardAdapter processingKeyboardAdapter;

    private boolean lightTheme = true;
    private Button btnToggleTheme;
    private Toggle tglMode;
    private Checkbox chkEnabled;
    private Slider sldAmount;
    private TextField txtSample;
    private Label lblTheme;
    private Label lblSliderValue;

    public void settings() {
        size(920, 520);
        smooth(8);
    }

    public void setup() {
        inputManager = new InputManager();
        keyboardState = new KeyboardState();
        processingKeyboardAdapter = new ProcessingKeyboardAdapter(keyboardState, inputManager);
        createLabels();
        createInteractiveControls();
        registerInput();
        refreshDerivedLabels();
    }

    public void draw() {
        ThemeTokens tokens = themeManager.getSnapshot().tokens;
        background(tokens.surface);
        controls.forEach(Control::draw);
    }

    public void mouseMoved() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.MOVE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseDragged() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.DRAG, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mousePressed() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.PRESS, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseReleased() {
        inputManager.dispatchPointer(new PointerEvent(PointerEvent.Type.RELEASE, (float) mouseX, (float) mouseY, mouseButton));
    }

    public void mouseWheel(MouseEvent event) {
        inputManager.dispatchPointer(new PointerEvent(
                PointerEvent.Type.WHEEL,
                (float) mouseX,
                (float) mouseY,
                mouseButton,
                (float) event.getCount(),
                event.isShiftDown(),
                event.isControlDown()
        ));
    }

    public void keyPressed() {
        processingKeyboardAdapter.keyPressed(key, keyCode);
    }

    public void keyReleased() {
        processingKeyboardAdapter.keyReleased(key, keyCode);
    }

    public void keyTyped() {
        processingKeyboardAdapter.keyTyped(key, keyCode);
    }

    private void createLabels() {
        LabelStyle titleStyle = labelStyle(24.0f);
        LabelStyle bodyStyle = labelStyle(15.0f);
        LabelStyle captionStyle = labelStyle(13.0f);
        LabelStyle valueStyle = labelStyle(17.0f);

        add(label("lblTitle", "Theme Facade Sketch", 80.0f, 34.0f, 760.0f, 34.0f, titleStyle));
        add(label("lblHelp", "Press 't' to toggle LightTheme and DarkTheme when TextField is not focused.", 80.0f, 72.0f, 760.0f, 28.0f, bodyStyle));

        lblTheme = label("lblTheme", "", 80.0f, 112.0f, 300.0f, 24.0f, valueStyle);
        add(lblTheme);

        add(label("lblButton", "Button", 150.0f, 148.0f, 180.0f, 22.0f, captionStyle));
        add(label("lblToggle", "Toggle", 382.5f, 148.0f, 90.0f, 22.0f, captionStyle));
        add(label("lblCheckbox", "Checkbox", 545.0f, 148.0f, 120.0f, 22.0f, captionStyle));
        add(label("lblSlider", "Slider progress and thumb", 85.0f, 258.0f, 280.0f, 22.0f, captionStyle));
        add(label("lblTextField", "TextField focus, caret and selection", 80.0f, 360.0f, 360.0f, 22.0f, captionStyle));

        lblSliderValue = label("lblSliderValue", "", 400.0f, 288.0f, 260.0f, 28.0f, valueStyle);
        add(lblSliderValue);

        add(label("lblValidation", "Validate background, text, borders, hover, pressed, focus, selection and slider colors after toggling.", 80.0f, 462.0f, 760.0f, 42.0f, bodyStyle));
    }

    private void createInteractiveControls() {
        btnToggleTheme = new Button(this, "btnToggleTheme", "Toggle Theme", 170.0f, 180.0f, 180.0f, 35.0f);
        btnToggleTheme.setStyle(ButtonDefaultStyles.primary(themeManager));
        btnToggleTheme.setClickListener(this::toggleTheme);
        add(btnToggleTheme);

        tglMode = new Toggle(this, "tglMode", 0, 2, 400.0f, 180.0f, 35.0f);
        tglMode.setStyle(ToggleDefaultStyles.circular(themeManager));
        add(tglMode);

        chkEnabled = new Checkbox(this, "chkEnabled", true, 570.0f, 180.0f, 35.0f);
        chkEnabled.setStyle(CheckboxDefaultStyles.standard(themeManager));
        add(chkEnabled);

        sldAmount = new Slider(
                this,
                "sldAmount",
                BigDecimal.ZERO,
                new BigDecimal("100"),
                new BigDecimal("5"),
                new BigDecimal("45"),
                230.0f,
                286.0f,
                300.0f,
                68.0f
        );
        sldAmount.setSnapMode(SnapMode.ALWAYS);
        sldAmount.setShowValueText(false);
        sldAmount.setStyle(SliderDefaultStyles.standard(themeManager));
        sldAmount.setChangeListener(value -> refreshDerivedLabels());
        add(sldAmount);

        txtSample = new TextField(this, "txtSample", "Focus here, type, select text", 230.0f, 394.0f, 300f, 40.0f);
        txtSample.setStyle(TextFieldDefaultStyles.standard(themeManager));
        add(txtSample);
    }

    private void registerInput() {
        inputManager.registerLayer(new ThemeFacadeInputLayer(0));
    }

    private void toggleTheme() {
        lightTheme = !lightTheme;
        themeManager.setTheme(lightTheme ? new LightTheme() : new DarkTheme());
        refreshDerivedLabels();
    }

    private void refreshDerivedLabels() {
        lblTheme.setText("Current theme: " + (lightTheme ? "LightTheme" : "DarkTheme"));
        lblSliderValue.setText("Slider value: " + sldAmount.getFormattedValue());
    }

    private Label label(String code, String text, float x, float y, float width, float height, LabelStyle style) {
        Label label = new Label(this, code, text, x, y, width, height);
        label.setStyle(style);
        return label;
    }

    private LabelStyle labelStyle(float textSize) {
        LabelStyleConfig config = new LabelStyleConfig();
        config.textSize = textSize;
        config.lineSpacingMultiplier = 1.15f;
        config.themeProvider = themeManager;
        return new DefaultLabelStyle(config);
    }

    private void add(Control control) {
        controls.add(control);
    }

    private final class ThemeFacadeInputLayer extends DefaultInputLayer {
        private ThemeFacadeInputLayer(int priority) {
            super(priority);
        }

        public boolean handlePointerEvent(PointerEvent event) {
            if (event == null) return false;

            btnToggleTheme.handlePointerEvent(event);
            tglMode.handlePointerEvent(event);
            chkEnabled.handlePointerEvent(event);
            sldAmount.handlePointerEvent(event);
            txtSample.handlePointerEvent(event);
            return true;
        }

        public boolean handleKeyboardEvent(KeyboardEvent event) {
            if (event == null) return false;

            if (txtSample.isFocused()) {
                txtSample.handleKeyboardEvent(event);
                return true;
            }

            if (event.getType() == KeyboardEvent.Type.TYPE && (event.getKey() == 't' || event.getKey() == 'T')) {
                toggleTheme();
                return true;
            }

            return false;
        }
    }
}
