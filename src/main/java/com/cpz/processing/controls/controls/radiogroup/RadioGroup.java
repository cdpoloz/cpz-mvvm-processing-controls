package com.cpz.processing.controls.controls.radiogroup;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.KeyboardRoutableControl;
import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.radiogroup.config.RadioGroupStyleConfig;
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
import com.cpz.processing.controls.core.util.FontLoader;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.List;
import java.util.Objects;

/**
 * Convenience facade for the radio group control.
 *
 * @author CPZ
 */
public final class RadioGroup implements PointerRoutableControl, KeyboardRoutableControl, ParentSizeAwareControl, TooltipAttachable {
    private final PApplet sketch;
    private final RadioGroupModel model;
    private final RadioGroupViewModel viewModel;
    private final RadioGroupView view;
    private final FocusManager focusManager;
    private final RadioGroupInputAdapter inputAdapter;
    private final KeyboardInputAdapter keyboardInputAdapter;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private ControlMeasure textSize;
    private boolean textSizeStyleIsolated;
    private Float parentWidth;
    private Float parentHeight;
    private boolean resolveItemHeightFromBounds;

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
        this(sketch, code, options, selectedIndex, ControlBounds.of(
                ControlMeasure.absolute(x),
                ControlMeasure.absolute(y),
                ControlMeasure.absolute(width),
                ControlMeasure.absolute(0.0F)
        ), false);
    }

    public RadioGroup(PApplet sketch, List<String> options, ControlBounds bounds) {
        this(sketch, ControlCode.auto("radiogroup"), options, -1, bounds);
    }

    public RadioGroup(PApplet sketch, List<String> options, int selectedIndex, ControlBounds bounds) {
        this(sketch, ControlCode.auto("radiogroup"), options, selectedIndex, bounds);
    }

    public RadioGroup(PApplet sketch, String code, List<String> options, ControlBounds bounds) {
        this(sketch, code, options, -1, bounds);
    }

    public RadioGroup(PApplet sketch, String code, List<String> options, int selectedIndex, ControlBounds bounds) {
        this(sketch, code, options, selectedIndex, bounds, true);
    }

    private RadioGroup(PApplet sketch, String code, List<String> options, int selectedIndex, ControlBounds bounds, boolean resolveItemHeightFromBounds) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.resolveItemHeightFromBounds = resolveItemHeightFromBounds;
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.model = new RadioGroupModel(code, options, selectedIndex);
        this.viewModel = new RadioGroupViewModel(this.model);
        this.view = new RadioGroupView(sketch, this.viewModel, resolvedBounds.x(), resolvedBounds.y(), resolvedBounds.width());
        if (this.resolveItemHeightFromBounds) {
            this.view.setItemHeight(resolvedBounds.height());
        }
        this.focusManager = new FocusManager();
        this.inputAdapter = new RadioGroupInputAdapter(this.view, this.viewModel, this.focusManager);
        this.keyboardInputAdapter = new KeyboardInputAdapter(this.focusManager);
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
    }

    public void draw() {
        this.applyResolvedGeometryAndTextSize();
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        this.applyResolvedGeometryAndTextSize();
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

    public boolean canConsumePointerEvent(PointerEvent event) {
        this.applyResolvedGeometryAndTextSize();
        return event != null
                && event.getType() != PointerEvent.Type.WHEEL
                && this.isVisible()
                && this.view.contains(event.getX(), event.getY());
    }

    public boolean canConsumeKeyboardEvent(KeyboardEvent event) {
        return event != null && this.viewModel.isFocused() && this.isVisible() && this.isEnabled();
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
        this.textSizeStyleIsolated = false;
        this.applyResolvedTextSize();
    }

    public void setTextSize(float textSize) {
        this.setTextSize(ControlMeasure.absolute(textSize));
    }

    public void setTextSize(ControlMeasure textSize) {
        this.textSize = Objects.requireNonNull(textSize, "textSize");
        this.applyResolvedTextSize();
    }

    public void setPosition(float x, float y) {
        this.bounds = this.bounds.withPosition(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setWidth(float width) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), this.bounds.height());
        this.applyResolvedGeometryAndTextSize();
    }

    public void setItemHeight(float itemHeight) {
        this.resolveItemHeightFromBounds = true;
        this.bounds = this.bounds.withSize(this.bounds.width(), ControlMeasure.absolute(itemHeight));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setItemSpacing(float itemSpacing) {
        this.view.setItemSpacing(itemSpacing);
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.resolveItemHeightFromBounds = true;
        this.applyResolvedGeometryAndTextSize();
    }

    public void setParentSize(float width, float height) {
        this.parentWidth = width;
        this.parentHeight = height;
        this.applyResolvedGeometryAndTextSize();
    }

    public void clearParentSize() {
        this.parentWidth = null;
        this.parentHeight = null;
        this.applyResolvedGeometryAndTextSize();
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
        this.applyResolvedGeometryAndTextSize();
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
        return this.bounds.resolve(this.parentWidth(), this.parentHeight());
    }

    private float parentWidth() {
        return this.parentWidth != null ? this.parentWidth : this.sketch.width;
    }

    private float parentHeight() {
        return this.parentHeight != null ? this.parentHeight : this.sketch.height;
    }

    private void applyResolvedGeometryAndTextSize() {
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.view.setPosition(resolvedBounds.x(), resolvedBounds.y());
        this.view.setWidth(resolvedBounds.width());
        if (this.resolveItemHeightFromBounds) {
            this.view.setItemHeight(resolvedBounds.height());
        }
        this.applyResolvedTextSize();
    }

    private void applyResolvedTextSize() {
        if (this.textSize == null) {
            return;
        }
        this.ensureTextSizeStyleIsolated();
        RadioGroupStyleConfig styleConfig = this.view.getStyle().getRadioGroupStyleConfig();
        if (styleConfig != null) {
            styleConfig.textSize = this.textSize.resolve(this.parentHeight());
            styleConfig.font = FontLoader.resolve(styleConfig.fontResolver, this.sketch, styleConfig.textSize, styleConfig.font);
        }
    }

    private void ensureTextSizeStyleIsolated() {
        if (this.textSizeStyleIsolated) {
            return;
        }
        RadioGroupStyle style = this.view.getStyle();
        if (style != null) {
            this.view.setStyle(style.copy());
        }
        this.textSizeStyleIsolated = true;
    }
}
