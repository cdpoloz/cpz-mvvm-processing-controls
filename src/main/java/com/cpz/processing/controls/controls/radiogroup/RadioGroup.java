package com.cpz.processing.controls.controls.radiogroup;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.radiogroup.input.RadioGroupInputAdapter;
import com.cpz.processing.controls.controls.radiogroup.model.RadioGroupModel;
import com.cpz.processing.controls.controls.radiogroup.style.RadioGroupStyle;
import com.cpz.processing.controls.controls.radiogroup.view.RadioGroupView;
import com.cpz.processing.controls.controls.radiogroup.viewmodel.RadioGroupViewModel;
import com.cpz.processing.controls.core.focus.FocusManager;
import com.cpz.processing.controls.core.input.KeyboardEvent;
import com.cpz.processing.controls.core.input.KeyboardInputAdapter;
import com.cpz.processing.controls.core.input.PointerEvent;
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
 * Convenience facade for the radio group control.
 *
 * @author CPZ
 */
public final class RadioGroup implements Control, TooltipAttachable {
    private final RadioGroupModel model;
    private final RadioGroupViewModel viewModel;
    private final RadioGroupView view;
    private final FocusManager focusManager;
    private final RadioGroupInputAdapter inputAdapter;
    private final KeyboardInputAdapter keyboardInputAdapter;
    private final TooltipSupport tooltipSupport;

    public RadioGroup(PApplet sketch, List<String> options, float x, float y, float width) {
        this(sketch, ControlCode.auto("radiogroup"), options, -1, x, y, width);
    }

    public RadioGroup(PApplet sketch, List<String> options, int selectedIndex, float x, float y, float width) {
        this(sketch, ControlCode.auto("radiogroup"), options, selectedIndex, x, y, width);
    }

    public RadioGroup(PApplet sketch, String code, List<String> options, float x, float y, float width) {
        this(sketch, code, options, -1, x, y, width);
    }

    public RadioGroup(PApplet sketch, String code, List<String> options, int selectedIndex, float x, float y, float width) {
        Objects.requireNonNull(sketch, "sketch");
        this.model = new RadioGroupModel(code, options, selectedIndex);
        this.viewModel = new RadioGroupViewModel(this.model);
        this.view = new RadioGroupView(sketch, this.viewModel, x, y, width);
        this.focusManager = new FocusManager();
        this.inputAdapter = new RadioGroupInputAdapter(this.view, this.viewModel, this.focusManager);
        this.keyboardInputAdapter = new KeyboardInputAdapter(this.focusManager);
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
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

    public List<String> getOptions() {
        return this.model.getOptions();
    }

    public void setOptions(List<String> options) {
        this.viewModel.setOptions(options);
    }

    public int getSelectedIndex() {
        return this.viewModel.getSelectedIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        this.viewModel.setSelectedIndex(selectedIndex);
    }

    public String getSelectedOption() {
        int selectedIndex = this.viewModel.getSelectedIndex();
        List<String> options = this.model.getOptions();
        return selectedIndex >= 0 && selectedIndex < options.size() ? options.get(selectedIndex) : null;
    }

    public void setChangeListener(ValueListener<Integer> listener) {
        this.viewModel.setOnOptionSelected(listener == null ? null : listener::onChange);
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

    public void setStyle(RadioGroupStyle style) {
        this.view.setStyle(style);
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setWidth(float width) {
        this.view.setWidth(width);
    }

    public void setItemHeight(float itemHeight) {
        this.view.setItemHeight(itemHeight);
    }

    public void setItemSpacing(float itemSpacing) {
        this.view.setItemSpacing(itemSpacing);
    }

    public RadioGroup setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public RadioGroup setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public RadioGroup setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public RadioGroup setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public RadioGroup setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public RadioGroup setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public RadioGroup setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public RadioGroup setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public RadioGroup setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public RadioGroup setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public RadioGroup setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public RadioGroup clearTooltip() {
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
