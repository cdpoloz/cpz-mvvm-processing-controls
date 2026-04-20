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
 *
 * @author CPZ
 */
public final class SvgShapeRenderer implements ToggleShapeRenderer {
   private final PShape shape;

   /**
    * Creates a svg shape renderer.
    *
    * @param sketch parameter used by this operation
    * @param path parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public SvgShapeRenderer(PApplet sketch, String path) {
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
   public void render(PApplet sketch, float x, float y, float width, float height, ToggleRenderStyle renderStyle) {
      if (this.shape != null) {
         sketch.pushStyle();
         sketch.shapeMode(3);
         sketch.fill(renderStyle.fillColor());
         sketch.stroke(renderStyle.strokeColor());
         sketch.strokeWeight(renderStyle.strokeWeight());
         sketch.shape(this.shape, x, y, width, height);
         sketch.popStyle();
      }
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
