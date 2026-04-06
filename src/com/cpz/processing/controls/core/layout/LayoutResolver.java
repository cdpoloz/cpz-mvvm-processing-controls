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
 */
public final class LayoutResolver {
   private LayoutResolver() {
   }

   /**
    * Resolves x.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return resolved x
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static float resolveX(LayoutConfig var0, float var1, float var2) {
      float var3 = var0.getNormalizedX() * var2;
      switch (var0.getAnchor()) {
         case TOP_CENTER:
         case CENTER:
         case BOTTOM_CENTER:
            return var3 - var1 / 2.0F;
         case TOP_RIGHT:
         case BOTTOM_RIGHT:
            return var3 - var1;
         default:
            return var3;
      }
   }

   /**
    * Resolves y.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return resolved y
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public static float resolveY(LayoutConfig var0, float var1, float var2) {
      float var3 = var0.getNormalizedY() * var2;
      switch (var0.getAnchor()) {
         case TOP_CENTER:
         case TOP_RIGHT:
         case TOP_LEFT:
            return var3;
         case CENTER:
            return var3 - var1 / 2.0F;
         case BOTTOM_CENTER:
         case BOTTOM_RIGHT:
         case BOTTOM_LEFT:
            return var3 - var1;
         default:
            return var3;
      }
   }
}
