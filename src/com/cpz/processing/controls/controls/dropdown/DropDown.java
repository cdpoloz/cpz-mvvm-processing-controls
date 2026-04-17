package com.cpz.processing.controls.controls.dropdown;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.dropdown.input.DropDownInputAdapter;
import com.cpz.processing.controls.controls.dropdown.model.DropDownModel;
import com.cpz.processing.controls.controls.dropdown.style.DefaultDropDownStyle;
import com.cpz.processing.controls.controls.dropdown.util.DropDownOverlayController;
import com.cpz.processing.controls.controls.dropdown.view.DropDownView;
import com.cpz.processing.controls.controls.dropdown.viewmodel.DropDownViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.InputManager;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.OverlayManager;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.util.List;
import java.util.Objects;

/**
 * Convenience facade for the drop down control.
 */
public final class DropDown implements Control {
    private static final int DEFAULT_OVERLAY_Z_INDEX = 100;

    private final DropDownModel model;
    private final DropDownViewModel viewModel;
    private final DropDownView view;
    private final FocusManager focusManager;
    private final DropDownOverlayController overlayController;
    private final DropDownInputAdapter inputAdapter;

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, List<String> items, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, ControlCode.auto("dropdown"), items, -1, x, y, width, height);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, List<String> items, int selectedIndex, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, ControlCode.auto("dropdown"), items, selectedIndex, x, y, width, height);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, String code, List<String> items, float x, float y, float width, float height) {
        this(sketch, overlayManager, inputManager, code, items, -1, x, y, width, height);
    }

    public DropDown(PApplet sketch, OverlayManager overlayManager, InputManager inputManager, String code, List<String> items, int selectedIndex, float x, float y, float width, float height) {
        Objects.requireNonNull(sketch, "sketch");
        Objects.requireNonNull(overlayManager, "overlayManager");
        Objects.requireNonNull(inputManager, "inputManager");
        this.model = new DropDownModel(code, items, selectedIndex);
        this.viewModel = new DropDownViewModel(this.model);
        this.view = new DropDownView(sketch, this.viewModel, x, y, width, height);
        this.focusManager = new FocusManager();
        this.overlayController = new DropDownOverlayController(this.view, this.viewModel, this.focusManager, overlayManager, inputManager, DEFAULT_OVERLAY_Z_INDEX);
        this.inputAdapter = new DropDownInputAdapter(this.view, this.viewModel, this.focusManager, this.overlayController);
    }

    public void draw() {
        this.overlayController.syncRegistration();
        if (!this.viewModel.isExpanded()) {
            this.view.draw();
        }
    }

    public void handlePointerEvent(PointerEvent event) {
        if (event != null) {
            this.inputAdapter.handlePointerEvent(event);
            this.overlayController.syncRegistration();
        }
    }

    public void dispose() {
        this.overlayController.dispose();
    }

    public String getCode() {
        return this.model.getCode();
    }

    public List<String> getItems() {
        return this.model.getItems();
    }

    public void setItems(List<String> items) {
        this.viewModel.setItems(items);
        this.overlayController.syncRegistration();
    }

    public int getSelectedIndex() {
        return this.viewModel.getSelectedIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        this.viewModel.selectIndex(selectedIndex);
    }

    public String getSelectedItem() {
        int selectedIndex = this.viewModel.getSelectedIndex();
        List<String> items = this.model.getItems();
        return selectedIndex >= 0 && selectedIndex < items.size() ? items.get(selectedIndex) : null;
    }

    public boolean isExpanded() {
        return this.viewModel.isExpanded();
    }

    public boolean isFocused() {
        return this.viewModel.isFocused();
    }

    public void setChangeListener(ValueListener<Integer> listener) {
        this.viewModel.setOnSelectionChanged(listener == null ? null : listener::onChange);
    }

    public boolean isEnabled() {
        return this.viewModel.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.viewModel.setEnabled(enabled);
        this.overlayController.syncRegistration();
    }

    public boolean isVisible() {
        return this.viewModel.isVisible();
    }

    public void setVisible(boolean visible) {
        this.viewModel.setVisible(visible);
        this.overlayController.syncRegistration();
    }

    public void setStyle(DefaultDropDownStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }
}
