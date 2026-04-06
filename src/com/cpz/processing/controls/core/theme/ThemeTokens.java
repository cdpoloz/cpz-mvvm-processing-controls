package com.cpz.processing.controls.core.theme;

/**
 * Theme component for theme tokens.
 *
 * Responsibilities:
 * - Represent theme data or theme access for the rendering pipeline.
 * - Keep theme concerns explicit and reusable.
 *
 * Behavior:
 * - Keeps the public role isolated from unrelated concerns.
 *
 * Notes:
 * - This type is part of the public project surface.
 */
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

   /**
    * Creates a theme tokens.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public ThemeTokens() {
   }

   /**
    * Creates a theme tokens.
    *
    * @param var1 parameter used by this operation
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
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

   /**
    * Returns a copy of state.
    *
    * @return copied value
    *
    * Behavior:
    * - Produces the public result required by the surrounding pipeline.
    */
   public ThemeTokens copy() {
      return new ThemeTokens(this);
   }
}
