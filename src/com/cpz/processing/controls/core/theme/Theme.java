package com.cpz.processing.controls.core.theme;

public class Theme {
   private final ThemeTokens tokens;

   public Theme(ThemeTokens var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("tokens must not be null");
      } else {
         this.tokens = var1.copy();
      }
   }

   public ThemeTokens tokens() {
      return this.tokens.copy();
   }

   public Theme copy() {
      return new Theme(this.tokens);
   }
}
