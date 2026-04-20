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
 *
 * @author CPZ
 */
public final class Colors {
   private Colors() {
   }

   /**
    * Performs agray.
    *
    * @param alpha parameter used by this operation
    * @param value parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int agray(int alpha, int value) {
      return argb(alpha, value, value, value);
   }

   /**
    * Performs gray.
    *
    * @param value parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int gray(int value) {
      return rgb(value, value, value);
   }

   /**
    * Performs rgb.
    *
    * @param red parameter used by this operation
    * @param green parameter used by this operation
    * @param blue parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int rgb(int red, int green, int blue) {
      return argb(255, red, green, blue);
   }

   /**
    * Performs argb.
    *
    * @param alpha parameter used by this operation
    * @param red parameter used by this operation
    * @param green parameter used by this operation
    * @param blue parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int argb(int alpha, int red, int green, int blue) {
      alpha = clamp(alpha);
      red = clamp(red);
      green = clamp(green);
      blue = clamp(blue);
      return (alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | blue & 255;
   }

   /**
    * Performs rgb pct.
    *
    * @param redPercent parameter used by this operation
    * @param greenPercent parameter used by this operation
    * @param bluePercent parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int rgbPct(int redPercent, int greenPercent, int bluePercent) {
      return rgb(pctToByte(redPercent), pctToByte(greenPercent), pctToByte(bluePercent));
   }

   /**
    * Performs argb pct.
    *
    * @param alphaPercent parameter used by this operation
    * @param redPercent parameter used by this operation
    * @param greenPercent parameter used by this operation
    * @param bluePercent parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int argbPct(int alphaPercent, int redPercent, int greenPercent, int bluePercent) {
      return argb(pctToByte(alphaPercent), pctToByte(redPercent), pctToByte(greenPercent), pctToByte(bluePercent));
   }

   /**
    * Performs gray pct.
    *
    * @param valuePercent parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int grayPct(int valuePercent) {
      return gray(pctToByte(valuePercent));
   }

   /**
    * Performs agray pct.
    *
    * @param alphaPercent parameter used by this operation
    * @param valuePercent parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int agrayPct(int alphaPercent, int valuePercent) {
      return agray(pctToByte(alphaPercent), pctToByte(valuePercent));
   }

   private static int pctToByte(int value) {
      return Math.max(0, Math.min(100, value)) * 255 / 100;
   }

   /**
    * Performs alpha.
    *
    * @param alpha parameter used by this operation
    * @param color parameter used by this operation
    * @return result of this operation
    *
    * Behavior:
    * - Executes the public operation exposed by this type.
    */
   public static int alpha(int alpha, int color) {
      return color & 16777215 | (alpha & 255) << 24;
   }

   private static int clamp(int value) {
      return Math.max(0, Math.min(255, value));
   }
}
