package com.cpz.processing.controls.core.theme;

public final class ThemeSnapshot {
   public final ThemeTokens tokens;

   public ThemeSnapshot(Theme var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("theme must not be null");
      } else {
         this.tokens = var1.tokens();
      }
   }
}
