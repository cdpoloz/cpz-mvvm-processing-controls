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
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.List;
import java.util.Objects;

/**
 * Convenience facade for the drop down control.
 *
 * @author CPZ
 */
public final class DropDown implements Control, TooltipAttachable {
    private static final int DEFAULT_OVERLAY_Z_INDEX = 100;

    private final DropDownModel model;
    private final DropDownViewModel viewModel;
    private final DropDownView view;
    private final FocusManager focusManager;
    private final DropDownOverlayController overlayController;
    private final DropDownInputAdapter inputAdapter;
    private final TooltipSupport tooltipSupport;

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
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
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

    public DropDown setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public DropDown setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public DropDown setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public DropDown setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public DropDown setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public DropDown setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public DropDown setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public DropDown setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public DropDown setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public DropDown setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public DropDown setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public DropDown clearTooltip() {
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
