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
 *
 * @author CPZ
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
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RectHitTest(float x, float y, float width, float height) {
      this.w = width;
      this.h = height;
      this.x = x - width * 0.5F;
      this.y = y - height * 0.5F;
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
      this.w = width;
      this.h = height;
      this.x = x - width * 0.5F;
      this.y = y - height * 0.5F;
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
      return x >= this.x && x <= this.x + this.w && y >= this.y && y <= this.y + this.h;
   }
}
