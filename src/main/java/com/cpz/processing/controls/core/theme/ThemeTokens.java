package com.cpz.processing.controls.core.theme;

/**
 * Token bag consumed by styles to resolve concrete render colors.
 *
 * <p>Tokens are intentionally simple values. Themes copy them, snapshots expose
 * them to styles, and renderers receive already resolved render styles.</p>
 *
 * @author CPZ
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
    * Creates an empty token set for theme construction.
    */
   public ThemeTokens() {
   }

   /**
    * Creates a copy of another token set.
    *
    * @param tokens source tokens
    * @throws IllegalArgumentException when {@code tokens} is {@code null}
    */
   public ThemeTokens(ThemeTokens tokens) {
      if (tokens == null) {
         throw new IllegalArgumentException("source must not be null");
      } else {
         this.surface = tokens.surface;
         this.surfaceVariant = tokens.surfaceVariant;
         this.onSurface = tokens.onSurface;
         this.onSurfaceVariant = tokens.onSurfaceVariant;
         this.primary = tokens.primary;
         this.onPrimary = tokens.onPrimary;
         this.border = tokens.border;
         this.hoverOverlay = tokens.hoverOverlay;
         this.pressedOverlay = tokens.pressedOverlay;
         this.disabledAlpha = tokens.disabledAlpha;
         this.cursor = tokens.cursor;
         this.selection = tokens.selection;
      }
   }

   /**
    * Returns a defensive copy of this token set.
    *
    * @return copied tokens
    */
   public ThemeTokens copy() {
      return new ThemeTokens(this);
   }
}
