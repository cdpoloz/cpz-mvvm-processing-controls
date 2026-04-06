package com.cpz.processing.controls.core.util;

/**
 * Utility component for colors.
 *
 * Responsibilities:
 * - Expose a public architectural role.
 * - Keep responsibilities explicit in the API surface.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
public final class Colors {
   private Colors() {
   }

   /**
    * Performs agray.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int agray(int var0, int var1) {
      return argb(var0, var1, var1, var1);
   }

   /**
    * Performs gray.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int gray(int var0) {
      return rgb(var0, var0, var0);
   }

   /**
    * Performs rgb.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int rgb(int var0, int var1, int var2) {
      return argb(255, var0, var1, var2);
   }

   /**
    * Performs argb.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int argb(int var0, int var1, int var2, int var3) {
      var0 = clamp(var0);
      var1 = clamp(var1);
      var2 = clamp(var2);
      var3 = clamp(var3);
      return (var0 & 255) << 24 | (var1 & 255) << 16 | (var2 & 255) << 8 | var3 & 255;
   }

   /**
    * Performs rgb pct.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int rgbPct(int var0, int var1, int var2) {
      return rgb(pctToByte(var0), pctToByte(var1), pctToByte(var2));
   }

   /**
    * Performs argb pct.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @param var2 parameter used by this operation
    * @param var3 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int argbPct(int var0, int var1, int var2, int var3) {
      return argb(pctToByte(var0), pctToByte(var1), pctToByte(var2), pctToByte(var3));
   }

   /**
    * Performs gray pct.
    *
    * @param var0 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int grayPct(int var0) {
      return gray(pctToByte(var0));
   }

   /**
    * Performs agray pct.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int agrayPct(int var0, int var1) {
      return agray(pctToByte(var0), pctToByte(var1));
   }

   private static int pctToByte(int var0) {
      return Math.max(0, Math.min(100, var0)) * 255 / 100;
   }

   /**
    * Performs alpha.
    *
    * @param var0 parameter used by this operation
    * @param var1 parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int alpha(int var0, int var1) {
      return var1 & 16777215 | (var0 & 255) << 24;
   }

   private static int clamp(int var0) {
      return Math.max(0, Math.min(255, var0));
   }
}
