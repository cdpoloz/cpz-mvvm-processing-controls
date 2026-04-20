package com.cpz.processing.controls.controls.numericfield;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.numericfield.input.NumericFieldInputAdapter;
import com.cpz.processing.controls.controls.numericfield.model.NumericFieldModel;
import com.cpz.processing.controls.controls.numericfield.style.NumericFieldStyle;
import com.cpz.processing.controls.controls.numericfield.view.NumericFieldView;
import com.cpz.processing.controls.controls.numericfield.viewmodel.NumericFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Convenience facade for the numeric field control.
 *
 * @author CPZ
 */
public final class NumericField implements Control {
    private static final BigDecimal DEFAULT_MIN = null;
    private static final BigDecimal DEFAULT_MAX = null;
    private static final BigDecimal DEFAULT_STEP = BigDecimal.ONE;
    private static final int DEFAULT_SCALE = 8;

    private final NumericFieldModel model;
    private final NumericFieldViewModel viewModel;
    private final NumericFieldView view;
    private final FocusManager focusManager;
    private final NumericFieldInputAdapter inputAdapter;
    private final KeyboardInputAdapter keyboardInputAdapter;

    public NumericField(PApplet sketch, String text, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("numericfield"), text, x, y, width, height);
    }

    public NumericField(PApplet sketch, String code, String text, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new NumericFieldModel(code, BigDecimal.ZERO, DEFAULT_MIN, DEFAULT_MAX, DEFAULT_STEP, true, true, DEFAULT_SCALE);
        this.viewModel = new NumericFieldViewModel(this.model);
        this.viewModel.setText(text == null ? "" : text);
        this.view = new NumericFieldView(sketch, this.viewModel, x, y, width, height);
        this.focusManager = new FocusManager();
        this.inputAdapter = new NumericFieldInputAdapter(this.view, this.viewModel, this.focusManager);
        this.keyboardInputAdapter = new KeyboardInputAdapter(this.focusManager);
    }

    public void draw() {
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        if (event != null) {
            this.inputAdapter.handlePointerEvent(event);
        }
    }

    public void handleKeyboardEvent(KeyboardEvent event) {
        this.keyboardInputAdapter.handleKeyboardEvent(event);
    }

    public String getCode() {
        return this.model.getCode();
    }

    public String getText() {
        return this.viewModel.getText();
    }

    public void setText(String text) {
        this.viewModel.setText(text);
    }

    /**
     * Returns the numeric value derived from the current text buffer.
     * This is not guaranteed to match the last committed model value while the user is editing.
     */
    public BigDecimal getValue() {
        return this.viewModel.getParsedValue();
    }

    public void setValue(BigDecimal value) {
        this.viewModel.setValue(value);
    }

    /**
     * Returns whether the current text buffer can be parsed as a numeric value.
     */
    public boolean isValid() {
        return this.viewModel.isValid();
    }

    public boolean isFocused() {
        return this.viewModel.isFocused();
    }

    public void setChangeListener(ValueListener<String> listener) {
        this.viewModel.setOnTextChanged(listener == null ? null : listener::onChange);
    }

    public void setValueChangeListener(ValueListener<BigDecimal> listener) {
        this.viewModel.setOnValueChanged(listener == null ? null : listener::onChange);
    }

    public boolean isEnabled() {
        return this.viewModel.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.viewModel.setEnabled(enabled);
    }

    public boolean isVisible() {
        return this.viewModel.isVisible();
    }

    public void setVisible(boolean visible) {
        this.viewModel.setVisible(visible);
    }

    public void setStyle(NumericFieldStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }
}
