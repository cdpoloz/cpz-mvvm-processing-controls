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
 */
public record TooltipViewState(float x, float y, float width, float height, String text, boolean enabled, float textPadding, float cornerRadius) {
}
