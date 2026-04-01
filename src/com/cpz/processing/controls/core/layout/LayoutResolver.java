package com.cpz.processing.controls.core.layout;

public final class LayoutResolver {
   private LayoutResolver() {
   }

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
