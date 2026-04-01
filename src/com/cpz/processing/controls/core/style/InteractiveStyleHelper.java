package com.cpz.processing.controls.core.style;

import com.cpz.processing.controls.core.util.Colors;
import processing.core.PApplet;

public final class InteractiveStyleHelper {
   private InteractiveStyleHelper() {
   }

   public static int resolveFillColor(int var0, int var1, int var2, boolean var3, boolean var4) {
      if (var4) {
         return var2;
      } else {
         return var3 ? var1 : var0;
      }
   }

   public static int resolveFillColorWithOverlays(int var0, int var1, int var2, boolean var3, boolean var4) {
      return resolveFillColor(var0, applyOverlay(var0, var1), applyOverlay(var0, var2), var3, var4);
   }

   public static int resolveFillColor(PApplet var0, int var1, float var2, float var3, boolean var4, boolean var5) {
      int var6 = var0.lerpColor(var1, var0.color(255), var2);
      int var7 = var0.lerpColor(var1, var0.color(0), var3);
      return resolveFillColor(var1, var6, var7, var4, var5);
   }

   public static int applyDisabledAlpha(int var0, boolean var1, int var2) {
      return var1 ? var0 : Colors.alpha(var2, var0);
   }

   public static int applyOverlay(int var0, int var1) {
      float var2 = (float)(var1 >>> 24 & 255) / 255.0F;
      int var3 = blendChannel(var0 >>> 24 & 255, var1 >>> 24 & 255, var2);
      int var4 = blendChannel(var0 >>> 16 & 255, var1 >>> 16 & 255, var2);
      int var5 = blendChannel(var0 >>> 8 & 255, var1 >>> 8 & 255, var2);
      int var6 = blendChannel(var0 & 255, var1 & 255, var2);
      return Colors.argb(var3, var4, var5, var6);
   }

   public static float resolveStrokeWeight(float var0, float var1, boolean var2) {
      return var2 ? var1 : var0;
   }

   public static int resolveStrokeColor(int var0, boolean var1, int var2) {
      return applyDisabledAlpha(var0, var1, var2);
   }

   private static int blendChannel(int var0, int var1, float var2) {
      return Math.round((float)var0 + (float)(var1 - var0) * var2);
   }
}
