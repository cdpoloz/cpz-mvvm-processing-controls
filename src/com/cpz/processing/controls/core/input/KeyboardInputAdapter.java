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
    * @param param parameter used by this operation
    * @param param parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleKeyboardEvent(KeyboardEvent event) {
      if (event != null) {
         switch (event.getType()) {
            case PRESS:
               this.handlePressed(event.getKeyCode(), event.isShiftDown(), event.isControlDown(), event.getKeyCode());
               return;
            case TYPE:
               this.onKeyTyped(event.getKey());
               return;
            default:
         }
      }
   }

   private void handlePressed(int value, boolean pressed, boolean pressed2, int value2) {
      if (value == 9) {
         this.suppressTypedOnce = true;
         if (pressed) {
            this.focusManager.focusPrevious();
         } else {
            this.focusManager.focusNext();
         }

      } else {
         KeyboardInputTarget target = this.focusManager.getFocusedKeyboardTarget();
         if (target != null && target.isVisible() && target.isEnabled()) {
            if (pressed2) {
               switch (value2) {
                  case 65:
                     this.suppressTypedOnce = true;
                     target.selectAll();
                     return;
                  case 67:
                     this.suppressTypedOnce = true;
                     target.copySelection();
                     return;
                  case 86:
                     this.suppressTypedOnce = true;
                     target.pasteFromClipboard();
                     return;
                  case 88:
                     this.suppressTypedOnce = true;
                     target.cutSelection();
                     return;
               }
            }

            if (value == 8) {
               target.backspace();
            } else if (value == 127) {
               target.deleteForward();
            } else if (value == 36) {
               this.suppressTypedOnce = true;
               target.moveCursorHome();
            } else if (value == 35) {
               this.suppressTypedOnce = true;
               target.moveCursorEnd();
            } else if (value == 37) {
               if (pressed) {
                  target.moveCursorLeftWithSelection();
               } else {
                  target.moveCursorLeft();
               }

            } else if (value == 39) {
               if (pressed) {
                  target.moveCursorRightWithSelection();
               } else {
                  target.moveCursorRight();
               }

            } else if (value == 38) {
               this.suppressTypedOnce = true;
               target.increment(pressed, pressed2);
            } else if (value == 40) {
               this.suppressTypedOnce = true;
               target.decrement(pressed, pressed2);
            } else {
               if (value == 10 || value == 13) {
                  this.suppressTypedOnce = true;
                  target.commit();
               }

            }
         }
      }
   }
}
