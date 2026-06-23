package com.cpz.processing.controls.controls.textfield.state;

/**
 * Immutable state record for text field view state.
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
public record TextFieldViewState(float x, float y, float width, float height, String text, String textBeforeSelection, String selectedText, String textAfterSelection, int cursorIndex, int selectionStart, int selectionEnd, boolean focused, boolean showCursor, boolean enabled, float textX, float cursorX, float selectionStartX, float selectionEndX) {
}
