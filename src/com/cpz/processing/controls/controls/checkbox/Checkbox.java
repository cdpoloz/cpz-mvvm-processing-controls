package com.cpz.processing.controls.controls.checkbox;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputAdapter;
import com.cpz.processing.controls.controls.checkbox.model.CheckboxModel;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxStyle;
import com.cpz.processing.controls.controls.checkbox.view.CheckboxView;
import com.cpz.processing.controls.controls.checkbox.viewmodel.CheckboxViewModel;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Convenience facade for the checkbox control.
 */
public final class Checkbox {
    private final CheckboxModel model;
    private final CheckboxViewModel viewModel;
    private final CheckboxView view;
    private final CheckboxInputAdapter inputAdapter;
    private ValueListener<Boolean> changeListener;

    public Checkbox(PApplet sketch, boolean checked, float x, float y, float size) {
        this(sketch, ControlCode.auto("checkbox"), checked, x, y, size, size);
    }

    public Checkbox(PApplet sketch, boolean checked, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("checkbox"), checked, x, y, width, height);
    }

    public Checkbox(PApplet sketch, String code, boolean checked, float x, float y, float size) {
        this(sketch, code, checked, x, y, size, size);
    }

    public Checkbox(PApplet sketch, String code, boolean checked, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new CheckboxModel(code, checked);
        this.viewModel = new CheckboxViewModel(this.model);
        this.view = new CheckboxView(sketch, this.viewModel, x, y, width, height);
        this.inputAdapter = new CheckboxInputAdapter(this.view, this.viewModel);
    }

    public void draw() {
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        boolean before = this.viewModel.isChecked();
        this.inputAdapter.handlePointerEvent(event);
        this.notifyChangeIfNeeded(before);
    }

    public String getCode() {
        return this.model.getCode();
    }

    public boolean isChecked() {
        return this.viewModel.isChecked();
    }

    public void setChecked(boolean checked) {
        boolean before = this.viewModel.isChecked();
        this.viewModel.setChecked(checked);
        this.notifyChangeIfNeeded(before);
    }

    // <editor-fold defaultstate="collapsed" desc="*** setter & getter ***">
    public void setChangeListener(ValueListener<Boolean> listener) {
        this.changeListener = listener;
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

    public void setStyle(CheckboxStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float size) {
        this.view.setSize(size);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }

    private void notifyChangeIfNeeded(boolean previousValue) {
        boolean currentValue = this.viewModel.isChecked();
        if (previousValue != currentValue && this.changeListener != null) {
            this.changeListener.onChange(currentValue);
        }
    }
    // </editor-fold>
}
