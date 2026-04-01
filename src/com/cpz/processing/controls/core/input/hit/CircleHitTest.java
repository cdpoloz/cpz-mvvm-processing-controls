package com.cpz.processing.controls.core.input.hit;

import com.cpz.processing.controls.core.input.hit.interfaces.HitTest;

public final class CircleHitTest implements HitTest {
   private float cx;
   private float cy;
   private float r;

   public CircleHitTest() {
   }

   public CircleHitTest(float var1, float var2, float var3) {
      this.set(var1, var2, var3);
   }

   public void onLayout(float var1, float var2, float var3, float var4) {
      float var5 = Math.min(var3, var4);
      this.set(var1, var2, var5 * 0.5F);
   }

   public void set(float var1, float var2, float var3) {
      this.cx = var1;
      this.cy = var2;
      this.r = var3;
   }

   public boolean contains(float var1, float var2) {
      float var3 = var1 - this.cx;
      float var4 = var2 - this.cy;
      return var3 * var3 + var4 * var4 <= this.r * this.r;
   }
}
