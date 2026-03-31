package com.cpz.processing.controls.slidercontrol.viewmodel;

import com.cpz.processing.controls.common.viewmodel.AbstractControlViewModel;
import com.cpz.processing.controls.slidercontrol.SnapMode;
import com.cpz.processing.controls.slidercontrol.model.SliderModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SliderViewModel extends AbstractControlViewModel<SliderModel> {

    private boolean hovered;
    private boolean pressed;
    private boolean dragging;
    private boolean showText = true;

    private Function<BigDecimal, String> formatter;
    private Consumer<BigDecimal> onValueChanged;

    public SliderViewModel(SliderModel model) {
        super(model);
    }

    public void onPointerMoved(boolean inside) {
        hovered = isInteractive() && inside;
        if (!isInteractive()) {
            resetInteractionState();
        }
    }

    public void onPointerPressed(float normalizedValue) {
        if (!isInteractive()) {
            resetInteractionState();
            return;
        }
        hovered = true;
        pressed = true;
        dragging = true;
        applyNormalizedValue(normalizedValue, shouldSnapDuringInteraction());
    }

    public void onPointerDragged(float normalizedValue) {
        if (!dragging || !isInteractive()) {
            return;
        }
        pressed = true;
        applyNormalizedValue(normalizedValue, shouldSnapDuringInteraction());
    }

    public void onPointerReleased() {
        if (dragging && model.getSnapMode() == SnapMode.ON_RELEASE) {
            applyNormalizedValue(getNormalizedValue(), true);
        }
        dragging = false;
        pressed = false;
    }

    public void onMouseWheel(float delta, boolean isShiftDown, boolean isCtrlDown) {
        if (!isInteractive() || !hovered || delta == 0f) {
            return;
        }

        BigDecimal deltaStep = model.getStep();
        if (isShiftDown) {
            deltaStep = deltaStep.multiply(BigDecimal.TEN);
        } else if (isCtrlDown) {
            deltaStep = deltaStep.multiply(new BigDecimal("0.1"));
        }

        BigDecimal previousValue = model.getValue();
        BigDecimal direction = BigDecimal.valueOf(-Math.signum(delta));
        BigDecimal nextValue = previousValue.add(direction.multiply(deltaStep));
        model.setValue(model.normalizeValue(nextValue, true));
        notifyValueChanged(previousValue);
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isDragging() {
        return dragging;
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public BigDecimal getValue() {
        return model.getValue();
    }

    public void setValue(BigDecimal value) {
        BigDecimal previousValue = model.getValue();
        model.setValue(value);
        notifyValueChanged(previousValue);
    }

    public BigDecimal getMin() {
        return model.getMin();
    }

    public void setMin(BigDecimal min) {
        BigDecimal previousValue = model.getValue();
        model.setMin(min);
        notifyValueChanged(previousValue);
    }

    public BigDecimal getMax() {
        return model.getMax();
    }

    public void setMax(BigDecimal max) {
        BigDecimal previousValue = model.getValue();
        model.setMax(max);
        notifyValueChanged(previousValue);
    }

    public BigDecimal getStep() {
        return model.getStep();
    }

    public void setStep(BigDecimal step) {
        BigDecimal previousValue = model.getValue();
        model.setStep(step);
        notifyValueChanged(previousValue);
    }

    public SnapMode getSnapMode() {
        return model.getSnapMode();
    }

    public void setSnapMode(SnapMode snapMode) {
        BigDecimal previousValue = model.getValue();
        model.setSnapMode(snapMode);
        notifyValueChanged(previousValue);
    }

    public float getNormalizedValue() {
        BigDecimal span = model.getMax().subtract(model.getMin());
        if (span.signum() == 0) {
            return 0f;
        }
        BigDecimal offset = model.getValue().subtract(model.getMin());
        return clampNormalized(offset.divide(span, 6, RoundingMode.HALF_UP).floatValue());
    }

    public String getFormattedValue() {
        BigDecimal value = model.getValue();
        if (formatter != null) {
            return formatter.apply(value);
        }
        int scale = Math.max(0, model.getStep().scale());
        return value.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    public void setFormatter(Function<BigDecimal, String> formatter) {
        this.formatter = formatter;
    }

    public void setOnValueChanged(Consumer<BigDecimal> onValueChanged) {
        this.onValueChanged = onValueChanged;
    }

    @Override
    protected void onAvailabilityChanged() {
        if (!isInteractive()) {
            resetInteractionState();
        }
    }

    private boolean isInteractive() {
        return isEnabled() && isVisible();
    }

    private void applyNormalizedValue(float normalizedValue, boolean snap) {
        float clampedNormalized = clampNormalized(normalizedValue);
        BigDecimal min = model.getMin();
        BigDecimal max = model.getMax();
        BigDecimal span = max.subtract(min);
        BigDecimal nextValue = min.add(span.multiply(BigDecimal.valueOf(clampedNormalized)));
        BigDecimal previousValue = model.getValue();
        if (snap) {
            model.setValue(nextValue);
        } else {
            model.setValue(model.normalizeValue(nextValue, false));
        }
        notifyValueChanged(previousValue);
    }

    private boolean shouldSnapDuringInteraction() {
        return model.getSnapMode() == SnapMode.ALWAYS;
    }

    private void notifyValueChanged(BigDecimal previousValue) {
        if (onValueChanged != null && previousValue.compareTo(model.getValue()) != 0) {
            onValueChanged.accept(model.getValue());
        }
    }

    private void resetInteractionState() {
        hovered = false;
        pressed = false;
        dragging = false;
    }

    private static float clampNormalized(float normalized) {
        return Math.max(0f, Math.min(1f, normalized));
    }
}
