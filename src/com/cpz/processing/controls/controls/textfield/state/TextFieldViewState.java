package com.cpz.processing.controls.controls.textfield.state;

public record TextFieldViewState(float x, float y, float width, float height, String text, String textBeforeSelection, String selectedText, String textAfterSelection, int cursorIndex, int selectionStart, int selectionEnd, boolean focused, boolean showCursor, boolean enabled, float textX, float cursorX, float selectionStartX, float selectionEndX) {
}
