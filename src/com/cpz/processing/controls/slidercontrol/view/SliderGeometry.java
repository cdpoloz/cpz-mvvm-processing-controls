package com.cpz.processing.controls.slidercontrol.view;

import com.cpz.processing.controls.slidercontrol.SliderOrientation;

public final class SliderGeometry {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final SliderOrientation orientation;
    private final float trackStart;
    private final float trackEnd;
    private final float trackLength;

    public SliderGeometry(float x,
                          float y,
                          float width,
                          float height,
                          SliderOrientation orientation,
                          float trackStart,
                          float trackEnd) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orientation = orientation == null ? SliderOrientation.HORIZONTAL : orientation;
        float resolvedStart = Math.min(trackStart, trackEnd);
        float resolvedEnd = Math.max(trackStart, trackEnd);
        this.trackStart = resolvedStart;
        this.trackEnd = resolvedEnd;
        this.trackLength = Math.max(0f, resolvedEnd - resolvedStart);
    }

    public float normalizedToPosition(float normalized) {
        float clampedNormalized = clamp(normalized, 0f, 1f);
        return trackStart + (trackLength * clampedNormalized);
    }

    public float positionToNormalized(float position) {
        if (trackLength <= 0f) {
            return 0f;
        }
        float clampedPosition = clamp(position, trackStart, trackEnd);
        return (clampedPosition - trackStart) / trackLength;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public SliderOrientation orientation() {
        return orientation;
    }

    public float trackStart() {
        return trackStart;
    }

    public float trackEnd() {
        return trackEnd;
    }

    public float trackLength() {
        return trackLength;
    }

    public float trackStartX() {
        return orientation == SliderOrientation.HORIZONTAL ? trackStart : x;
    }

    public float trackStartY() {
        return orientation == SliderOrientation.VERTICAL ? y + (height * 0.5f) : y;
    }

    public float trackEndX() {
        return orientation == SliderOrientation.HORIZONTAL ? trackEnd : x;
    }

    public float trackEndY() {
        return orientation == SliderOrientation.VERTICAL ? y - (height * 0.5f) : y;
    }

    public float thumbX(float normalizedValue) {
        return orientation == SliderOrientation.HORIZONTAL ? normalizedToPosition(normalizedValue) : x;
    }

    public float thumbY(float normalizedValue) {
        return orientation == SliderOrientation.VERTICAL ? y + (height * 0.5f) - normalizedToPosition(normalizedValue) : y;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
