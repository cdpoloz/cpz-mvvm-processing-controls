package com.cpz.processing.controls.controls.toggle.style.render;

import com.cpz.processing.controls.controls.toggle.style.ToggleRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for circle shape renderer.
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
public final class CircleShapeRenderer implements ToggleShapeRenderer {
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
      var1.pushStyle();
      var1.stroke(var6.strokeColor());
      var1.strokeWeight(var6.strokeWeight());
      var1.fill(var6.fillColor());
      float var7 = Math.min(var4, var5);
      var1.circle(var2, var3, var7);
      var1.popStyle();
   }
}
