package com.cpz.processing.controls.core.overlay.notification;

import com.cpz.processing.controls.controls.geometry.ControlMeasure;
import java.util.Objects;

/**
 * Immutable custom origin for a notification stack.
 *
 * <p>The horizontal measure resolves against the sketch width and the vertical
 * measure resolves against the sketch height. Relative values use factors, so
 * {@code 0.5f} represents fifty percent of the corresponding sketch axis.</p>
 *
 * @param x horizontal origin measure
 * @param y vertical origin measure
 * @author CPZ
 */
public record NotificationPosition(ControlMeasure x, ControlMeasure y) {
    /**
     * Creates a validated notification position.
     *
     * @param x horizontal origin measure
     * @param y vertical origin measure
     * @throws NullPointerException if either measure is {@code null}
     * @throws IllegalArgumentException if either measure has a non-finite value
     */
    public NotificationPosition {
        Objects.requireNonNull(x, "x");
        Objects.requireNonNull(y, "y");
        validateFinite(x, "x");
        validateFinite(y, "y");
    }

    /**
     * Creates a position with absolute coordinates.
     *
     * @param x absolute horizontal coordinate
     * @param y absolute vertical coordinate
     * @return immutable absolute position
     */
    public static NotificationPosition absolute(float x, float y) {
        return of(ControlMeasure.absolute(x), ControlMeasure.absolute(y));
    }

    /**
     * Creates a position with relative coordinates.
     *
     * @param xFactor horizontal factor resolved against the sketch width
     * @param yFactor vertical factor resolved against the sketch height
     * @return immutable relative position
     */
    public static NotificationPosition relative(float xFactor, float yFactor) {
        return of(ControlMeasure.relative(xFactor), ControlMeasure.relative(yFactor));
    }

    /**
     * Creates a position from independently configured axis measures.
     *
     * @param x horizontal absolute or relative measure
     * @param y vertical absolute or relative measure
     * @return immutable position
     * @throws NullPointerException if either measure is {@code null}
     * @throws IllegalArgumentException if either measure has a non-finite value
     */
    public static NotificationPosition of(ControlMeasure x, ControlMeasure y) {
        return new NotificationPosition(x, y);
    }

    private static void validateFinite(ControlMeasure measure, String axis) {
        if (!Float.isFinite(measure.value())) {
            throw new IllegalArgumentException("Notification position " + axis + " must be finite.");
        }
    }
}
