package com.cpz.processing.controls.core.input.hit;

import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;

/**
 * Input component for rect hit test.
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
public class RectHitTest implements HitTest {
   private float x;
   private float y;
   private float w;
   private float h;

   /**
    * Creates a rect hit test.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RectHitTest() {
   }

   /**
    * Creates a rect hit test.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RectHitTest(float var1, float var2, float var3, float var4) {
      this.w = var3;
      this.h = var4;
      this.x = var1 - var3 * 0.5F;
      this.y = var2 - var4 * 0.5F;
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
      this.w = var3;
      this.h = var4;
      this.x = var1 - var3 * 0.5F;
      this.y = var2 - var4 * 0.5F;
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
      return var1 >= this.x && var1 <= this.x + this.w && var2 >= this.y && var2 <= this.y + this.h;
   }
}
