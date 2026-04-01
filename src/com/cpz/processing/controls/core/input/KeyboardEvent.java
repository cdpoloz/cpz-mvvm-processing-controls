package com.cpz.processing.controls.core.input;

public final class KeyboardEvent {
   private final Type type;
   private final char key;
   private final int keyCode;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;

   public KeyboardEvent(Type var1, char var2, int var3, boolean var4, boolean var5, boolean var6) {
      this.type = var1;
      this.key = var2;
      this.keyCode = var3;
      this.shiftDown = var4;
      this.controlDown = var5;
      this.altDown = var6;
   }

   public Type getType() {
      return this.type;
   }

   public char getKey() {
      return this.key;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public boolean isShiftDown() {
      return this.shiftDown;
   }

   public boolean isControlDown() {
      return this.controlDown;
   }

   public boolean isAltDown() {
      return this.altDown;
   }

   public static enum Type {
      PRESS,
      TYPE,
      RELEASE;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{PRESS, TYPE, RELEASE};
      }
   }
}
