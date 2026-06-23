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
 *
 * @author CPZ
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
    * @param type parameter used by this operation
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type type, float mouseX, float mouseY) {
      this(type, mouseX, mouseY, defaultPressed(type), 0, 0.0F, false, false, false);
   }

   /**
    * Creates a pointer event.
    *
    * @param type parameter used by this operation
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    * @param button pointer button
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type type, float mouseX, float mouseY, int value) {
      this(type, mouseX, mouseY, defaultPressed(type), value, 0.0F, false, false, false);
   }

   /**
    * Creates a pointer event.
    *
    * @param type parameter used by this operation
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    * @param button pointer button
    * @param wheelDelta wheel delta
    * @param pressed whether the pointer is pressed
    * @param shiftDown whether Shift is down
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type type, float mouseX, float mouseY, int button, float wheelDelta, boolean shiftDown, boolean controlDown) {
      this(type, mouseX, mouseY, defaultPressed(type), button, wheelDelta, shiftDown, controlDown, false);
   }

   /**
    * Creates a pointer event.
    *
    * @param type parameter used by this operation
    * @param mouseX parameter used by this operation
    * @param mouseY parameter used by this operation
    * @param pressed whether the pointer is pressed
    * @param button pointer button
    * @param wheelDelta wheel delta
    * @param shiftDown whether Shift is down
    * @param controlDown whether Control is down
    * @param altDown whether Alt is down
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public PointerEvent(Type type, float mouseX, float mouseY, boolean pressed, int button, float wheelDelta, boolean shiftDown, boolean controlDown, boolean altDown) {
      this.type = type;
      this.x = mouseX;
      this.y = mouseY;
      this.pressed = pressed;
      this.button = button;
      this.wheelDelta = wheelDelta;
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

   private static boolean defaultPressed(Type type) {
      return type == Type.PRESS || type == Type.DRAG;
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
