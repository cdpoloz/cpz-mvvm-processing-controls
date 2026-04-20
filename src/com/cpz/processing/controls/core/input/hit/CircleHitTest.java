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
 *
 * @author CPZ
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
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public CircleHitTest(float x, float y, float width) {
      this.set(x, y, width);
   }

   /**
    * Handles layout.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    *
    * Behavior:
    * - Applies the public interaction flow exposed by this type.
    */
   public void onLayout(float x, float y, float width, float height) {
      float value = Math.min(width, height);
      this.set(x, y, value * 0.5F);
   }

   /**
    * Performs set.
    *
    * @param x new state
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    *
    * Behavior:
    * - Updates the public state or registration owned by this type.
    */
   public void set(float x, float y, float width) {
      this.cx = x;
      this.cy = y;
      this.r = width;
   }

   /**
    * Performs contains.
    *
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public boolean contains(float x, float y) {
      float dx = x - this.cx;
      float dy = y - this.cy;
      return dx * dx + dy * dy <= this.r * this.r;
   }
}
