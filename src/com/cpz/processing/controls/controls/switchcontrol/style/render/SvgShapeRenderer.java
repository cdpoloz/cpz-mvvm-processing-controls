package com.cpz.processing.controls.controls.switchcontrol.style.render;

import com.cpz.processing.controls.controls.switchcontrol.style.SwitchRenderStyle;
import processing.core.PApplet;
import processing.core.PShape;

public final class SvgShapeRenderer implements ShapeRenderer {
   private final PShape shape;

   public SvgShapeRenderer(PApplet var1, String var2) {
      this.shape = loadShape(var1, var2);
      if (this.shape != null) {
         this.shape.disableStyle();
      }

   }

   public void render(PApplet var1, float var2, float var3, float var4, float var5, SwitchRenderStyle var6) {
      if (this.shape != null) {
         var1.pushStyle();
         var1.shapeMode(3);
         var1.fill(var6.fillColor());
         var1.stroke(var6.strokeColor());
         var1.strokeWeight(var6.strokeWeight());
         var1.shape(this.shape, var2, var3, var4, var5);
         var1.popStyle();
      }
   }

   private static PShape loadShape(PApplet var0, String var1) {
      if (var0 != null && var1 != null && !var1.isEmpty()) {
         PShape var2 = var0.loadShape(var1);
         if (var2 == null && var1.startsWith("data/")) {
            var2 = var0.loadShape(var1.substring("data/".length()));
         }

         return var2;
      } else {
         return null;
      }
   }
}
