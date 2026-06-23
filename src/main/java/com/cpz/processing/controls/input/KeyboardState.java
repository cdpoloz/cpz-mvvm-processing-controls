package com.cpz.processing.controls.input;

import java.util.HashSet;
import java.util.Set;

/**
 * @author CPZ
 */
public final class KeyboardState {
   private static final int SHIFT_KEY_CODE = 16;
   private static final int CTRL_KEY_CODE = 17;
   private static final int ALT_KEY_CODE = 18;
   private final Set<Integer> pressedKeys = new HashSet<>();
   private boolean shiftDown;
   private boolean ctrlDown;
   private boolean altDown;

   public void keyPressed(int keyCode) {
      this.pressedKeys.add(keyCode);
      this.updateModifiers(keyCode, true);
   }

   public void keyReleased(int keyCode) {
      this.pressedKeys.remove(keyCode);
      this.updateModifiers(keyCode, false);
   }

   public boolean isPressed(int value) {
      return this.pressedKeys.contains(value);
   }

   public boolean isShiftDown() {
      return this.shiftDown;
   }

   public boolean isCtrlDown() {
      return this.ctrlDown;
   }

   public boolean isAltDown() {
      return this.altDown;
   }

   private void updateModifiers(int value, boolean enabled) {
      if (value == SHIFT_KEY_CODE) {
         this.shiftDown = enabled;
      } else if (value == CTRL_KEY_CODE) {
         this.ctrlDown = enabled;
      } else if (value == ALT_KEY_CODE) {
         this.altDown = enabled;
      }
   }
}
