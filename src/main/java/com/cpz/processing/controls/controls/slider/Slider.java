package com.cpz.processing.controls.controls.slider;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.ParentSizeAwareControl;
import com.cpz.processing.controls.controls.PointerRoutableControl;
import com.cpz.processing.controls.controls.geometry.ControlBounds;
import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import com.cpz.processing.controls.controls.geometry.ResolvedBounds;
import com.cpz.processing.controls.controls.slider.config.SliderStyleConfig;
import com.cpz.processing.controls.controls.slider.input.SliderInputAdapter;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
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

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

/**
 * Convenience facade for the slider control.
 *
 * @author CPZ
 */
public final class Slider implements PointerRoutableControl, ParentSizeAwareControl, TooltipAttachable {
    private final PApplet sketch;
    private final SliderModel model;
    private final SliderViewModel viewModel;
    private final SliderView view;
    private final SliderInputAdapter inputAdapter;
    private final TooltipSupport tooltipSupport;
    private ControlBounds bounds;
    private ControlMeasure textSize;
    private boolean textSizeStyleIsolated;
    private Float parentWidth;
    private Float parentHeight;

    public Slider(PApplet sketch, float x, float y, float width, float height) {
        this(
                sketch,
                ControlCode.auto("slider"),
                BigDecimal.ZERO,
                BigDecimal.ONE,
                new BigDecimal("0.01"),
                BigDecimal.ZERO,
                x,
                y,
                width,
                height,
                SliderOrientation.HORIZONTAL,
                SnapMode.ALWAYS
        );
    }

    public Slider(PApplet sketch, String code, float x, float y, float width, float height) {
        this(
                sketch,
                code,
                BigDecimal.ZERO,
                BigDecimal.ONE,
                new BigDecimal("0.01"),
                BigDecimal.ZERO,
                x,
                y,
                width,
                height,
                SliderOrientation.HORIZONTAL,
                SnapMode.ALWAYS
        );
    }

    public Slider(
            PApplet sketch,
            String code,
            BigDecimal min,
            BigDecimal max,
            BigDecimal step,
            BigDecimal value,
            float x,
            float y,
            float width,
            float height
    ) {
        this(sketch, code, min, max, step, value, x, y, width, height, SliderOrientation.HORIZONTAL, SnapMode.ALWAYS);
    }

    public Slider(
            PApplet sketch,
            String code,
            BigDecimal min,
            BigDecimal max,
            BigDecimal step,
            BigDecimal value,
            float x,
            float y,
            float width,
            float height,
            SliderOrientation orientation
    ) {
        this(sketch, code, min, max, step, value, x, y, width, height, orientation, SnapMode.ALWAYS);
    }

    public Slider(
            PApplet sketch,
            String code,
            BigDecimal min,
            BigDecimal max,
            BigDecimal step,
            BigDecimal value,
            float x,
            float y,
            float width,
            float height,
            SliderOrientation orientation,
            SnapMode snapMode
    ) {
        this(
                sketch,
                code,
                min,
                max,
                step,
                value,
                ControlBounds.absolute(x, y, width, height),
                orientation,
                snapMode
        );
    }

    public Slider(PApplet sketch, ControlBounds bounds) {
        this(
                sketch,
                ControlCode.auto("slider"),
                BigDecimal.ZERO,
                BigDecimal.ONE,
                new BigDecimal("0.01"),
                BigDecimal.ZERO,
                bounds,
                SliderOrientation.HORIZONTAL,
                SnapMode.ALWAYS
        );
    }

    public Slider(PApplet sketch, String code, ControlBounds bounds) {
        this(
                sketch,
                code,
                BigDecimal.ZERO,
                BigDecimal.ONE,
                new BigDecimal("0.01"),
                BigDecimal.ZERO,
                bounds,
                SliderOrientation.HORIZONTAL,
                SnapMode.ALWAYS
        );
    }

    public Slider(
            PApplet sketch,
            String code,
            BigDecimal min,
            BigDecimal max,
            BigDecimal step,
            BigDecimal value,
            ControlBounds bounds,
            SliderOrientation orientation,
            SnapMode snapMode
    ) {
        this.sketch = Objects.requireNonNull(sketch, "sketch");
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        ResolvedBounds resolvedBounds = this.resolveBounds();
        this.model = new SliderModel(code, min, max, step, value, snapMode);
        this.viewModel = new SliderViewModel(this.model);
        this.view = new SliderView(
                sketch,
                this.viewModel,
                resolvedBounds.x(),
                resolvedBounds.y(),
                resolvedBounds.width(),
                resolvedBounds.height(),
                orientation
        );
        this.inputAdapter = new SliderInputAdapter(this.view, this.viewModel);
        this.tooltipSupport = new TooltipSupport(this.view::getTooltipBounds, this::isVisible);
    }

    public void draw() {
        this.applyResolvedGeometryAndTextSize();
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        this.applyResolvedGeometryAndTextSize();
        this.inputAdapter.handlePointerEvent(event);
    }

    public boolean canConsumePointerEvent(PointerEvent event) {
        this.applyResolvedGeometryAndTextSize();
        if (event == null || !this.viewModel.isVisible()) {
            return false;
        }
        switch (event.getType()) {
            case MOVE:
            case PRESS:
            case WHEEL:
                return this.viewModel.isEnabled() && this.view.contains(event.getX(), event.getY());
            case DRAG:
            case RELEASE:
                return this.viewModel.isDragging()
                        || (this.viewModel.isEnabled() && this.view.contains(event.getX(), event.getY()));
            default:
                return false;
        }
    }

    public String getCode() {
        return this.model.getCode();
    }

    public BigDecimal getValue() {
        return this.viewModel.getValue();
    }

    public void setValue(BigDecimal value) {
        this.viewModel.setValue(value);
    }

    public BigDecimal getMin() {
        return this.viewModel.getMin();
    }

    public void setMin(BigDecimal min) {
        this.viewModel.setMin(min);
    }

    public BigDecimal getMax() {
        return this.viewModel.getMax();
    }

    public void setMax(BigDecimal max) {
        this.viewModel.setMax(max);
    }

    public BigDecimal getStep() {
        return this.viewModel.getStep();
    }

    public void setStep(BigDecimal step) {
        this.viewModel.setStep(step);
    }

    public SnapMode getSnapMode() {
        return this.viewModel.getSnapMode();
    }

    public void setSnapMode(SnapMode snapMode) {
        this.viewModel.setSnapMode(snapMode);
    }

    public String getFormattedValue() {
        return this.viewModel.getFormattedValue();
    }

    public void setFormatter(Function<BigDecimal, String> formatter) {
        this.viewModel.setFormatter(formatter);
    }

    public boolean isShowValueText() {
        return this.viewModel.isShowText();
    }

    public void setShowValueText(boolean showValueText) {
        this.viewModel.setShowText(showValueText);
    }

    public void setChangeListener(ValueListener<BigDecimal> listener) {
        this.viewModel.setOnValueChanged(listener == null ? null : listener::onChange);
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

    public SliderOrientation getOrientation() {
        return this.view.getOrientation();
    }

    public void setOrientation(SliderOrientation orientation) {
        this.view.setOrientation(orientation);
    }

    public void setStyle(SliderStyle style) {
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

    public void setSize(float width, float height) {
        this.bounds = this.bounds.withSize(ControlMeasure.absolute(width), ControlMeasure.absolute(height));
        this.applyResolvedGeometryAndTextSize();
    }

    public void setBounds(ControlBounds bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
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

    public Slider setTooltip(String text) {
        this.tooltipSupport.setTooltip(text);
        return this;
    }

    public Slider setTooltip(Tooltip tooltip) {
        this.tooltipSupport.setTooltip(tooltip);
        return this;
    }

    public Slider setTooltipText(String text) {
        this.tooltipSupport.setTooltipText(text);
        return this;
    }

    public Slider setTooltipStyle(TooltipStyleConfig styleConfig) {
        this.tooltipSupport.setTooltipStyle(styleConfig);
        return this;
    }

    public Slider setTooltipFont(PFont font) {
        this.tooltipSupport.setTooltipFont(font);
        return this;
    }

    public Slider setTooltipTextSize(float size) {
        this.tooltipSupport.setTooltipTextSize(size);
        return this;
    }

    public Slider setTooltipBackgroundColor(int argb) {
        this.tooltipSupport.setTooltipBackgroundColor(argb);
        return this;
    }

    public Slider setTooltipTextColor(int argb) {
        this.tooltipSupport.setTooltipTextColor(argb);
        return this;
    }

    public Slider setTooltipBorderColor(int argb) {
        this.tooltipSupport.setTooltipBorderColor(argb);
        return this;
    }

    public Slider setTooltipPadding(float padding) {
        this.tooltipSupport.setTooltipPadding(padding);
        return this;
    }

    public Slider setTooltipOffset(float offset) {
        this.tooltipSupport.setTooltipOffset(offset);
        return this;
    }

    public Slider clearTooltip() {
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
        this.view.setSize(resolvedBounds.width(), resolvedBounds.height());
        this.applyResolvedTextSize();
    }

    private void applyResolvedTextSize() {
        if (this.textSize == null) {
            return;
        }
        this.ensureTextSizeStyleIsolated();
        SliderStyleConfig styleConfig = this.view.getStyle().getSliderStyleConfig();
        if (styleConfig != null) {
            styleConfig.textSize = this.textSize.resolve(this.parentHeight());
            styleConfig.font = FontLoader.resolve(styleConfig.fontResolver, this.sketch, styleConfig.textSize, styleConfig.font);
        }
    }

    private void ensureTextSizeStyleIsolated() {
        if (this.textSizeStyleIsolated) {
            return;
        }
        SliderStyle style = this.view.getStyle();
        if (style != null) {
            this.view.setStyle(style.copy());
        }
        this.textSizeStyleIsolated = true;
    }
}
