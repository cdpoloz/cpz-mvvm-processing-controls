package com.cpz.processing.controls.input;

import java.util.HashSet;
import java.util.Set;

public final class KeyboardState {
   private static final int SHIFT_KEY_CODE = 16;
   private static final int CTRL_KEY_CODE = 17;
   private static final int ALT_KEY_CODE = 18;
   private final Set<Integer> pressedKeys = new HashSet<>();
   private boolean shiftDown;
   private boolean ctrlDown;
   private boolean altDown;

   public void keyPressed(int var1) {
      this.pressedKeys.add(var1);
      this.updateModifiers(var1, true);
   }

   public void keyReleased(int var1) {
      this.pressedKeys.remove(var1);
      this.updateModifiers(var1, false);
   }

   public boolean isPressed(int var1) {
      return this.pressedKeys.contains(var1);
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

   private void updateModifiers(int var1, boolean var2) {
      if (var1 == SHIFT_KEY_CODE) {
         this.shiftDown = var2;
      } else if (var1 == CTRL_KEY_CODE) {
         this.ctrlDown = var2;
      } else if (var1 == ALT_KEY_CODE) {
         this.altDown = var2;
      }
   }
}
