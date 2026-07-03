package com.cpz.processing.controls.controls.geometry;

import java.util.Objects;

/**
 * Explicit geometry specification for public controls.
 *
 * <p>Relative positions use parent width for x and parent height for y.
 * Relative dimensions use parent height for both width and height.</p>
 *
 * @author CPZ
 */
public final class ControlBounds {
    private final ControlMeasure x;
    private final ControlMeasure y;
    private final ControlMeasure width;
    private final ControlMeasure height;

    private ControlBounds(ControlMeasure x, ControlMeasure y, ControlMeasure width, ControlMeasure height) {
        this.x = Objects.requireNonNull(x, "x");
        this.y = Objects.requireNonNull(y, "y");
        this.width = Objects.requireNonNull(width, "width");
        this.height = Objects.requireNonNull(height, "height");
    }

    public static ControlBounds absolute(float x, float y, float width, float height) {
        return of(
                ControlMeasure.absolute(x),
                ControlMeasure.absolute(y),
                ControlMeasure.absolute(width),
                ControlMeasure.absolute(height)
        );
    }

    public static ControlBounds relative(float xFactor, float yFactor, float widthFactor, float heightFactor) {
        return of(
                ControlMeasure.relative(xFactor),
                ControlMeasure.relative(yFactor),
                ControlMeasure.relative(widthFactor),
                ControlMeasure.relative(heightFactor)
        );
    }

    public static ControlBounds of(
            ControlMeasure x,
            ControlMeasure y,
            ControlMeasure width,
            ControlMeasure height
    ) {
        return new ControlBounds(x, y, width, height);
    }

    public ResolvedBounds resolve(float parentWidth, float parentHeight) {
        return new ResolvedBounds(
                this.x.resolve(parentWidth),
                this.y.resolve(parentHeight),
                this.width.resolve(parentHeight),
                this.height.resolve(parentHeight)
        );
    }

    public ControlBounds withPosition(ControlMeasure x, ControlMeasure y) {
        return of(x, y, this.width, this.height);
    }

    public ControlBounds withSize(ControlMeasure width, ControlMeasure height) {
        return of(this.x, this.y, width, height);
    }

    public ControlMeasure x() {
        return this.x;
    }

    public ControlMeasure y() {
        return this.y;
    }

    public ControlMeasure width() {
        return this.width;
    }

    public ControlMeasure height() {
        return this.height;
    }
}
