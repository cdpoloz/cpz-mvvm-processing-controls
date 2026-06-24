package com.cpz.processing.controls.core.overlay.tooltip.state;

/**
 * Immutable state record for tooltip view state.
 *
 * Responsibilities:
 * - Coordinate overlay-specific state or drawing flow.
 * - Keep overlay behavior isolated from base controls.
 *
 * Behavior:
 * - Stores data only and does not contain workflow logic.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @param x left coordinate
 * @param y top coordinate
 * @param width tooltip width
 * @param height tooltip height
 * @param text displayed text
 * @param enabled whether the tooltip is enabled
 * @param textPadding text padding
 * @param cornerRadius corner radius
 * @author CPZ
 */
public record TooltipViewState(float x, float y, float width, float height, String text, boolean enabled, float textPadding, float cornerRadius) {
}
