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
 *
 * @param x left coordinate
 * @param y top coordinate
 * @param width control width
 * @param height control height
 * @param expanded whether the list is expanded
 * @param selectedText selected item text
 * @param items item view states
 * @param selectedIndex selected item index
 * @param hoveredIndex hovered item index
 * @param hovered whether the pointer is over the control
 * @param pressed whether the control is pressed
 * @param focused whether the control is focused
 * @param enabled whether the control is enabled
 * @param itemHeight item height
 * @param maxVisibleItems maximum number of visible items
 * @param textPadding text padding
 * @param arrowPadding arrow padding
 * @param cornerRadius control corner radius
 * @param listCornerRadius list corner radius
 * @param strokeWeight stroke width
 * @author CPZ
 */
public record DropDownViewState(float x, float y, float width, float height, boolean expanded, String selectedText, List items, int selectedIndex, int hoveredIndex, boolean hovered, boolean pressed, boolean focused, boolean enabled, float itemHeight, int maxVisibleItems, float textPadding, float arrowPadding, float cornerRadius, float listCornerRadius, float strokeWeight) {
}
