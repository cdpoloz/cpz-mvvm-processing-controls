package com.cpz.processing.controls.controls.checkbox.style.render;

import com.cpz.processing.controls.controls.checkbox.style.CheckboxRenderStyle;
import processing.core.PApplet;

/**
 * Renderer for default checkbox renderer.
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
public final class DefaultCheckboxRenderer implements CheckboxRenderer {
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
   public void render(PApplet sketch, float x, float y, float width, float height, CheckboxRenderStyle renderStyle) {
      float value = x - width * 0.5F;
      float value2 = y - height * 0.5F;
      sketch.pushStyle();
      sketch.fill(renderStyle.boxFillColor());
      sketch.stroke(renderStyle.borderColor());
      sketch.strokeWeight(renderStyle.borderWidth());
      sketch.rect(value, value2, width, height, renderStyle.cornerRadius());
      if (renderStyle.showCheck()) {
         float value3 = Math.min(width, height) * renderStyle.checkInset();
         float value4 = value + value3;
         float value5 = value2 + height * 0.55F;
         float value6 = value + width * 0.45F;
         float value7 = value2 + height - value3;
         float value8 = value + width - value3;
         float value9 = value2 + value3;
         sketch.stroke(renderStyle.checkColor());
         sketch.strokeWeight(renderStyle.checkStrokeWeight());
         sketch.noFill();
         sketch.line(value4, value5, value6, value7);
         sketch.line(value6, value7, value8, value9);
      }

      sketch.popStyle();
   }
}
