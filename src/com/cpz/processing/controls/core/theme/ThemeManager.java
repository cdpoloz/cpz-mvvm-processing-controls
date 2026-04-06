package com.cpz.processing.controls.core.theme;

public final class ThemeManager implements ThemeProvider {
   private static final ThemeManager DEFAULT = new ThemeManager();
   private Theme currentTheme;

   public ThemeManager() {
      this(new LightTheme());
   }

   public ThemeManager(Theme var1) {
      this.setTheme(var1 == null ? new LightTheme() : var1);
   }

   public Theme getTheme() {
      return this.currentTheme.copy();
   }

   public void setTheme(Theme var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Theme null");
      } else {
         this.currentTheme = var1.copy();
      }
   }

   public static Theme getDefaultTheme() {
      return DEFAULT.getTheme();
   }
}
