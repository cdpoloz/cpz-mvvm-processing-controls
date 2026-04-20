package com.cpz.processing.controls.core.theme;

import com.cpz.processing.controls.core.util.Colors;

/**
 * Built-in dark theme token set.
 *
 * @author CPZ
 */
public final class DarkTheme extends Theme {
   /**
    * Creates the default dark theme.
    */
   public DarkTheme() {
      super(createTokens());
   }

   private static ThemeTokens createTokens() {
      ThemeTokens tokens = new ThemeTokens();
      tokens.surface = Colors.rgb(27, 31, 38);
      tokens.surfaceVariant = Colors.rgb(42, 49, 60);
      tokens.onSurface = Colors.rgb(235, 240, 246);
      tokens.onSurfaceVariant = Colors.rgb(176, 186, 198);
      tokens.primary = Colors.rgb(110, 176, 255);
      tokens.onPrimary = Colors.rgb(13, 25, 44);
      tokens.border = Colors.rgb(92, 108, 128);
      tokens.hoverOverlay = Colors.argb(34, 255, 255, 255);
      tokens.pressedOverlay = Colors.argb(54, 0, 0, 0);
      tokens.disabledAlpha = 130;
      tokens.cursor = Colors.rgb(140, 196, 255);
      tokens.selection = Colors.argb(120, 78, 129, 196);
      return tokens;
   }
}
