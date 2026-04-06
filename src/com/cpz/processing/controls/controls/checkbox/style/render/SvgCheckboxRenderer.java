package com.cpz.processing.controls.controls.checkbox.style.render;

import com.cpz.processing.controls.controls.checkbox.style.CheckboxRenderStyle;
import processing.core.PApplet;
import processing.core.PShape;

/**
 * Renderer for svg checkbox renderer.
 *
 * Responsibilities:
 * - Draw already resolved frame data.
 * - Keep rendering concerns separate from state decisions.
 *
 * Behavior:
 * - Uses already resolved state and does not decide behavior.
 *
 * Notes:
 * - This type belongs to the visual styling pipeline.
 */
public final class SvgCheckboxRenderer implements CheckboxRenderer {
   private final PShape shape;

   /**
    * Creates a svg checkbox renderer.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SvgCheckboxRenderer(PApplet var1, String var2) {
      this.shape = loadShape(var1, var2);
      if (this.shape != null) {
         this.shape.disableStyle();
      }

   }

   /**
    * Renders the current frame.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @param var4 parameter used by this operation
    * @param var5 parameter used by this operation
    * @param var6 parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet var1, float var2, float var3, float var4, float var5, CheckboxRenderStyle var6) {
      float var7 = var2 - var4 * 0.5F;
      float var8 = var3 - var5 * 0.5F;
      var1.pushStyle();
      if (this.shape != null) {
         var1.shapeMode(3);
         var1.fill(var6.boxFillColor());
         var1.stroke(var6.borderColor());
         var1.strokeWeight(var6.borderWidth());
         var1.shape(this.shape, var2, var3, var4, var5);
      }

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
