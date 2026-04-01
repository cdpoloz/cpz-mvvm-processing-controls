package com.cpz.processing.controls.controls.switchcontrol.style.render;

import com.cpz.processing.controls.controls.switchcontrol.style.SwitchRenderStyle;
import processing.core.PApplet;

public final class RectShapeRenderer implements ShapeRenderer {
   private final float cornerRadius;

   public RectShapeRenderer() {
      this(0.0F);
   }

   public RectShapeRenderer(float var1) {
      this.cornerRadius = var1;
   }

   public void render(PApplet var1, float var2, float var3, float var4, float var5, SwitchRenderStyle var6) {
      float var7 = var4 * 0.5F;
      float var8 = var5 * 0.5F;
      float var9 = var2 - var7;
      float var10 = var3 - var8;
      var1.pushStyle();
      var1.stroke(var6.strokeColor());
      var1.strokeWeight(var6.strokeWeight());
      var1.fill(var6.fillColor());
      var1.rect(var9, var10, var4, var5, this.cornerRadius);
      var1.popStyle();
   }
}
