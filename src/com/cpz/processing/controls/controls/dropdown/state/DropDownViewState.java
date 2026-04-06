package com.cpz.processing.controls.controls.dropdown.state;

import java.util.List;

/**
 * Immutable state record for drop down view state.
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
public record DropDownViewState(float x, float y, float width, float height, boolean expanded, String selectedText, List items, int selectedIndex, int hoveredIndex, boolean hovered, boolean pressed, boolean focused, boolean enabled, float itemHeight, int maxVisibleItems, float textPadding, float arrowPadding, float cornerRadius, float listCornerRadius, float strokeWeight) {
}
