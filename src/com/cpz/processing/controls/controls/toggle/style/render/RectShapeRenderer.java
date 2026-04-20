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
 *
 * @author CPZ
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
    * @param x parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public RectShapeRenderer(float x) {
      this.cornerRadius = x;
   }

   /**
    * Renders the current frame.
    *
    * @param sketch parameter used by this operation
    * @param x parameter used by this operation
    * @param y parameter used by this operation
    * @param width parameter used by this operation
    * @param height parameter used by this operation
    * @param renderStyle parameter used by this operation
    *
    * Behavior:
    * - Uses already available state and does not define business rules.
    */
   public void render(PApplet sketch, float x, float y, float width, float height, ToggleRenderStyle renderStyle) {
      float value = width * 0.5F;
      float value2 = height * 0.5F;
      float value3 = x - value;
      float value4 = y - value2;
      sketch.pushStyle();
      sketch.stroke(renderStyle.strokeColor());
      sketch.strokeWeight(renderStyle.strokeWeight());
      sketch.fill(renderStyle.fillColor());
      sketch.rect(value3, value4, width, height, this.cornerRadius);
      sketch.popStyle();
   }
}
