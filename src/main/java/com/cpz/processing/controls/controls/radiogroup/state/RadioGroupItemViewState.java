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
 * @param index option index
 * @param text option text
 * @param selected whether the option is selected
 * @param hovered whether the pointer is over the option
 * @param pressed whether the option is pressed
 * @param focused whether the option is focused
 * @param x left coordinate
 * @param y top coordinate
 * @param width option width
 * @param height option height
 * @param indicatorCenterX indicator center x-coordinate
 * @param indicatorCenterY indicator center y-coordinate
 * @param textX horizontal text coordinate
 * @author CPZ
 */
public record RadioGroupItemViewState(int index, String text, boolean selected, boolean hovered, boolean pressed, boolean focused, float x, float y, float width, float height, float indicatorCenterX, float indicatorCenterY, float textX) {
}
