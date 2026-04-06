package com.cpz.processing.controls.core.theme;

import com.cpz.processing.controls.core.util.Colors;

/**
 * Theme component for light theme.
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
public final class LightTheme extends Theme {
   /**
    * Creates a light theme.
    *
    * Behavior:
    * - Initializes the public state required by this type.
    */
   public LightTheme() {
      super(createTokens());
   }

   private static ThemeTokens createTokens() {
      ThemeTokens var0 = new ThemeTokens();
      var0.surface = Colors.rgb(245, 247, 250);
      var0.surfaceVariant = Colors.rgb(230, 235, 241);
      var0.onSurface = Colors.rgb(28, 36, 46);
      var0.onSurfaceVariant = Colors.rgb(86, 98, 112);
      var0.primary = Colors.rgb(54, 122, 224);
      var0.onPrimary = Colors.gray(255);
      var0.border = Colors.rgb(154, 167, 184);
      var0.hoverOverlay = Colors.argb(28, 255, 255, 255);
      var0.pressedOverlay = Colors.argb(40, 0, 0, 0);
      var0.disabledAlpha = 120;
      var0.cursor = Colors.rgb(32, 102, 210);
      var0.selection = Colors.argb(110, 89, 156, 255);
      return var0;
   }
}
