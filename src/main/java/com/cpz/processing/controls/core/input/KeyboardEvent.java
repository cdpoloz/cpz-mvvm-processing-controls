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
 *
 * @author CPZ
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
    * @param type parameter used by this operation
    * @param key parameter used by this operation
    * @param keyCode parameter used by this operation
    * @param shiftDown whether Shift is down
    * @param controlDown whether Control is down
    * @param altDown whether Alt is down
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public KeyboardEvent(Type type, char key, int keyCode, boolean shiftDown, boolean controlDown, boolean altDown) {
      this.type = type;
      this.key = key;
      this.keyCode = keyCode;
      this.shiftDown = shiftDown;
      this.controlDown = controlDown;
      this.altDown = altDown;
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
