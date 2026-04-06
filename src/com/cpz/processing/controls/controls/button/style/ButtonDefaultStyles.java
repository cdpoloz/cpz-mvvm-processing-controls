package com.cpz.processing.controls.controls.button.style;

import com.cpz.processing.controls.controls.button.config.ButtonStyleConfig;
import com.cpz.processing.controls.core.theme.ThemeProvider;

public final class ButtonDefaultStyles {
   private ButtonDefaultStyles() {
   }

   public static ButtonStyle primary() {
      return primary((ThemeProvider)null);
   }

   public static ButtonStyle primary(ThemeProvider var0) {
      ButtonStyleConfig var1 = new ButtonStyleConfig();
      var1.strokeWeight = 2.0F;
      var1.strokeWeightHover = 3.0F;
      var1.cornerRadius = 10.0F;
      var1.themeProvider = var0;
      return new DefaultButtonStyle(var1);
   }
}
