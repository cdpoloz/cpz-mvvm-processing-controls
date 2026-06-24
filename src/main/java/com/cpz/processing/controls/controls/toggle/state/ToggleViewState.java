package com.cpz.processing.controls.controls.toggle.state;

/**
 * Immutable state record for toggle view state.
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
 * @param stateIndex current state index
 * @param totalStates total number of states
 * @param hovered whether the pointer is over the toggle
 * @param pressed whether the toggle is pressed
 * @param enabled whether the toggle is enabled
 * @author CPZ
 */
public record ToggleViewState(float x, float y, float width, float height, int stateIndex, int totalStates, boolean hovered, boolean pressed, boolean enabled) {
}
