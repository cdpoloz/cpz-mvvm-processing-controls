package com.cpz.processing.controls.controls.label.style;

import com.cpz.processing.controls.controls.label.config.LabelStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class LabelDefaultStyles {
   private LabelDefaultStyles() {
   }

   public static LabelStyle defaultText() {
      return defaultText((ThemeProvider)null);
   }

   public static LabelStyle defaultText(ThemeProvider var0) {
      LabelStyleConfig var1 = new LabelStyleConfig();
      var1.font = null;
      var1.textSize = 12.0F;
      var1.textColor = null;
      var1.lineSpacingMultiplier = 1.0F;
      var1.alignX = HorizontalAlign.START;
      var1.alignY = VerticalAlign.BASELINE;
      var1.disabledAlpha = null;
      var1.themeProvider = var0;
      return new DefaultLabelStyle(var1);
   }
}
