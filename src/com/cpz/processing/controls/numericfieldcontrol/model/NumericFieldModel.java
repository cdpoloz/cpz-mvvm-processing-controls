package com.cpz.processing.controls.numericfieldcontrol.model;

import com.cpz.processing.controls.common.Enableable;

import java.math.BigDecimal;

public final class NumericFieldModel implements Enableable {

    private BigDecimal value;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal step;
    private boolean allowNegative;
    private boolean allowDecimal;
    private int scale;
    private boolean enabled = true;

    public NumericFieldModel(BigDecimal value,
                             BigDecimal min,
                             BigDecimal max,
                             BigDecimal step,
                             boolean allowNegative,
                             boolean allowDecimal,
                             int scale) {
        this.min = min == null ? BigDecimal.ZERO : min;
        this.max = max == null ? this.min : max;
        if (this.max.compareTo(this.min) < 0) {
            BigDecimal swap = this.min;
            this.min = this.max;
            this.max = swap;
        }
        setScale(scale);
        setStep(step);
        this.allowNegative = allowNegative;
        this.allowDecimal = allowDecimal;
        setValue(value == null ? this.min : value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = clamp(value == null ? min : value);
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min == null ? BigDecimal.ZERO : min;
        if (max.compareTo(this.min) < 0) {
            max = this.min;
        }
        value = clamp(value);
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max == null ? min : max;
        if (this.max.compareTo(min) < 0) {
            this.max = min;
        }
        value = clamp(value);
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        if (step == null || step.compareTo(BigDecimal.ZERO) <= 0) {
            this.step = BigDecimal.ONE;
            return;
        }
        this.step = step;
    }

    public boolean isAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public boolean isAllowDecimal() {
        return allowDecimal;
    }

    public void setAllowDecimal(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = Math.max(0, scale);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private BigDecimal clamp(BigDecimal candidate) {
        if (candidate.compareTo(min) < 0) {
            return min;
        }
        if (candidate.compareTo(max) > 0) {
            return max;
        }
        return candidate;
    }
}
