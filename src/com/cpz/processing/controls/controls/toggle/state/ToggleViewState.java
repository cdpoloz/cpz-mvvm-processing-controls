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
 */
public record ToggleViewState(float x, float y, float width, float height, int stateIndex, int totalStates, boolean hovered, boolean pressed, boolean enabled) {
}
