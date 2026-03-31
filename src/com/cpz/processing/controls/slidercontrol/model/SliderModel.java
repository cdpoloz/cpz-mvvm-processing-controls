package com.cpz.processing.controls.slidercontrol.model;

import com.cpz.processing.controls.common.Enableable;
import com.cpz.processing.controls.slidercontrol.SnapMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SliderModel implements Enableable {

    private static final BigDecimal DEFAULT_MIN = BigDecimal.ZERO;
    private static final BigDecimal DEFAULT_MAX = BigDecimal.ONE;
    private static final BigDecimal DEFAULT_STEP = new BigDecimal("0.01");
    private static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;

    private BigDecimal min = DEFAULT_MIN;
    private BigDecimal max = DEFAULT_MAX;
    private BigDecimal step = DEFAULT_STEP;
    private BigDecimal value = DEFAULT_VALUE;
    private SnapMode snapMode = SnapMode.ALWAYS;
    private boolean enabled = true;

    public SliderModel() {
        setValue(DEFAULT_VALUE);
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        BigDecimal normalizedMin = requireNonNull(min, "min");
        if (normalizedMin.compareTo(max) >= 0) {
            throw new IllegalArgumentException("min must be < max");
        }
        this.min = normalizedMin;
        this.value = clampValue(value);
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        BigDecimal normalizedMax = requireNonNull(max, "max");
        if (normalizedMax.compareTo(min) <= 0) {
            throw new IllegalArgumentException("max must be > min");
        }
        this.max = normalizedMax;
        this.value = clampValue(value);
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        BigDecimal normalizedStep = requireNonNull(step, "step");
        if (normalizedStep.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("step must be > 0");
        }
        this.step = normalizedStep;
        this.value = clampAndSnap(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = normalizeValue(requireNonNull(value, "value"), shouldSnapImmediately());
    }

    public BigDecimal normalizeValue(BigDecimal value, boolean snap) {
        BigDecimal normalizedValue = requireNonNull(value, "value");
        return snap ? clampAndSnap(normalizedValue) : clampValue(normalizedValue);
    }

    public SnapMode getSnapMode() {
        return snapMode;
    }

    public void setSnapMode(SnapMode snapMode) {
        this.snapMode = snapMode == null ? SnapMode.ALWAYS : snapMode;
        this.value = normalizeValue(value, shouldSnapImmediately());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private BigDecimal clampAndSnap(BigDecimal rawValue) {
        BigDecimal clamped = clampValue(rawValue);
        BigDecimal offset = clamped.subtract(min);
        BigDecimal steps = offset.divide(step, 0, RoundingMode.HALF_UP);
        BigDecimal snapped = min.add(steps.multiply(step));
        return snapped.max(min).min(max);
    }

    private BigDecimal clampValue(BigDecimal rawValue) {
        return rawValue.max(min).min(max);
    }

    private boolean shouldSnapImmediately() {
        return snapMode == SnapMode.ALWAYS;
    }

    private static BigDecimal requireNonNull(BigDecimal value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        return value;
    }
}
