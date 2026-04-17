package com.cpz.processing.controls.controls.toggle;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputAdapter;
import com.cpz.processing.controls.controls.toggle.model.ToggleModel;
import com.cpz.processing.controls.controls.toggle.style.ToggleStyle;
import com.cpz.processing.controls.controls.toggle.view.ToggleView;
import com.cpz.processing.controls.controls.toggle.viewmodel.ToggleViewModel;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.util.Objects;

/**
 * Convenience facade for the toggle control.
 */
public final class Toggle implements Control {
    private final ToggleModel model;
    private final ToggleViewModel viewModel;
    private final ToggleView view;
    private final ToggleInputAdapter inputAdapter;
    private ValueListener<Integer> changeListener;

    public Toggle(PApplet sketch, float x, float y, float size) {
        this(sketch, ControlCode.auto("toggle"), 0, 2, x, y, size, size);
    }

    public Toggle(PApplet sketch, float x, float y, float width, float height) {
        this(sketch, ControlCode.auto("toggle"), 0, 2, x, y, width, height);
    }

    public Toggle(PApplet sketch, String code, float x, float y, float size) {
        this(sketch, code, 0, 2, x, y, size, size);
    }

    public Toggle(PApplet sketch, String code, float x, float y, float width, float height) {
        this(sketch, code, 0, 2, x, y, width, height);
    }

    public Toggle(PApplet sketch, String code, int initialState, int totalStates, float x, float y, float size) {
        this(sketch, code, initialState, totalStates, x, y, size, size);
    }

    public Toggle(PApplet sketch, String code, int initialState, int totalStates, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new ToggleModel(code);
        this.viewModel = new ToggleViewModel(this.model);
        this.view = new ToggleView(sketch, this.viewModel, x, y, width, height);
        this.inputAdapter = new ToggleInputAdapter(this.view, this.viewModel);
        this.setTotalStates(totalStates);
        this.setState(initialState);
    }

    public void draw() {
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        int before = this.viewModel.getState();
        this.inputAdapter.handlePointerEvent(event);
        this.notifyChangeIfNeeded(before);
    }

    public String getCode() {
        return this.model.getCode();
    }

    public int getState() {
        return this.viewModel.getState();
    }

    public void setState(int state) {
        int before = this.viewModel.getState();
        int totalStates = this.viewModel.getTotalStates();
        int normalized = Math.max(0, Math.min(totalStates - 1, state));
        this.model.setPrevState(before);
        this.model.setState(normalized);
        this.notifyChangeIfNeeded(before);
    }

    public int getPrevState() {
        return this.viewModel.getPrevState();
    }

    public int getTotalStates() {
        return this.viewModel.getTotalStates();
    }

    public void setTotalStates(int totalStates) {
        int before = this.viewModel.getState();
        this.viewModel.setTotalStates(totalStates);
        this.notifyChangeIfNeeded(before);
    }

    public void setChangeListener(ValueListener<Integer> listener) {
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

    public void setStyle(ToggleStyle style) {
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

    private void notifyChangeIfNeeded(int previousState) {
        int currentState = this.viewModel.getState();
        if (previousState != currentState && this.changeListener != null) {
            this.changeListener.onChange(currentState);
        }
    }
}
