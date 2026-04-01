package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.focus.Focusable;

public interface KeyboardInputTarget extends Focusable {
   void onKeyTyped(char var1);

   void insertText(String var1);

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

   default void increment(boolean var1, boolean var2) {
   }

   default void decrement(boolean var1, boolean var2) {
   }
}
