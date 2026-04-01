package com.cpz.processing.controls.core.input;

public final class PointerEvent {
   private final Type type;
   private final float x;
   private final float y;
   private final int button;
   private final float wheelDelta;
   private final boolean shiftDown;
   private final boolean controlDown;

   public PointerEvent(Type var1, float var2, float var3) {
      this(var1, var2, var3, 0, 0.0F, false, false);
   }

   public PointerEvent(Type var1, float var2, float var3, int var4) {
      this(var1, var2, var3, var4, 0.0F, false, false);
   }

   public PointerEvent(Type var1, float var2, float var3, int var4, float var5, boolean var6, boolean var7) {
      this.type = var1;
      this.x = var2;
      this.y = var3;
      this.button = var4;
      this.wheelDelta = var5;
      this.shiftDown = var6;
      this.controlDown = var7;
   }

   public Type getType() {
      return this.type;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public int getButton() {
      return this.button;
   }

   public float getWheelDelta() {
      return this.wheelDelta;
   }

   public boolean isShiftDown() {
      return this.shiftDown;
   }

   public boolean isControlDown() {
      return this.controlDown;
   }

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
