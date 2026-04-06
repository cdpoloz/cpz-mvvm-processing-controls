package com.cpz.processing.controls.controls.toggle.style.render;

import com.cpz.processing.controls.controls.toggle.style.ToggleRenderStyle;
import processing.core.PApplet;
import processing.core.PShape;

/**
 * Renderer for svg shape renderer.
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
public final class SvgShapeRenderer implements ToggleShapeRenderer {
   private final PShape shape;

   /**
    * Creates a svg shape renderer.
    *
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SvgShapeRenderer(PApplet var1, String var2) {
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
   public void render(PApplet var1, float var2, float var3, float var4, float var5, ToggleRenderStyle var6) {
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
