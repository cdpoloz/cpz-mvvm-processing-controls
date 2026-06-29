package com.cpz.processing.controls.controls.toggle;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputAdapter;
import com.cpz.processing.controls.controls.toggle.model.ToggleModel;
import com.cpz.processing.controls.controls.toggle.style.ToggleStyle;
import com.cpz.processing.controls.controls.toggle.view.ToggleView;
import com.cpz.processing.controls.controls.toggle.viewmodel.ToggleViewModel;
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
 * Convenience facade for the toggle control.
 *
 * @author CPZ
 */
public final class Toggle implements Control, TooltipTarget {
    private final ToggleModel model;
    private final ToggleViewModel viewModel;
    private final ToggleView view;
    private final ToggleInputAdapter inputAdapter;
    private final TooltipSupport tooltipSupport;
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
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible, this::isEnabled);
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

    public boolean canConsumePointerEvent(PointerEvent event) {
        return event != null
                && event.getType() != PointerEvent.Type.WHEEL
                && this.isVisible()
                && this.view.contains(event.getX(), event.getY());
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

    public Toggle setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public Toggle setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public Toggle setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public Toggle setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public Toggle setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public Toggle setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public Toggle setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public Toggle setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public Toggle setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public Toggle setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public Toggle clearTooltip() {
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
