package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.focus.FocusManager;

/**
 * Input component for keyboard input adapter.
 *
 * Responsibilities:
 * - Translate public input flow into control operations.
 * - Keep raw event handling outside business state.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class KeyboardInputAdapter {
   private static final char DELETE_KEY = 127;
   private static final int KEY_CODE_BACKSPACE = 8;
   private static final int KEY_CODE_TAB = 9;
   private static final int KEY_CODE_ENTER = 10;
   private static final int KEY_CODE_RETURN = 13;
   private static final int KEY_CODE_DELETE = 127;
   private static final int KEY_CODE_HOME = 36;
   private static final int KEY_CODE_END = 35;
   private static final int KEY_CODE_LEFT = 37;
   private static final int KEY_CODE_RIGHT = 39;
   private static final int KEY_CODE_UP = 38;
   private static final int KEY_CODE_DOWN = 40;
   private static final int KEY_CODE_A = 65;
   private static final int KEY_CODE_C = 67;
   private static final int KEY_CODE_V = 86;
   private static final int KEY_CODE_X = 88;
   private final FocusManager focusManager;
   private boolean suppressTypedOnce;

   /**
    * Creates a keyboard input adapter.
    *
    * @param focusManager parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public KeyboardInputAdapter(FocusManager focusManager) {
      this.focusManager = focusManager;
   }

   /**
    * Handles key typed.
    *
    * @param key parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onKeyTyped(char key) {
      if (this.suppressTypedOnce) {
         this.suppressTypedOnce = false;
      } else {
         KeyboardInputTarget target = this.focusManager.getFocusedKeyboardTarget();
         if (target != null && target.isVisible() && target.isEnabled()) {
            target.onKeyTyped(key);
         }
      }
   }

   /**
    * Handles key pressed.
    *
    * @param event parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleKeyboardEvent(KeyboardEvent event) {
      if (event != null) {
         switch (event.getType()) {
            case PRESS:
               this.handlePressed(event);
               return;
            case TYPE:
               this.onKeyTyped(event.getKey());
               return;
            default:
         }
      }
   }

   private void handlePressed(KeyboardEvent event) {
      int keyCode = event.getKeyCode();
      boolean shiftDown = event.isShiftDown();
      boolean controlDown = event.isControlDown();
      if (keyCode == KEY_CODE_TAB) {
         this.suppressTypedOnce = true;
         if (shiftDown) {
            this.focusManager.focusPrevious();
         } else {
            this.focusManager.focusNext();
         }

      } else {
         KeyboardInputTarget target = this.focusManager.getFocusedKeyboardTarget();
         if (target != null && target.isVisible() && target.isEnabled()) {
            if (controlDown) {
               switch (keyCode) {
                  case KEY_CODE_A:
                     this.suppressTypedOnce = true;
                     target.selectAll();
                     return;
                  case KEY_CODE_C:
                     this.suppressTypedOnce = true;
                     target.copySelection();
                     return;
                  case KEY_CODE_V:
                     this.suppressTypedOnce = true;
                     target.pasteFromClipboard();
                     return;
                  case KEY_CODE_X:
                     this.suppressTypedOnce = true;
                     target.cutSelection();
                     return;
               }
            }

            if (keyCode == KEY_CODE_BACKSPACE) {
               target.backspace();
            } else if (this.isDeleteForward(event)) {
               target.deleteForward();
            } else if (keyCode == KEY_CODE_HOME) {
               this.suppressTypedOnce = true;
               if (shiftDown) {
                  target.moveCursorHomeWithSelection();
               } else {
                  target.moveCursorHome();
               }
            } else if (keyCode == KEY_CODE_END) {
               this.suppressTypedOnce = true;
               if (shiftDown) {
                  target.moveCursorEndWithSelection();
               } else {
                  target.moveCursorEnd();
               }
            } else if (keyCode == KEY_CODE_LEFT) {
               if (shiftDown) {
                  target.moveCursorLeftWithSelection();
               } else {
                  target.moveCursorLeft();
               }

            } else if (keyCode == KEY_CODE_RIGHT) {
               if (shiftDown) {
                  target.moveCursorRightWithSelection();
               } else {
                  target.moveCursorRight();
               }

            } else if (keyCode == KEY_CODE_UP) {
               this.suppressTypedOnce = true;
               target.increment(shiftDown, controlDown);
            } else if (keyCode == KEY_CODE_DOWN) {
               this.suppressTypedOnce = true;
               target.decrement(shiftDown, controlDown);
            } else {
               if (keyCode == KEY_CODE_ENTER || keyCode == KEY_CODE_RETURN) {
                  this.suppressTypedOnce = true;
                  target.commit();
               }

            }
         }
      }
   }

   private boolean isDeleteForward(KeyboardEvent event) {
      return event.getKeyCode() == KEY_CODE_DELETE || event.getKey() == DELETE_KEY;
   }
}
