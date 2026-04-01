package com.cpz.processing.controls.core.util;

public final class Colors {
   private Colors() {
   }

   public static int agray(int var0, int var1) {
      return argb(var0, var1, var1, var1);
   }

   public static int gray(int var0) {
      return rgb(var0, var0, var0);
   }

   public static int rgb(int var0, int var1, int var2) {
      return argb(255, var0, var1, var2);
   }

   public static int argb(int var0, int var1, int var2, int var3) {
      var0 = clamp(var0);
      var1 = clamp(var1);
      var2 = clamp(var2);
      var3 = clamp(var3);
      return (var0 & 255) << 24 | (var1 & 255) << 16 | (var2 & 255) << 8 | var3 & 255;
   }

   public static int rgbPct(int var0, int var1, int var2) {
      return rgb(pctToByte(var0), pctToByte(var1), pctToByte(var2));
   }

   public static int argbPct(int var0, int var1, int var2, int var3) {
      return argb(pctToByte(var0), pctToByte(var1), pctToByte(var2), pctToByte(var3));
   }

   public static int grayPct(int var0) {
      return gray(pctToByte(var0));
   }

   public static int agrayPct(int var0, int var1) {
      return agray(pctToByte(var0), pctToByte(var1));
   }

   private static int pctToByte(int var0) {
      return Math.max(0, Math.min(100, var0)) * 255 / 100;
   }

   public static int alpha(int var0, int var1) {
      return var1 & 16777215 | (var0 & 255) << 24;
   }

   private static int clamp(int var0) {
      return Math.max(0, Math.min(255, var0));
   }
}
