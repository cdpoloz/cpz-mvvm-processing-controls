package com.cpz.processing.controls.controls.toggle;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.toggle.input.ToggleInputAdapter;
import com.cpz.processing.controls.controls.toggle.model.ToggleModel;
import com.cpz.processing.controls.controls.toggle.style.ToggleStyle;
import com.cpz.processing.controls.controls.toggle.view.ToggleView;
import com.cpz.processing.controls.controls.toggle.viewmodel.ToggleViewModel;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.overlay.tooltip.Tooltip;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipBounds;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipAttachable;
import com.cpz.processing.controls.core.overlay.tooltip.TooltipSupport;
import com.cpz.processing.controls.core.overlay.tooltip.config.TooltipStyleConfig;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.Objects;

/**
 * Convenience facade for the toggle control.
 *
 * @author CPZ
 */
public final class Toggle implements PointerRoutableControl, ParentSizeAwareControl, TooltipAttachable {
    private final PApplet sketch;
    private final ToggleModel model;
    private final ToggleViewModel viewModel;
    private final ToggleView view;
    private final ToggleInputAdapter inputAdapter;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private Float parentWidth;
    private Float parentHeight;
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
        this(sketch, code, initialState, totalStates, ControlBounds.absolute(x, y, width, height));
    }

    public Toggle(PApplet sketch, ControlBounds bounds) {
        this(sketch, ControlCode.auto("toggle"), 0, 2, bounds);
    }

    public Toggle(PApplet sketch, String code, ControlBounds bounds) {
        this(sketch, code, 0, 2, bounds);
    }

    public Toggle(PApplet sketch, String code, int initialState, int totalStates, ControlBounds bounds) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.model = new ToggleModel(code);
        this.viewModel = new ToggleViewModel(this.model);
        this.view = new ToggleView(
                sketch,
                this.viewModel,
                resolvedBounds.x(),
                resolvedBounds.y(),
                resolvedBounds.width(),
                resolvedBounds.height()
        );
        this.inputAdapter = new ToggleInputAdapter(this.view, this.viewModel);
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
        this.setTotalStates(totalStates);
        this.setState(initialState);
    }

    public void draw() {
        this.applyResolvedBounds();
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        this.applyResolvedBounds();
        int before = this.viewModel.getState();
        this.inputAdapter.handlePointerEvent(event);
        this.notifyChangeIfNeeded(before);
    }

    public boolean canConsumePointerEvent(PointerEvent event) {
        this.applyResolvedBounds();
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
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedBounds();
    }

    public void setSize(float size) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(size), ControlMeasure.absolute(size));
        this.applyResolvedBounds();
    }

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedBounds();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.applyResolvedBounds();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedBounds();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedBounds();
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

    public Toggle setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
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
        this.applyResolvedBounds();
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

    private ResolvedBounds resolveBounds() {
        float width = this.parentWidth != null ? this.parentWidth : this.sketch.width;
        float height = this.parentHeight != null ? this.parentHeight : this.sketch.height;
        return this.bounds.resolve(width, height);
    }

    private void applyResolvedBounds() {
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.view.setPosition(resolvedBounds.x(), resolvedBounds.y());
        this.view.setSize(resolvedBounds.width(), resolvedBounds.height());
    }
}
