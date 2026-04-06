package com.cpz.processing.controls.core.input;

import com.cpz.processing.controls.core.focus.FocusManager;
import processing.event.KeyEvent;

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
 */
public final class KeyboardInputAdapter {
   private final FocusManager focusManager;
   private boolean suppressTypedOnce;

   /**
    * Creates a keyboard input adapter.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public KeyboardInputAdapter(FocusManager var1) {
      this.focusManager = var1;
   }

   /**
    * Handles key typed.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onKeyTyped(char var1) {
      if (this.suppressTypedOnce) {
         this.suppressTypedOnce = false;
      } else {
         KeyboardInputTarget var2 = this.focusManager.getFocusedKeyboardTarget();
         if (var2 != null && var2.isVisible() && var2.isEnabled()) {
            var2.onKeyTyped(var1);
         }
      }
   }

   /**
    * Handles key pressed.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onKeyPressed(char var1, int var2, KeyEvent var3) {
      boolean var4 = var3 != null && var3.isShiftDown();
      boolean var5 = var3 != null && var3.isControlDown();
      int var6 = var3 != null ? var3.getKeyCode() : var2;
      this.handlePressed(var2, var4, var5, var6);
   }

   /**
    * Handles keyboard event.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void handleKeyboardEvent(KeyboardEvent var1) {
      if (var1 != null) {
         switch (var1.getType()) {
            case PRESS:
               this.handlePressed(var1.getKeyCode(), var1.isShiftDown(), var1.isControlDown(), var1.getKeyCode());
               return;
            case TYPE:
               this.onKeyTyped(var1.getKey());
               return;
            default:
         }
      }
   }

   private void handlePressed(int var1, boolean var2, boolean var3, int var4) {
      if (var1 == 9) {
         this.suppressTypedOnce = true;
         if (var2) {
            this.focusManager.focusPrevious();
         } else {
            this.focusManager.focusNext();
         }

      } else {
         KeyboardInputTarget var5 = this.focusManager.getFocusedKeyboardTarget();
         if (var5 != null && var5.isVisible() && var5.isEnabled()) {
            if (var3) {
               switch (var4) {
                  case 65:
                     this.suppressTypedOnce = true;
                     var5.selectAll();
                     return;
                  case 67:
                     this.suppressTypedOnce = true;
                     var5.copySelection();
                     return;
                  case 86:
                     this.suppressTypedOnce = true;
                     var5.pasteFromClipboard();
                     return;
                  case 88:
                     this.suppressTypedOnce = true;
                     var5.cutSelection();
                     return;
               }
            }

            if (var1 == 8) {
               var5.backspace();
            } else if (var1 == 127) {
               var5.deleteForward();
            } else if (var1 == 37) {
               if (var2) {
                  var5.moveCursorLeftWithSelection();
               } else {
                  var5.moveCursorLeft();
               }

            } else if (var1 == 39) {
               if (var2) {
                  var5.moveCursorRightWithSelection();
               } else {
                  var5.moveCursorRight();
               }

            } else if (var1 == 38) {
               this.suppressTypedOnce = true;
               var5.increment(var2, var3);
            } else if (var1 == 40) {
               this.suppressTypedOnce = true;
               var5.decrement(var2, var3);
            } else {
               if (var1 == 10 || var1 == 13) {
                  this.suppressTypedOnce = true;
                  var5.commit();
               }

            }
         }
      }
   }
}
