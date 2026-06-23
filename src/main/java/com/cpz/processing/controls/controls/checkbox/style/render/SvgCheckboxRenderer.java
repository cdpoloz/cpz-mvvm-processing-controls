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
 *
 * @author CPZ
 */
public final class SvgCheckboxRenderer implements CheckboxRenderer {
   private final PShape shape;

   /**
    * Creates a svg checkbox renderer.
    *
    * @param sketch parameter used by this operation
    * @param path parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SvgCheckboxRenderer(PApplet sketch, String path) {
      this.shape = loadShape(sketch, path);
      if (this.shape != null) {
         this.shape.disableStyle();
      }

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
   public void render(PApplet sketch, float x, float y, float width, float height, CheckboxRenderStyle renderStyle) {
      float value = x - width * 0.5F;
      float value2 = y - height * 0.5F;
      sketch.pushStyle();
      if (this.shape != null) {
         sketch.shapeMode(3);
         sketch.fill(renderStyle.boxFillColor());
         sketch.stroke(renderStyle.borderColor());
         sketch.strokeWeight(renderStyle.borderWidth());
         sketch.shape(this.shape, x, y, width, height);
      }

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

   private static PShape loadShape(PApplet sketch, String path) {
      if (sketch != null && path != null && !path.isEmpty()) {
         PShape shape = sketch.loadShape(path);
         if (shape == null && path.startsWith("data/")) {
            shape = sketch.loadShape(path.substring("data/".length()));
         }

         return shape;
      } else {
         return null;
      }
   }
}
