package com.cpz.processing.controls.controls.geometry;

import java.util.Objects;

/**
 * Explicit finite measurement value used by relative geometry APIs.
 *
 * @author CPZ
 */
public final class ControlMeasure {
    private final MeasureMode mode;
    private final float value;

    private ControlMeasure(MeasureMode mode, float value) {
        this.mode = Objects.requireNonNull(mode, "mode");
        if (!Float.isFinite(value)) {
            throw new IllegalArgumentException("Control measure value must be finite: " + value);
        }
        this.value = value;
    }

    /**
     * Creates a finite absolute measurement.
     *
     * @param value absolute value
     * @return the measurement
     * @throws IllegalArgumentException when {@code value} is not finite
     */
    public static ControlMeasure absolute(float value) {
        return new ControlMeasure(MeasureMode.ABSOLUTE, value);
    }

    /**
     * Creates a finite relative measurement.
     *
     * @param factor relative factor
     * @return the measurement
     * @throws IllegalArgumentException when {@code factor} is not finite
     */
    public static ControlMeasure relative(float factor) {
        return new ControlMeasure(MeasureMode.RELATIVE, factor);
    }

    public float resolve(float relativeBase) {
        return this.mode == MeasureMode.RELATIVE ? relativeBase * this.value : this.value;
    }

    public MeasureMode mode() {
        return this.mode;
    }

    public float value() {
        return this.value;
    }
}
