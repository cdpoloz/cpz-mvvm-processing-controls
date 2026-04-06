package com.cpz.processing.controls.controls.slider.state;

/**
 * Immutable state record for slider view state.
 *
 * Responsibilities:
 * - Carry immutable public data.
 * - Keep frame or configuration payloads explicit.
 *
 * Behavior:
 * - Stores data only and does not contain workflow logic.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public record SliderViewState(float normalizedValue, boolean hovered, boolean pressed, boolean dragging, boolean showText, String displayText, boolean enabled) {
}
