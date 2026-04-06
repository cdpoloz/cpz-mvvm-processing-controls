package com.cpz.processing.controls.core.theme;

public final class ThemeManager implements ThemeProvider {
   private Theme currentTheme;
   private ThemeSnapshot snapshot;

   public ThemeManager() {
      this(new LightTheme());
   }

   public ThemeManager(Theme var1) {
      this.setTheme(var1 == null ? new LightTheme() : var1);
   }

   public ThemeSnapshot getSnapshot() {
      return this.snapshot;
   }

   public void setTheme(Theme var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Theme null");
      } else {
         this.currentTheme = var1.copy();
         this.snapshot = new ThemeSnapshot(this.currentTheme);
      }
   }
}
