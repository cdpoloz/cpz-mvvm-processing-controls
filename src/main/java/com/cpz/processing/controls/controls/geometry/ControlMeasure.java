package com.cpz.processing.controls.controls.geometry;

import java.util.Objects;

/**
 * Explicit measurement value used by relative geometry APIs.
 *
 * @author CPZ
 */
public final class ControlMeasure {
    private final MeasureMode mode;
    private final float value;

    private ControlMeasure(MeasureMode mode, float value) {
        this.mode = Objects.requireNonNull(mode, "mode");
        this.value = value;
    }

    public static ControlMeasure absolute(float value) {
        return new ControlMeasure(MeasureMode.ABSOLUTE, value);
    }

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
