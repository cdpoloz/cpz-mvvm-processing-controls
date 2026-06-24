package com.cpz.processing.controls.controls.checkbox.state;

/**
 * Immutable state record for checkbox view state.
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
 * @param checked whether the checkbox is checked
 * @param hovered whether the pointer is over the checkbox
 * @param pressed whether the checkbox is pressed
 * @param enabled whether the checkbox is enabled
 * @author CPZ
 */
public record CheckboxViewState(float x, float y, float width, float height, boolean checked, boolean hovered, boolean pressed, boolean enabled) {
}
