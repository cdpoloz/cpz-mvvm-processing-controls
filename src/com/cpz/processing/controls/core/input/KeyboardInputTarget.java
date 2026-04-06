package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.focus.Focusable;

/**
 * Input component for keyboard input target.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Declares the contract without prescribing implementation details.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public interface KeyboardInputTarget extends Focusable {
   /**
    * Handles a typed character.
    *
    * @param var1 typed character
    */
   void onKeyTyped(char var1);

   /**
    * Inserts text at the current cursor or selection.
    *
    * @param var1 text to insert
    */
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

   /**
    * Commits the current editing buffer when supported.
    */
   default void commit() {
   }

   /**
    * Increments the current value when supported.
    *
    * @param var1 coarse-step modifier
    * @param var2 fine-step modifier
    */
   default void increment(boolean var1, boolean var2) {
   }

   /**
    * Decrements the current value when supported.
    *
    * @param var1 coarse-step modifier
    * @param var2 fine-step modifier
    */
   default void decrement(boolean var1, boolean var2) {
   }
}
