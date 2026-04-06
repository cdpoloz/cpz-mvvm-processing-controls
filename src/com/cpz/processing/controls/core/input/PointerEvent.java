package com.cpz.processing.controls.core.input;

/**
 * Input component for pointer event.
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
public final class PointerEvent {
   private final Type type;
   private final float x;
   private final float y;
   private final int button;
   private final float wheelDelta;
   private final boolean shiftDown;
   private final boolean controlDown;

   /**
    * Creates a pointer event.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type var1, float var2, float var3) {
      this(var1, var2, var3, 0, 0.0F, false, false);
   }

   /**
    * Creates a pointer event.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type var1, float var2, float var3, int var4) {
      this(var1, var2, var3, var4, 0.0F, false, false);
   }

   /**
    * Creates a pointer event.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    * @param var7 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type var1, float var2, float var3, int var4, float var5, boolean var6, boolean var7) {
      this.type = var1;
      this.x = var2;
      this.y = var3;
      this.button = var4;
      this.wheelDelta = var5;
      this.shiftDown = var6;
      this.controlDown = var7;
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
    * Returns x.
    *
    * @return current x
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getX() {
      return this.x;
   }

   /**
    * Returns y.
    *
    * @return current y
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getY() {
      return this.y;
   }

   /**
    * Returns button.
    *
    * @return current button
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public int getButton() {
      return this.button;
   }

   /**
    * Returns wheel delta.
    *
    * @return current wheel delta
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public float getWheelDelta() {
      return this.wheelDelta;
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
      MOVE,
      DRAG,
      WHEEL,
      PRESS,
      RELEASE;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{MOVE, DRAG, WHEEL, PRESS, RELEASE};
      }
   }
}
