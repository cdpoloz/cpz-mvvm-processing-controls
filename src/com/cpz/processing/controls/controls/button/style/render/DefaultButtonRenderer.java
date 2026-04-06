package com.cpz.processing.controls.controls.button.style.render;

import com.cpz.processing.controls.controls.button.style.ButtonRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default button renderer.
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
public final class DefaultButtonRenderer implements ButtonRenderer {
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
   public void render(PApplet var1, float var2, float var3, float var4, float var5, ButtonRenderStyle var6) {
      float var7 = var2 - var4 * 0.5F;
      float var8 = var3 - var5 * 0.5F;
      String var9 = var6.text();
      if (var9 == null) {
         var9 = "";
      }

      var1.pushStyle();
      var1.fill(var6.fillColor());
      var1.stroke(var6.strokeColor());
      var1.strokeWeight(var6.strokeWeight());
      var1.rect(var7, var8, var4, var5, var6.cornerRadius());
      if (var6.showText()) {
         var1.fill(var6.textColor());
         var1.textAlign(3, 3);
         var1.text(var9, var2, var3);
      }

      var1.popStyle();
   }
}
