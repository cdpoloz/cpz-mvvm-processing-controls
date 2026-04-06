package com.cpz.processing.controls.core.input.hit;

import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;

/**
 * Input component for circle hit test.
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
public final class CircleHitTest implements HitTest {
   private float cx;
   private float cy;
   private float r;

   /**
    * Creates a circle hit test.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CircleHitTest() {
   }

   /**
    * Creates a circle hit test.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CircleHitTest(float var1, float var2, float var3) {
      this.set(var1, var2, var3);
   }

   /**
    * Handles layout.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onLayout(float var1, float var2, float var3, float var4) {
      float var5 = Math.min(var3, var4);
      this.set(var1, var2, var5 * 0.5F);
   }

   /**
    * Performs set.
    *
    * @param var1 new state
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void set(float var1, float var2, float var3) {
      this.cx = var1;
      this.cy = var2;
      this.r = var3;
   }

   /**
    * Performs contains.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public boolean contains(float var1, float var2) {
      float var3 = var1 - this.cx;
      float var4 = var2 - this.cy;
      return var3 * var3 + var4 * var4 <= this.r * this.r;
   }
}
