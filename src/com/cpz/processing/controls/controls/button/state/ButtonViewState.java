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
 */
public record ButtonViewState(float x, float y, float width, float height, String text, boolean showText, boolean enabled, boolean hovered, boolean pressed) {
}
