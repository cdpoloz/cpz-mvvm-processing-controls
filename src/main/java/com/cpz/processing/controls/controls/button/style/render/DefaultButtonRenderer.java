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
 *
 * @author CPZ
 */
public final class DefaultButtonRenderer implements ButtonRenderer {
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
   public void render(PApplet sketch, float x, float y, float width, float height, ButtonRenderStyle renderStyle) {
      float value = x - width * 0.5F;
      float value2 = y - height * 0.5F;
      String text = renderStyle.text();
      if (text == null) {
         text = "";
      }

      sketch.pushStyle();
      sketch.fill(renderStyle.fillColor());
      sketch.stroke(renderStyle.strokeColor());
      sketch.strokeWeight(renderStyle.strokeWeight());
      sketch.rect(value, value2, width, height, renderStyle.cornerRadius());
      if (renderStyle.showText()) {
         sketch.fill(renderStyle.textColor());
         sketch.textAlign(3, 3);
         sketch.text(text, x, y);
      }

      sketch.popStyle();
   }
}
