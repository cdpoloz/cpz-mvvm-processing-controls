package com.cpz.processing.controls.controls.radiogroup.state;

/**
 * Immutable state record for radio group item view state.
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
public record RadioGroupItemViewState(int index, String text, boolean selected, boolean hovered, boolean pressed, boolean focused, float x, float y, float width, float height, float indicatorCenterX, float indicatorCenterY, float textX) {
}
