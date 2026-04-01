package com.cpz.processing.controls.core.theme;

public final class ThemeManager {
   private static Theme currentTheme = new LightTheme();

   private ThemeManager() {
   }

   public static Theme getTheme() {
      return currentTheme;
   }

   public static void setTheme(Theme var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Theme null");
      } else {
         currentTheme = var0;
      }
   }
}
