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
 *
 * @author CPZ
 */
public interface KeyboardInputTarget extends Focusable {
   /**
    * Handles a typed character.
    *
    * @param key typed character
    */
   void onKeyTyped(char key);

   /**
    * Inserts text at the current cursor or selection.
    *
    * @param text text to insert
    */
   void insertText(String text);

   void backspace();

   void deleteForward();

   void moveCursorLeft();

   void moveCursorRight();

   /**
    * Moves the cursor to the start of the current value when supported.
    */
   default void moveCursorHome() {
   }

   /**
    * Moves the cursor to the end of the current value when supported.
    */
   default void moveCursorEnd() {
   }

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
    * @param coarseStep coarse-step modifier
    * @param fineStep fine-step modifier
    */
   default void increment(boolean coarseStep, boolean fineStep) {
   }

   /**
    * Decrements the current value when supported.
    *
    * @param coarseStep coarse-step modifier
    * @param fineStep fine-step modifier
    */
   default void decrement(boolean coarseStep, boolean fineStep) {
   }
}
