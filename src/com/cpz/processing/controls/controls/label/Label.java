package com.cpz.processing.controls.controls.label;

import com.cpz.processing.controls.controls.label.model.LabelModel;
import com.cpz.processing.controls.controls.label.style.LabelStyle;
import com.cpz.processing.controls.controls.label.view.LabelView;
import com.cpz.processing.controls.controls.label.viewmodel.LabelViewModel;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Convenience facade for the label control.
 */
public final class Label {
    private final LabelModel model;
    private final LabelViewModel viewModel;
    private final LabelView view;

    public Label(PApplet sketch, String text, float x, float y) {
        this(sketch, ControlCode.auto("label"), text, x, y, 0.0f, 0.0f);
    }

    public Label(PApplet sketch, String text, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("label"), text, x, y, width, height);
    }

    public Label(PApplet sketch, String code, String text, float x, float y) {
        this(sketch, code, text, x, y, 0.0f, 0.0f);
    }

    public Label(PApplet sketch, String code, String text, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new LabelModel(code);
        this.viewModel = new LabelViewModel(this.model);
        this.viewModel.setText(text);
        this.view = new LabelView(sketch, this.viewModel, x, y, width, height);
    }

    public void draw() {
        this.view.draw();
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

    public void setStyle(LabelStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }
}
