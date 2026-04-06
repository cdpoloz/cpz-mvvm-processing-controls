package com.cpz.processing.controls.core.theme;

public final class ThemeTokens {
   public int surface;
   public int surfaceVariant;
   public int onSurface;
   public int onSurfaceVariant;
   public int primary;
   public int onPrimary;
   public int border;
   public int hoverOverlay;
   public int pressedOverlay;
   public int disabledAlpha;
   public int cursor;
   public int selection;

   public ThemeTokens() {
   }

   public ThemeTokens(ThemeTokens var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("source must not be null");
      } else {
         this.surface = var1.surface;
         this.surfaceVariant = var1.surfaceVariant;
         this.onSurface = var1.onSurface;
         this.onSurfaceVariant = var1.onSurfaceVariant;
         this.primary = var1.primary;
         this.onPrimary = var1.onPrimary;
         this.border = var1.border;
         this.hoverOverlay = var1.hoverOverlay;
         this.pressedOverlay = var1.pressedOverlay;
         this.disabledAlpha = var1.disabledAlpha;
         this.cursor = var1.cursor;
         this.selection = var1.selection;
      }
   }

   public ThemeTokens copy() {
      return new ThemeTokens(this);
   }
}
