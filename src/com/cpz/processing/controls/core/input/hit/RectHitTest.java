package com.cpz.processing.controls.core.input.hit;

import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;

public class RectHitTest implements HitTest {
   private float x;
   private float y;
   private float w;
   private float h;

   public RectHitTest() {
   }

   public RectHitTest(float var1, float var2, float var3, float var4) {
      this.w = var3;
      this.h = var4;
      this.x = var1 - var3 * 0.5F;
      this.y = var2 - var4 * 0.5F;
   }

   public void onLayout(float var1, float var2, float var3, float var4) {
      this.w = var3;
      this.h = var4;
      this.x = var1 - var3 * 0.5F;
      this.y = var2 - var4 * 0.5F;
   }

   public boolean contains(float var1, float var2) {
      return var1 >= this.x && var1 <= this.x + this.w && var2 >= this.y && var2 <= this.y + this.h;
   }
}
