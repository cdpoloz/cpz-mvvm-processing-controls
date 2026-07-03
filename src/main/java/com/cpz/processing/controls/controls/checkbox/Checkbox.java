package com.cpz.processing.controls.controls.checkbox;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.checkbox.input.CheckboxInputAdapter;
import com.cpz.processing.controls.controls.checkbox.model.CheckboxModel;
import com.cpz.processing.controls.controls.checkbox.style.CheckboxStyle;
import com.cpz.processing.controls.controls.checkbox.view.CheckboxView;
import com.cpz.processing.controls.controls.checkbox.viewmodel.CheckboxViewModel;
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
 * Convenience facade for the checkbox control.
 *
 * @author CPZ
 */
public final class Checkbox implements PointerRoutableControl, TooltipAttachable {
    private final CheckboxModel model;
    private final CheckboxViewModel viewModel;
    private final CheckboxView view;
    private final CheckboxInputAdapter inputAdapter;
    private final TooltipSupport tooltipSupport;
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
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
    }

    public void draw() {
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        boolean before = this.viewModel.isChecked();
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

    public Checkbox setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public Checkbox setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public Checkbox setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public Checkbox setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public Checkbox setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public Checkbox setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public Checkbox setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public Checkbox setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public Checkbox setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public Checkbox setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public Checkbox setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public Checkbox clearTooltip() {
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
    // </editor-fold>
}
