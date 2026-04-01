package com.cpz.processing.controls.core.input;

public class DefaultInputLayer implements InputLayer {
   private final int priority;
   private boolean active = true;

   public DefaultInputLayer(int var1) {
      this.priority = var1;
   }

   public int getPriority() {
      return this.priority;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean isPointerCaptureEnabled() {
      return true;
   }

   public boolean isKeyboardCaptureEnabled() {
      return true;
   }

   public boolean handlePointerEvent(PointerEvent var1) {
      return false;
   }

   public boolean handleKeyboardEvent(KeyboardEvent var1) {
      return false;
   }

   public void setActive(boolean var1) {
      this.active = var1;
   }
}
