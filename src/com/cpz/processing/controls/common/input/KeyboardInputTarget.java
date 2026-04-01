package com.cpz.processing.controls.common.input;

import com.cpz.processing.controls.common.focus.Focusable;

public interface KeyboardInputTarget extends Focusable {

    void onKeyTyped(char character);

    void insertText(String text);

    void backspace();

    void deleteForward();

    void moveCursorLeft();

    void moveCursorRight();

    void moveCursorLeftWithSelection();

    void moveCursorRightWithSelection();

    void selectAll();

    void deleteSelection();

    String getSelectedText();

    void copySelection();

    void cutSelection();

    void pasteFromClipboard();

    default void commit() {
    }

    default void increment(boolean shiftDown, boolean ctrlDown) {
    }

    default void decrement(boolean shiftDown, boolean ctrlDown) {
    }
}
