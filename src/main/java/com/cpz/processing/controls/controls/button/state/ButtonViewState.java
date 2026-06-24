package com.cpz.processing.controls.controls.button.state;

/**
 * Immutable state record for button view state.
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
 * @param x left coordinate
 * @param y top coordinate
 * @param width control width
 * @param height control height
 * @param text displayed text
 * @param showText whether text is displayed
 * @param enabled whether the button is enabled
 * @param hovered whether the pointer is over the button
 * @param pressed whether the button is pressed
 * @author CPZ
 */
public record ButtonViewState(float x, float y, float width, float height, String text, boolean showText, boolean enabled, boolean hovered, boolean pressed) {
}
