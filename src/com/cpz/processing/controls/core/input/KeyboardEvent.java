package com.cpz.processing.controls.core.input;

/**
 * Input component for keyboard event.
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
public final class KeyboardEvent {
   private final Type type;
   private final char key;
   private final int keyCode;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;

   /**
    * Creates a keyboard event.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public KeyboardEvent(Type var1, char var2, int var3, boolean var4, boolean var5, boolean var6) {
      this.type = var1;
      this.key = var2;
      this.keyCode = var3;
      this.shiftDown = var4;
      this.controlDown = var5;
      this.altDown = var6;
   }

   /**
    * Returns type.
    *
    * @return current type
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public Type getType() {
      return this.type;
   }

   /**
    * Returns key.
    *
    * @return current key
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public char getKey() {
      return this.key;
   }

   /**
    * Returns key code.
    *
    * @return current key code
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getKeyCode() {
      return this.keyCode;
   }

   /**
    * Returns whether shift down.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isShiftDown() {
      return this.shiftDown;
   }

   /**
    * Returns whether control down.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isControlDown() {
      return this.controlDown;
   }

   /**
    * Returns whether alt down.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isAltDown() {
      return this.altDown;
   }

   /**
    * Enumeration of type values.
    *
    * Responsibilities:
    * - Translate public input flow into control operations.
    * - Keep raw event handling outside business state.
    *
    * Behavior:
    * - Provides symbolic values only.
    *
    * Notes:
    * - This type is part of the public project surface.
    */
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
