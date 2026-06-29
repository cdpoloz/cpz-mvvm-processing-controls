package com.cpz.processing.controls.controls.textfield;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.textfield.input.TextFieldInputAdapter;
import com.cpz.processing.controls.controls.textfield.model.TextFieldModel;
import com.cpz.processing.controls.controls.textfield.style.TextFieldStyle;
import com.cpz.processing.controls.controls.textfield.view.TextFieldView;
import com.cpz.processing.controls.controls.textfield.viewmodel.TextFieldViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipTarget;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.Objects;

/**
 * Convenience facade for the text field control.
 *
 * @author CPZ
 */
public final class TextField implements Control, TooltipTarget {
    private final TextFieldModel model;
    private final TextFieldViewModel viewModel;
    private final TextFieldView view;
    private final FocusManager focusManager;
    private final TextFieldInputAdapter inputAdapter;
    private final KeyboardInputAdapter keyboardInputAdapter;
    private final TooltipSupport tooltipSupport;

    public TextField(PApplet sketch, String text, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("textfield"), text, x, y, width, height);
    }

    public TextField(PApplet sketch, String code, String text, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new TextFieldModel(code);
        this.viewModel = new TextFieldViewModel(this.model);
        this.viewModel.setText(text == null ? "" : text);
        this.view = new TextFieldView(sketch, this.viewModel, x, y, width, height);
        this.focusManager = new FocusManager();
        this.inputAdapter = new TextFieldInputAdapter(this.view, this.viewModel, this.focusManager);
        this.keyboardInputAdapter = new KeyboardInputAdapter(this.focusManager);
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible, this::isEnabled);
    }

    public void draw() {
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        if (event != null) {
            this.inputAdapter.handlePointerEvent(event);
            if (event.getType() == PointerEvent.Type.PRESS && !this.view.contains(event.getX(), event.getY())) {
                this.focusManager.clearFocus();
            }
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

    public void setChangeListener(ValueListener<String> listener) {
        this.viewModel.setOnTextChanged(listener == null ? null : listener::onChange);
    }

    public boolean isFocused() {
        return this.viewModel.isFocused();
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

    public void setStyle(TextFieldStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }

    public TextField setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public TextField setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public TextField setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public TextField setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public TextField setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public TextField setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public TextField setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public TextField setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public TextField setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public TextField setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public TextField clearTooltip() {
        this.tooltipSupport.clearTooltip();
        return this;
    }

    public TooltipBounds getTooltipBounds() {
        return this.tooltipSupport.getTooltipBounds();
    }

    public Tooltip getTooltip() {
        return this.tooltipSupport.getTooltip();
    }

    public boolean isTooltipTargetVisible() {
        return this.tooltipSupport.isTooltipTargetVisible();
    }

    public boolean isTooltipTargetEnabled() {
        return this.tooltipSupport.isTooltipTargetEnabled();
    }
}
