package com.cpz.processing.controls.controls.toggle.style.render;

import com.cpz.processing.controls.controls.toggle.style.ToggleRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for rect shape renderer.
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
public final class RectShapeRenderer implements ToggleShapeRenderer {
   private final float cornerRadius;

   /**
    * Creates a rect shape renderer.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RectShapeRenderer() {
      this(0.0F);
   }

   /**
    * Creates a rect shape renderer.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RectShapeRenderer(float var1) {
      this.cornerRadius = var1;
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
