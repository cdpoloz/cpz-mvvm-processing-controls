package com.cpz.processing.controls.controls.checkbox.style.render;

import com.cpz.processing.controls.controls.checkbox.style.CheckboxRenderStyle;
import processing.core.PApplet;

public final class DefaultCheckboxRenderer implements CheckboxRenderer {
   public void render(PApplet var1, float var2, float var3, float var4, float var5, CheckboxRenderStyle var6) {
      float var7 = var2 - var4 * 0.5F;
      float var8 = var3 - var5 * 0.5F;
      var1.pushStyle();
      var1.fill(var6.boxFillColor());
      var1.stroke(var6.borderColor());
      var1.strokeWeight(var6.borderWidth());
      var1.rect(var7, var8, var4, var5, var6.cornerRadius());
      if (var6.showCheck()) {
         float var9 = Math.min(var4, var5) * var6.checkInset();
         float var10 = var7 + var9;
         float var11 = var8 + var5 * 0.55F;
         float var12 = var7 + var4 * 0.45F;
         float var13 = var8 + var5 - var9;
         float var14 = var7 + var4 - var9;
         float var15 = var8 + var9;
         var1.stroke(var6.checkColor());
         var1.strokeWeight(var6.checkStrokeWeight());
         var1.noFill();
         var1.line(var10, var11, var12, var13);
         var1.line(var12, var13, var14, var15);
      }

      var1.popStyle();
   }
}
