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
 *
 * @param normalizedValue normalized slider value
 * @param hovered whether the pointer is over the slider
 * @param pressed whether the slider is pressed
 * @param dragging whether the thumb is being dragged
 * @param showText whether value text is displayed
 * @param displayText displayed value text
 * @param enabled whether the slider is enabled
 * @author CPZ
 */
public record SliderViewState(float normalizedValue, boolean hovered, boolean pressed, boolean dragging, boolean showText, String displayText, boolean enabled) {
}
