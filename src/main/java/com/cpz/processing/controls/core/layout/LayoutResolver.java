package com.cpz.processing.controls.core.layout;

/**
 * Layout component for layout resolver.
 *
 * Responsibilities:
 * - Represent layout data or coordinate resolution.
 * - Keep placement logic outside control behavior.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 *
 * @author CPZ
 */
public final class LayoutResolver {
   private LayoutResolver() {
   }

   /**
    * Resolves x.
    *
    * @param layout parameter used by this operation
    * @param controlSize parameter used by this operation
    * @param canvasSize parameter used by this operation
    * @return resolved x
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static float resolveX(LayoutConfig layout, float controlSize, float canvasSize) {
      float anchorX = layout.getNormalizedX() * canvasSize;
      switch (layout.getAnchor()) {
         case TOP_CENTER:
         case CENTER:
         case BOTTOM_CENTER:
            return anchorX - controlSize / 2.0F;
         case TOP_RIGHT:
         case BOTTOM_RIGHT:
            return anchorX - controlSize;
         default:
            return anchorX;
      }
   }

   /**
    * Resolves y.
    *
    * @param layout parameter used by this operation
    * @param controlSize parameter used by this operation
    * @param canvasSize parameter used by this operation
    * @return resolved y
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static float resolveY(LayoutConfig layout, float controlSize, float canvasSize) {
      float anchorX = layout.getNormalizedY() * canvasSize;
      switch (layout.getAnchor()) {
         case TOP_CENTER:
         case TOP_RIGHT:
         case TOP_LEFT:
            return anchorX;
         case CENTER:
            return anchorX - controlSize / 2.0F;
         case BOTTOM_CENTER:
         case BOTTOM_RIGHT:
         case BOTTOM_LEFT:
            return anchorX - controlSize;
         default:
            return anchorX;
      }
   }
}
