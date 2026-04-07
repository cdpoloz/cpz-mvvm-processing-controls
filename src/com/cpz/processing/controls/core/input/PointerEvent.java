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
   private final boolean pressed;
   private final int button;
   private final float wheelDelta;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;

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
      this(var1, var2, var3, defaultPressed(var1), 0, 0.0F, false, false, false);
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
      this(var1, var2, var3, defaultPressed(var1), var4, 0.0F, false, false, false);
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
      this(var1, var2, var3, defaultPressed(var1), var4, var5, var6, var7, false);
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
    * @param var8 parameter used by this operation
    * @param var9 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type var1, float var2, float var3, boolean var4, int var5, float var6, boolean var7, boolean var8, boolean var9) {
      this.type = var1;
      this.x = var2;
      this.y = var3;
      this.pressed = var4;
      this.button = var5;
      this.wheelDelta = var6;
      this.shiftDown = var7;
      this.controlDown = var8;
      this.altDown = var9;
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
    * Returns whether pressed.
    *
    * @return whether the current condition is satisfied
    *
    * Behavior:
    * - Returns the current value without applying side effects.
    */
   public boolean isPressed() {
      return this.pressed;
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

   private static boolean defaultPressed(Type var0) {
      return var0 == Type.PRESS || var0 == Type.DRAG;
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
