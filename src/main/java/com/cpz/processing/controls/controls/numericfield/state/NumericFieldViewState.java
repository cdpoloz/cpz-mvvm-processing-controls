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
 *
 * @param x left coordinate
 * @param y top coordinate
 * @param width control width
 * @param height control height
 * @param text complete text
 * @param textBeforeSelection text before the selected range
 * @param selectedText selected text
 * @param textAfterSelection text after the selected range
 * @param cursorPosition cursor position
 * @param selectionStart selection start index
 * @param selectionEnd selection end index
 * @param focused whether the field is focused
 * @param hovered whether the pointer is over the field
 * @param pressed whether the field is pressed
 * @param editing whether the value is being edited
 * @param showCursor whether the cursor is displayed
 * @param enabled whether the field is enabled
 * @param textX horizontal text coordinate
 * @param cursorX horizontal cursor coordinate
 * @param selectionStartX horizontal selection start coordinate
 * @param selectionEndX horizontal selection end coordinate
 * @author CPZ
 */
public record NumericFieldViewState(float x, float y, float width, float height, String text, String textBeforeSelection, String selectedText, String textAfterSelection, int cursorPosition, int selectionStart, int selectionEnd, boolean focused, boolean hovered, boolean pressed, boolean editing, boolean showCursor, boolean enabled, float textX, float cursorX, float selectionStartX, float selectionEndX) {
}
