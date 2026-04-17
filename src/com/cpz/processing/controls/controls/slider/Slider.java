package com.cpz.processing.controls.controls.slider;

import com.cpz.processing.controls.common.binding.ValueListener;
import com.cpz.processing.controls.controls.Control;
import com.cpz.processing.controls.controls.slider.input.SliderInputAdapter;
import com.cpz.processing.controls.controls.slider.model.SliderModel;
import com.cpz.processing.controls.controls.slider.model.SliderOrientation;
import com.cpz.processing.controls.controls.slider.model.SnapMode;
import com.cpz.processing.controls.controls.slider.style.SliderStyle;
import com.cpz.processing.controls.controls.slider.view.SliderView;
import com.cpz.processing.controls.controls.slider.viewmodel.SliderViewModel;
import com.cpz.processing.controls.core.input.PointerEvent;
import com.cpz.processing.controls.core.util.ControlCode;
import processing.core.PApplet;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

/**
 * Convenience facade for the slider control.
 */
public final class Slider implements Control {
    private final SliderModel model;
    private final SliderViewModel viewModel;
    private final SliderView view;
    private final SliderInputAdapter inputAdapter;

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
        Objects.requireNonNull(sketch, "sketch");
        this.model = new SliderModel(code, min, max, step, value, snapMode);
        this.viewModel = new SliderViewModel(this.model);
        this.view = new SliderView(sketch, this.viewModel, x, y, width, height, orientation);
        this.inputAdapter = new SliderInputAdapter(this.view, this.viewModel);
    }

    public void draw() {
        this.view.draw();
    }

    public void handlePointerEvent(PointerEvent event) {
        this.inputAdapter.handlePointerEvent(event);
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
    }

    public void setPosition(float x, float y) {
        this.view.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        this.view.setSize(width, height);
    }
}
