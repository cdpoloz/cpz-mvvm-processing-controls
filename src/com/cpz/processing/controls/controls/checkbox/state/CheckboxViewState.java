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
 * @author CPZ
 */
public record CheckboxViewState(float x, float y, float width, float height, boolean checked, boolean hovered, boolean pressed, boolean enabled) {
}
