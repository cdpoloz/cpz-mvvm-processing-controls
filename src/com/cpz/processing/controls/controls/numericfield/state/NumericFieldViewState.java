package com.cpz.processing.controls.controls.numericfield.state;

/**
 * Immutable state record for numeric field view state.
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
public record NumericFieldViewState(float x, float y, float width, float height, String text, String textBeforeSelection, String selectedText, String textAfterSelection, int cursorPosition, int selectionStart, int selectionEnd, boolean focused, boolean hovered, boolean pressed, boolean editing, boolean showCursor, boolean enabled, float textX, float cursorX, float selectionStartX, float selectionEndX) {
}
