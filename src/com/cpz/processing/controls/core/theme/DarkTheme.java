package com.cpz.processing.controls.core.theme;

import com.cpz.processing.controls.core.util.Colors;

public final class DarkTheme extends Theme {
   public DarkTheme() {
      super(createTokens());
   }

   private static ThemeTokens createTokens() {
      ThemeTokens var0 = new ThemeTokens();
      var0.surface = Colors.rgb(27, 31, 38);
      var0.surfaceVariant = Colors.rgb(42, 49, 60);
      var0.onSurface = Colors.rgb(235, 240, 246);
      var0.onSurfaceVariant = Colors.rgb(176, 186, 198);
      var0.primary = Colors.rgb(110, 176, 255);
      var0.onPrimary = Colors.rgb(13, 25, 44);
      var0.border = Colors.rgb(92, 108, 128);
      var0.hoverOverlay = Colors.argb(34, 255, 255, 255);
      var0.pressedOverlay = Colors.argb(54, 0, 0, 0);
      var0.disabledAlpha = 130;
      var0.cursor = Colors.rgb(140, 196, 255);
      var0.selection = Colors.argb(120, 78, 129, 196);
      return var0;
   }
}
